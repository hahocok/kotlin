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
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

    override fun subscribeToAllNotes(): ReceiveChannel<Result> =
        Channel<Result>(Channel.CONFLATED).apply {
            var registration: ListenerRegistration? = null

            try {
                registration =
                    userNotesCollection.addSnapshotListener { snapshot, e ->
                        val value = e?.let {
                            Result.Error(it)
                        } ?: snapshot?.let {
                            val notes = it.documents.map { it ->
                                it.toObject(Note::class.java)
                            }
                            Result.Success(notes)
                        }

                        value?.let { offer(it) }
                    }
            } catch (e: Throwable) {
                offer(Result.Error(e))
            }

            invokeOnClose { registration?.remove() }
        }

    override suspend fun getNoteById(id: String): Note =
        suspendCoroutine { continuation ->
            try {
                userNotesCollection.document(id).get()
                    .addOnSuccessListener {
                        continuation.resume(it.toObject(Note::class.java)!!)
                    }.addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            } catch (e: Throwable){
                continuation.resumeWithException(e)
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