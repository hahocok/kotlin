package com.android.kotlin.ui.note

import com.android.kotlin.data.Repository
import com.android.kotlin.data.model.Note
import com.android.kotlin.data.model.Result.Error
import com.android.kotlin.data.model.Result.Success
import com.android.kotlin.ui.base.BaseViewModel
import com.android.kotlin.ui.base.NoteViewState
import com.android.kotlin.ui.base.NoteViewState.Data


class NoteViewModel(private val repository: Repository) : BaseViewModel<Data, NoteViewState>() {

    private val currentNote: Note?
        get() = viewStateLiveData.value?.data?.note

    fun saveChanges(note: Note) {
        viewStateLiveData.value = NoteViewState(Data(note = note))
    }

    override fun onCleared() {
        currentNote?.let { repository.saveNote(it) }
    }

    fun loadNote(noteId: String) {
        repository.getNoteById(noteId).observeForever { t ->
            t?.let {
                viewStateLiveData.value = when (t) {
                    is Success<*> -> NoteViewState(Data(note = t.data as? Note))
                    is Error -> NoteViewState(error = t.error)
                }
            }
        }
    }

    fun deleteNote() {
        currentNote?.let {
            repository.deleteNote(it.id).observeForever { t ->
                t?.let {
                    viewStateLiveData.value = when (it) {
                        is Success<*> -> NoteViewState(Data(isDeleted = true))
                        is Error -> NoteViewState(error = it.error)
                    }
                }
            }
        }
    }
}

