package com.mypetprojectbyme.familyshoppinglist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.mypetprojectbyme.familyshoppinglist.Constants
import com.mypetprojectbyme.familyshoppinglist.R
import com.mypetprojectbyme.familyshoppinglist.Utils
import com.mypetprojectbyme.familyshoppinglist.databinding.FragmentCreateNoteBinding
import com.mypetprojectbyme.familyshoppinglist.domain.adapters.RecyclerListPurchaseAdapter
import com.mypetprojectbyme.familyshoppinglist.domain.model.NoteModel
import com.mypetprojectbyme.familyshoppinglist.domain.model.PurchaseModel
import com.mypetprojectbyme.familyshoppinglist.ui.viewmodels.CurrentUserViewModel
import com.mypetprojectbyme.familyshoppinglist.ui.viewmodels.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint

class CreateNoteFragment : Fragment() {

    private var createNoteBinding: FragmentCreateNoteBinding? = null
    private val noteViewModel: NoteViewModel by activityViewModels()
    private var purchaseAdapter: RecyclerListPurchaseAdapter? = null
    private var inputNameLength = 0
    private val currentUserViewModel by activityViewModels<CurrentUserViewModel>()
    private var userEmailArray: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        createNoteBinding = FragmentCreateNoteBinding.inflate(inflater, container, false)

        purchaseAdapter = RecyclerListPurchaseAdapter()

        createNoteBinding?.recyclerList?.adapter = purchaseAdapter

        createNoteBinding?.nameEdit?.doOnTextChanged { text, _, _, _ ->
            inputNameLength = text?.length ?: 0
            checkSaveButtonState()
            text?.length?.let { checkInputLayoutState(it, createNoteBinding?.nameLayout) }
        }

        createNoteBinding?.addItemButton?.setOnClickListener {
            purchaseAdapter?.addPurchaseItem(PurchaseModel(""))
        }

        lifecycleScope.launch(Dispatchers.Main) {
            currentUserViewModel.observeCurrentUser().collect { userOfAppModel ->
                userOfAppModel?.email?.let { email -> userEmailArray.add(email) }
            }
        }
        return createNoteBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments?.getStringArray(Constants.FAMILY_KEY) != null) {
            val array = arguments?.getStringArray(Constants.FAMILY_KEY)
            array?.forEach { user ->
                userEmailArray.add(user)
                Utils.createNoteLog("family members = $user")
            }
        }
        createNoteBinding?.saveButton?.setOnClickListener {
            val name: String = createNoteBinding?.nameEdit?.text.toString()
            val purchaseArrayList: ArrayList<PurchaseModel>? = purchaseAdapter?.getArrayList()
            val isChecked = false

            val noteModel =
                NoteModel(name, purchaseArrayList, isChecked, userEmailArray)
            noteViewModel.saveNote(noteModel)
            findNavController().popBackStack()
        }
    }

    private fun checkSaveButtonState() {
        createNoteBinding?.saveButton?.isEnabled = inputNameLength != 0
    }

    private fun checkInputLayoutState(textLength: Int, inputLayout: TextInputLayout?) {
        if (textLength == 0) {
            inputLayout?.isHelperTextEnabled = true
            inputLayout?.helperText = getText(R.string.there_is_enter_nothing_text)
        } else {
            inputLayout?.isHelperTextEnabled = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        createNoteBinding = null
        purchaseAdapter = null
    }

}