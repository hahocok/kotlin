package com.android.kotlin.ui.note

import com.android.kotlin.data.Repository
import com.android.kotlin.data.model.Note
import com.android.kotlin.ui.base.BaseViewModel
import com.android.kotlin.ui.base.NoteViewState.NoteData
import kotlinx.coroutines.launch


class NoteViewModel(private val repository: Repository) : BaseViewModel<NoteData>() {

    private val currentNote: Note?
        get() = getViewState().poll()?.note

    fun saveChanges(note: Note) {
        setData(NoteData(note = note))
    }

    fun loadNote(noteId: String) {
        launch {
            try {
                repository.getNoteById(noteId).let {
                    setData(NoteData(note = it))
                }
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }


    fun deleteNote() {
        launch {
            try {
                currentNote?.let { repository.deleteNote(it.id) }
                setData(NoteData(isDeleted = true))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }


    override fun onCleared() {
        launch {
            currentNote?.let { repository.saveNote(it) }
            super.onCleared()
        }
    }
}


