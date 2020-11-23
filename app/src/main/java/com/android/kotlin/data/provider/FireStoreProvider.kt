package com.android.kotlin.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.kotlin.data.errors.NoAuthException
import com.android.kotlin.data.model.Note
import com.android.kotlin.data.model.Result
import com.android.kotlin.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"

class FireStoreProvider(private val firebaseAuth: FirebaseAuth,
                        private val db: FirebaseFirestore) : RemoteDataProvider {

    private val store = FirebaseFirestore.getInstance()

    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    private val userNotesCollection: CollectionReference
        get() = currentUser?.let {                                                                                                                                                                                                                                                                                                                                      //Я копипастил код с урока и не заметил эту надпись
            store.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
        } ?: throw NoAuthException()


    override fun getCurrentUser(): LiveData<User?> =
        MutableLiveData<User?>().apply {
            value = currentUser?.let { User(it.displayName ?: "",
                it.email ?: "") }
        }

    override fun subscribeToAllNotes() = MutableLiveData<Result>().apply {
        try {
            userNotesCollection.addSnapshotListener { snapshot, e ->
                e?.let {
                    throw it
                } ?: let {
                    snapshot?.let { snapshot ->
                        value = Result.Success(snapshot.map { it.toObject(Note::class.java) })
                    }
                }
            }
        } catch (e: Throwable){
            value = Result.Error(e)
        }
    }

    override fun getNoteById(id: String) = MutableLiveData<Result>().apply {
        try {
            userNotesCollection.document(id).get()
                .addOnSuccessListener { snapshot ->
                    value = Result.Success(snapshot.toObject(Note::class.java))
                }.addOnFailureListener {
                    value = Result.Error(it)
                }
        } catch (e: Throwable){
            value = Result.Error(e)
        }
    }


    override fun saveNote(note: Note) =
        MutableLiveData<Result>().apply {
        try {
            userNotesCollection.document(note.id).set(note)
                .addOnSuccessListener {
                    value = Result.Success(note)
                }.addOnFailureListener {
                    value = Result.Error(it)
                }
        } catch (e: Throwable){
            value = Result.Error(e)
        }
    }

    override fun deleteNote(noteId: String): LiveData<Result> =
        MutableLiveData<Result>().apply {
            userNotesCollection.document(noteId).delete()
                .addOnSuccessListener {
                    value = Result.Success(null)
                }.addOnFailureListener {
                    value = Result.Error(it)
                }
        }
}