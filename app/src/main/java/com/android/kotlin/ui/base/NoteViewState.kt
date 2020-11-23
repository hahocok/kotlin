package com.android.kotlin.ui.base

import com.android.kotlin.data.model.Note

class NoteViewState(data: Data = Data(),
                    error: Throwable? = null) : BaseViewState<NoteViewState.Data>(data, error) {

    data class Data(val isDeleted: Boolean = false, val note: Note? = null)
}