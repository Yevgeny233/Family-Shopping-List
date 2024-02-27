package com.mypetprojectbyme.familyshoppinglist.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.mypetprojectbyme.familyshoppinglist.R
import com.mypetprojectbyme.familyshoppinglist.Utils
import com.mypetprojectbyme.familyshoppinglist.databinding.FragmentNoteBinding
import com.mypetprojectbyme.familyshoppinglist.domain.adapters.RecyclerFetchNoteAdapter
import com.mypetprojectbyme.familyshoppinglist.domain.model.FetchNoteModel
import com.mypetprojectbyme.familyshoppinglist.domain.model.NoteModel
import com.mypetprojectbyme.familyshoppinglist.ui.viewmodels.CurrentUserViewModel
import com.mypetprojectbyme.familyshoppinglist.ui.viewmodels.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var noteBinding: FragmentNoteBinding? = null
    private val noteViewModel: NoteViewModel by activityViewModels()
    private var userEmail: String? = null
    private val currentUserViewModel: CurrentUserViewModel by activityViewModels()
    private var recyclerFetchNoteAdapter: RecyclerFetchNoteAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        noteBinding = FragmentNoteBinding.inflate(inflater, container, false)

        return noteBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment =
            this.activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        noteBinding?.floatingActionAddButton?.setOnClickListener {
            navController.navigate(R.id.action_noteFragment_to_createNoteFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        recyclerFetchNoteAdapter = RecyclerFetchNoteAdapter()
        noteBinding?.noteRecView?.adapter = recyclerFetchNoteAdapter

        lifecycleScope.launch(Dispatchers.Main) {
            currentUserViewModel.observeCurrentUser().collect { userOfAppModel ->
                noteViewModel.fetchNoteListBySnapshot(userOfAppModel?.email)?.collect { snapshot ->
                    val arrayListResult = ArrayList<FetchNoteModel>()
                    for (s in snapshot) {
                        arrayListResult.add(FetchNoteModel(s.toObject(NoteModel::class.java), s.id))
                    }
                    recyclerFetchNoteAdapter?.updateList(arrayListResult)

                }
            }
        }
        recyclerFetchNoteAdapter?.clickItemListener = { fetchNoteModel ->
            noteViewModel.updateClickedNoteModel(fetchNoteModel)
            findNavController().navigate(
                R.id.action_noteFragment_to_editNoteFragment
            )
            Utils.printNoteViewModelLog(fetchNoteModel.toString())
        }

        recyclerFetchNoteAdapter?.clickCheckBoxListener = { fetchNoteModel ->
            noteViewModel.changeStatusNote(
                fetchNoteModel.idOfNote,
                !fetchNoteModel.noteModel.checked
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        noteBinding = null
        recyclerFetchNoteAdapter = null
        userEmail = null
        Log.i("TAG_LIFESICLE_NoteFragment_", "onDestroy")
    }
}
