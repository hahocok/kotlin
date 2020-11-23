package com.android.kotlin.ui.note

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import com.android.kotlin.R
import com.android.kotlin.common.getColorInt
import com.android.kotlin.data.model.Color
import com.android.kotlin.data.model.Note
import com.android.kotlin.ui.base.BaseActivity
import com.android.kotlin.ui.base.NoteViewState
import java.text.SimpleDateFormat
import java.util.*

private const val SAVE_DELAY = 2000L

class NoteActivity : BaseActivity<NoteViewState.NoteData, NoteViewState>() {

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"


        fun start(context: Context, noteId: String? = null) {
            context.startActivity<NoteActivity>(EXTRA_NOTE to noteId)
        }
    }


    override val model: NoteViewModel by viewModel()
    override val layoutRes: Int = R.layout.activity_note
    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noteId?.let {
            model.loadNote(it)
        }

        if (noteId == null ) supportActionBar?.title = getString(R.string.new_note_title)

        titleEt.addTextChangedListener(textChangeListener)
        bodyEt.addTextChangedListener(textChangeListener)

        colorPicker.onColorClickListener = {
            note?.color = it
            setToolbarColor(it)
            triggerSaveNote()
        }
    }

    private fun togglePalette() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    private fun setToolbarColor(color: Color) {
        toolbar.setBackgroundColor(color.getColorInt(this))
    }

    override fun renderData(noteData: NoteViewState.NoteData) {
        if (noteData.isDeleted) finish()
        this.note = noteData.note
        initView()
    }

    private fun initView() {
        note?.run {
            removeEditListener()
            titleEt.setText(title)
            bodyEt.setText(note)
            toolbar.setBackgroundColor(color.getColorInt(this@NoteActivity))
            supportActionBar?.title = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(lastChanged)
        } ?: let {
            supportActionBar?.title =   getString(R.string.new_note_title)
        }

        setEditListener()
    }

    private fun createNewNote(): Note = Note(UUID.randomUUID().toString(),
        titleEt.text.toString(),
        bodyEt.text.toString())


    private val textChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            triggerSaveNote()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // not used
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // not used
        }
    }

    private fun triggerSaveNote() {
        if (titleEt.text?.length!! < 3 && bodyEt.text.length < 3) return

        Handler().postDelayed({
            note = note?.copy(title = titleEt.text.toString(),
                note = bodyEt.text.toString(),
                color = note!!.color,
                lastChanged = Date())
                ?: createNewNote()

            note?.let { model.saveChanges(it) }
        }, SAVE_DELAY)
    }

    private fun setEditListener() {
        titleEt.addTextChangedListener(textChangeListener)
        bodyEt.addTextChangedListener(textChangeListener)
    }

    private fun removeEditListener() {
        titleEt.removeTextChangedListener(textChangeListener)
        bodyEt.removeTextChangedListener(textChangeListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        android.R.id.home -> super.onBackPressed().let { true }
        R.id.palette -> togglePalette().let { true }
        R.id.delete -> deleteNote().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
        menuInflater.inflate(R.menu.note_menu, menu).let { true }

    private fun deleteNote() {
        alert {
            messageResource = R.string.note_delete_message
            negativeButton(R.string.note_delete_cancel) { dialog ->  dialog.dismiss() }
            positiveButton(R.string.note_delete_ok) { model.deleteNote() }
        }.show()
    }

    override fun onBackPressed() {
        if (colorPicker.isOpen) {
            colorPicker.close()
            return
        }
        super.onBackPressed()
    }
}
