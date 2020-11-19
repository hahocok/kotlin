package com.android.kotlin.data.provider

import androidx.lifecycle.LiveData
import com.android.kotlin.data.model.Note
import com.android.kotlin.data.model.NoteResult

interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
}