package com.android.kotlin.ui.main

import com.android.kotlin.data.model.Note
import com.android.kotlin.ui.base.BaseViewState

class MainViewState(notes: List<Note>? = null, error: Throwable? = null)
    : BaseViewState<List<Note>?>(notes, error)