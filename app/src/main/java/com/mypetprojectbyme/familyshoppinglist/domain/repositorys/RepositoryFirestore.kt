package com.mypetprojectbyme.familyshoppinglist.domain.repositorys

import com.mypetprojectbyme.familyshoppinglist.data.NoteFirestoreDataSource
import com.mypetprojectbyme.familyshoppinglist.domain.model.NoteModel
import com.mypetprojectbyme.familyshoppinglist.domain.usecases.FirestoreNoteUseCase
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RepositoryFirestore @Inject constructor(
    private val noteFirestoreDataSource: NoteFirestoreDataSource
) : FirestoreNoteUseCase {

    override fun addNote(noteModel: NoteModel) {
        noteFirestoreDataSource.addNote(noteModel)
    }

    override fun removeNote(noteModel: NoteModel, id: String) {
        noteFirestoreDataSource.removeNote(noteModel, id)
    }

    override fun updateNote(noteModel: NoteModel, id: String) {
        noteFirestoreDataSource.updateNote(noteModel, id)
    }

    override fun changeNoteStatus(id: String, newState: Boolean) {
        noteFirestoreDataSource.changeNoteStatus(id, newState)
    }

    override fun getNoteListBySnapShot(userEmail: String?) =
        noteFirestoreDataSource.getNotesFlowBySnapShot(userEmail)

}