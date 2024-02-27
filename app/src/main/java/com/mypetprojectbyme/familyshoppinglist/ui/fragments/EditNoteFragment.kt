package com.mypetprojectbyme.familyshoppinglist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.mypetprojectbyme.familyshoppinglist.R
import com.mypetprojectbyme.familyshoppinglist.databinding.FragmentEditNoteBinding
import com.mypetprojectbyme.familyshoppinglist.domain.adapters.RecyclerListPurchaseAdapter
import com.mypetprojectbyme.familyshoppinglist.domain.model.FetchNoteModel
import com.mypetprojectbyme.familyshoppinglist.domain.model.PurchaseModel
import com.mypetprojectbyme.familyshoppinglist.ui.viewmodels.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class EditNoteFragment : Fragment() {

    private var editNoteBinding: FragmentEditNoteBinding? = null
    private val noteViewModel: NoteViewModel by activityViewModels()
    private var currentFetchNote: FetchNoteModel? = null
    private var recyclerListPurchaseAdapter: RecyclerListPurchaseAdapter? = null
    private var inputNameLength = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        editNoteBinding = FragmentEditNoteBinding.inflate(inflater, container, false)

        recyclerListPurchaseAdapter = RecyclerListPurchaseAdapter()
        editNoteBinding?.recyclerList?.adapter = recyclerListPurchaseAdapter

        return editNoteBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editNoteBinding?.nameEdit?.doOnTextChanged { text, start, before, count ->
            inputNameLength = text?.length ?: 0
            checkEditButtonState()
            text?.length?.let { checkInputLayoutState(it, editNoteBinding?.nameLayout) }
            noteViewModel.clickedNoteLiveData.value?.noteModel?.nameNote = text.toString()
        }

        editNoteBinding?.editButton?.setOnClickListener {

            currentFetchNote?.let { fetchNoteModel ->
                noteViewModel.updateNote(fetchNoteModel.noteModel, fetchNoteModel.idOfNote)
                moveToBack()
            }
        }

        editNoteBinding?.removeButton?.setOnClickListener {
            currentFetchNote?.let { fetchNoteModel ->
                noteViewModel.removeNote(fetchNoteModel.noteModel, fetchNoteModel.idOfNote)
                moveToBack()
            }
        }
        editNoteBinding?.addItemButton?.setOnClickListener {
            recyclerListPurchaseAdapter?.addPurchaseItem(PurchaseModel())
        }
    }

    override fun onStart() {
        super.onStart()
        noteViewModel.clickedNoteLiveData.observe(this) { clickedNote ->
            editNoteBinding?.nameEdit?.setText(clickedNote.noteModel.nameNote)
            clickedNote.noteModel.arrayPurchase?.let { recyclerListPurchaseAdapter?.updatePurchaseList(it) }
            currentFetchNote = clickedNote
        }
    }

    private fun checkEditButtonState() {
        editNoteBinding?.editButton?.isEnabled = inputNameLength != 0
    }

    private fun checkInputLayoutState(textLength: Int, inputLayout: TextInputLayout?) {
        if (textLength == 0) {
            inputLayout?.isHelperTextEnabled = true
            inputLayout?.helperText = getText(R.string.there_is_enter_nothing_text)
        } else {
            inputLayout?.isHelperTextEnabled = false
        }
    }

    private fun moveToBack() {
        findNavController().popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerListPurchaseAdapter = null
        editNoteBinding = null
    }
}