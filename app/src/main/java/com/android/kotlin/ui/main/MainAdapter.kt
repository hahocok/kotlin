package com.android.kotlin.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.kotlin.R
import com.android.kotlin.common.getColorInt
import com.android.kotlin.data.model.Color
import com.android.kotlin.data.model.Note
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note.*

class MainAdapter(private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<MainAdapter.NoteViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NoteViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
    )

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) = holder.bind(notes[position])

    inner class NoteViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(note: Note) {
            title.text = note.title
            body.text = note.note

            itemView.setBackgroundColor(itemView.context.resources.getColor(note.color.getColorInt(containerView.context), itemView.context.theme))
            itemView.setOnClickListener { onItemClickListener.onItemClick(note) }

        }
    }
}
