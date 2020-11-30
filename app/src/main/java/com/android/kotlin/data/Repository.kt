package com.android.kotlin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.kotlin.data.model.Color
import com.android.kotlin.data.model.Note
import java.util.*

object Repository {

    private val notesLiveData = MutableLiveData<List<Note>>()

    private val notes: MutableList<Note> = mutableListOf(
        Note(id = UUID.randomUUID().toString(),
            title = "Моя первая заметка",
            note = "Kotlin очень краткий, но при этом выразительный язык",
            color = Color.WHITE),
        Note(id = UUID.randomUUID().toString(),
            title = "Моя первая заметка",
            note = "Kotlin очень краткий, но при этом выразительный язык",
            color = Color.BLUE),
        Note(id = UUID.randomUUID().toString(),
            title = "Моя первая заметка",
            note = "Kotlin очень краткий, но при этом выразительный язык",
            color = Color.GREEN),
        Note(id = UUID.randomUUID().toString(),
            title = "Моя первая заметка",
            note = "Kotlin очень краткий, но при этом выразительный язык",
            color = Color.PINK),
        Note(id = UUID.randomUUID().toString(),
            title = "Моя первая заметка",
            note = "Kotlin очень краткий, но при этом выразительный язык",
            color = Color.RED),
        Note(id = UUID.randomUUID().toString(),
            title = "Моя первая заметка",
            note = "Kotlin очень краткий, но при этом выразительный язык",
            color = Color.YELLOW),
        Note(id = UUID.randomUUID().toString(),
            title = "Моя первая заметка",
            note = "Kotlin очень краткий, но при этом выразительный язык",
            color = Color.VIOLET)

    )

    init {
        notesLiveData.value = notes
    }

    fun saveNote(note: Note) {
        addOrReplace(note)
        notesLiveData.value = notes
    }

    fun getNotes(): LiveData<List<Note>> {
        return notesLiveData
    }

    private fun addOrReplace(note: Note) {
        for (i in notes.indices) {
            if (notes[i] == note) {
                notes[i] = note
                return
            }
        }

        notes.add(note)
    }
}