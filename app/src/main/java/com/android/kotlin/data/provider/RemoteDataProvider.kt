package com.android.kotlin.data.provider

import androidx.lifecycle.LiveData
import com.android.kotlin.data.model.Note
import com.android.kotlin.data.model.NoteResult
import com.android.kotlin.data.model.User

interface RemoteDataProvider {
    fun getCurrentUser(): LiveData<User?>
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
}