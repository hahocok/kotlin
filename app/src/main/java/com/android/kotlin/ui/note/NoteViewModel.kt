package com.android.kotlin.ui.note

import androidx.lifecycle.ViewModel
import com.android.kotlin.data.Repository
import com.android.kotlin.data.model.Note

class NoteViewModel : ViewModel() {

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        if (pendingNote != null) {
            Repository.saveNote(pendingNote!!)
        }
    }
}
