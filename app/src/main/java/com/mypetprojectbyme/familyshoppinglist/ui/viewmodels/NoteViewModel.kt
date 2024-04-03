package com.mypetprojectbyme.familyshoppinglist.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mypetprojectbyme.familyshoppinglist.domain.model.FetchNoteModel
import com.mypetprojectbyme.familyshoppinglist.domain.model.NoteModel
import com.mypetprojectbyme.familyshoppinglist.domain.repositoryes.RepositoryFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import javax.inject.Inject


@HiltViewModel
class NoteViewModel @Inject constructor(private val repositoryFirestore: RepositoryFirestore) :
    ViewModel() {

    private val _clickedNoteLiveData: MutableLiveData<FetchNoteModel> =
        MutableLiveData<FetchNoteModel>()
    val clickedNoteLiveData: LiveData<FetchNoteModel> = _clickedNoteLiveData

    fun updateClickedNoteModel(fetchNoteModel: FetchNoteModel) {
        _clickedNoteLiveData.value = fetchNoteModel
    }

    fun saveNote(noteModel: NoteModel) {
        repositoryFirestore.addNote(noteModel)
    }

    fun removeNote(noteModel: NoteModel, id: String) {
        repositoryFirestore.removeNote(noteModel, id)
    }

    fun updateNote(noteModel: NoteModel, id: String) {
        repositoryFirestore.updateNote(noteModel, id)
    }

    fun changeStatusNote(id: String, newState: Boolean) {
        repositoryFirestore.changeNoteStatus(id, newState)
    }

    fun fetchNoteListSnapshot(userEmail: String?) =
        repositoryFirestore.getNoteListSnapShot(userEmail)


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}