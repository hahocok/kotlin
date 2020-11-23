package com.android.kotlin.ui.main

import androidx.lifecycle.Observer
import com.android.kotlin.data.Repository
import com.android.kotlin.data.model.Note
import com.android.kotlin.data.model.Result
import com.android.kotlin.data.model.Result.Error
import com.android.kotlin.data.model.Result.Success
import com.android.kotlin.ui.base.BaseViewModel

class MainViewModel(private val repository: Repository) : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = object : Observer<Result> {
    override fun onChanged(t: Result?) {
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
