package com.android.kotlin.ui.base

import com.android.kotlin.data.model.Note

class NoteViewState(noteData: NoteData = NoteData(),
                    error: Throwable? = null) : BaseViewState<NoteViewState.NoteData>(noteData, error) {

    data class NoteData(val isDeleted: Boolean = false, val note: Note? = null)
}