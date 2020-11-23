package com.android.kotlin.data

import com.android.kotlin.data.model.Note
import com.android.kotlin.data.provider.FireStoreProvider
import com.android.kotlin.data.provider.RemoteDataProvider

class Repository(private val remoteProvider: RemoteDataProvider) {
    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun getCurrentUser() = remoteProvider.getCurrentUser()
    fun deleteNote(noteId: String) = remoteProvider.deleteNote(noteId)
}