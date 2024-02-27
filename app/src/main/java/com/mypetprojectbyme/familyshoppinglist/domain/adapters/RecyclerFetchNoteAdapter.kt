package com.mypetprojectbyme.familyshoppinglist.domain.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.mypetprojectbyme.familyshoppinglist.R
import com.mypetprojectbyme.familyshoppinglist.databinding.NoteItemBinding
import com.mypetprojectbyme.familyshoppinglist.domain.model.FetchNoteModel

class RecyclerFetchNoteAdapter :
    RecyclerView.Adapter<RecyclerFetchNoteAdapter.NoteViewHolder>() {

    var clickCheckBoxListener: ((FetchNoteModel) -> Unit)? = null
    var clickItemListener: ((FetchNoteModel) -> Unit)? = null
    private var fetchNoteList: ArrayList<FetchNoteModel> = ArrayList()

    inner class NoteViewHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fetchNoteModel: FetchNoteModel) {
            binding.nameOfNote.text = fetchNoteModel.noteModel.nameNote
            binding.checkboxNote.isChecked = fetchNoteModel.noteModel.checked

            if (fetchNoteModel.noteModel.checked) {
                binding.nameOfNote.paintFlags =
                    binding.checkboxNote.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.nameOfNote.setTextColor(binding.root.resources.getColor(R.color.grey))
            } else {
                binding.nameOfNote.paintFlags =
                    binding.nameOfNote.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.nameOfNote.setTextColor(binding.root.resources.getColor(R.color.black))

            }
        }

        init {
            itemView.setOnClickListener {
                clickItemListener?.invoke(fetchNoteList[adapterPosition])
            }
            binding.checkboxNote.setOnClickListener {
                clickCheckBoxListener?.invoke(fetchNoteList[adapterPosition])
                fetchNoteList[adapterPosition].noteModel.checked = (it as CheckBox).isChecked
                fetchNoteList[adapterPosition].noteModel.checked = it.isChecked
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return fetchNoteList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val noteItem = fetchNoteList[position]
        holder.bind(noteItem)
    }

    fun updateList(newList: ArrayList<FetchNoteModel>) {
        fetchNoteList = newList
        notifyDataSetChanged()
    }

    fun getNoteList(): ArrayList<FetchNoteModel> {
        return fetchNoteList
    }
}

