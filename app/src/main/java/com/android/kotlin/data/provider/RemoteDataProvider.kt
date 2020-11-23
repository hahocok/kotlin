package com.android.kotlin.data.provider

import androidx.lifecycle.LiveData
import com.android.kotlin.data.model.Note
import com.android.kotlin.data.model.Result
import com.android.kotlin.data.model.User
import kotlinx.coroutines.channels.ReceiveChannel

interface RemoteDataProvider {
    fun getCurrentUser(): LiveData<User?>
    fun subscribeToAllNotes(): ReceiveChannel<Result>
    suspend fun getNoteById(id: String): Note
    fun saveNote(note: Note): LiveData<Result>
    fun deleteNote(noteId: String): LiveData<Result>
}