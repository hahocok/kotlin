package com.android.kotlin.ui.base

import com.android.kotlin.data.model.Note

class NoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)