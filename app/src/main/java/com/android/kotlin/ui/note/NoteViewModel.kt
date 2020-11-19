package com.android.kotlin.ui.note

import androidx.lifecycle.Observer
import com.android.kotlin.data.Repository
import com.android.kotlin.data.model.Note
import com.android.kotlin.data.model.NoteResult
import com.android.kotlin.data.model.NoteResult.Error
import com.android.kotlin.data.model.NoteResult.Success
import com.android.kotlin.ui.base.BaseViewModel
import com.android.kotlin.ui.base.NoteViewState


class NoteViewModel(private val repository: Repository = Repository) : BaseViewModel<Note?, NoteViewState>() {

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        if (pendingNote != null) {
            repository.saveNote(pendingNote!!)
        }
    }

    fun loadNote(noteId: String) {
        repository.getNoteById(noteId).observeForever(object : Observer<NoteResult> {
            override fun onChanged(t: NoteResult?) {
                if (t == null) return

                when (t) {
                    is Success<*> ->
                        viewStateLiveData.value = NoteViewState(note = t.data as? Note)
                    is Error ->
                        viewStateLiveData.value = NoteViewState(error = t.error)
                }
            }
        })
    }
}

