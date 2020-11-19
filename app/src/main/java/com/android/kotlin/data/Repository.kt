package com.android.kotlin.data

import com.android.kotlin.data.model.Note
import com.android.kotlin.data.provider.FireStoreProvider
import com.android.kotlin.data.provider.RemoteDataProvider

object Repository {

    private val remoteProvider: RemoteDataProvider = FireStoreProvider()

    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
}