package com.android.kotlin.data.provider

import androidx.lifecycle.LiveData
import com.android.kotlin.data.model.Note
import com.android.kotlin.data.model.Result
import com.android.kotlin.data.model.User

interface RemoteDataProvider {
    fun getCurrentUser(): LiveData<User?>
    fun subscribeToAllNotes(): LiveData<Result>
    fun getNoteById(id: String): LiveData<Result>
    fun saveNote(note: Note): LiveData<Result>
    fun deleteNote(noteId: String): LiveData<Result>
}