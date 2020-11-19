package com.android.kotlin.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.kotlin.data.model.Note
import com.android.kotlin.data.model.NoteResult
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

private const val NOTES_COLLECTION = "notes"

class FireStoreProvider : RemoteDataProvider {

    private val store = FirebaseFirestore.getInstance()
    private val notesReference = store.collection(NOTES_COLLECTION)

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.addSnapshotListener { snapshot, e ->
            e?.let {
                result.value = NoteResult.Error(e)
            } ?: let {
                snapshot?.let { snapshot ->
                    val notes = mutableListOf<Note>()
                    for (doc: QueryDocumentSnapshot in snapshot) {
                        notes.add(doc.toObject(Note::class.java))
                    }
                    result.value = NoteResult.Success(notes)
                }
            }
        }
        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(id).get()
            .addOnSuccessListener { snapshot ->
                result.value = NoteResult.Success(snapshot.toObject(Note::class.java))
            }.addOnFailureListener {
                result.value = NoteResult.Error(it)
            }

        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.document(note.id)
            .set(note)
            .addOnSuccessListener { result.value = NoteResult.Success(note) }
            .addOnFailureListener { OnFailureListener { p0 -> result.value = NoteResult.Error(p0) } }

        return result
    }
}