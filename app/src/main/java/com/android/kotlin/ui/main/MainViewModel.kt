package com.android.kotlin.ui.main

import androidx.lifecycle.Observer
import com.android.kotlin.data.Repository
import com.android.kotlin.data.model.Note
import com.android.kotlin.data.model.NoteResult
import com.android.kotlin.data.model.NoteResult.Error
import com.android.kotlin.data.model.NoteResult.Success
import com.android.kotlin.ui.base.BaseViewModel

class MainViewModel(private val repository: Repository = Repository) : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = object : Observer<NoteResult> {
    override fun onChanged(t: NoteResult?) {
        if (t == null) return

        when(t) {
            is Success<*> -> {
                viewStateLiveData.value = MainViewState(notes = t.data as? List<Note>)
            }
            is Error -> {
                viewStateLiveData.value = MainViewState(error = t.error)
            }
        }
    }
    }

    private val repositoryNotes = repository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}
