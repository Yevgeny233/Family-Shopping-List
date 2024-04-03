package com.mypetprojectbyme.familyshoppinglist.domain.usecases

import com.google.firebase.firestore.QuerySnapshot
import com.mypetprojectbyme.familyshoppinglist.domain.model.NoteModel
import kotlinx.coroutines.flow.Flow

interface FirestoreNoteUseCase {

    fun addNote(noteModel: NoteModel)
    fun removeNote(noteModel: NoteModel, id: String)
    fun updateNote(noteModel: NoteModel, id: String)
    fun changeNoteStatus(id: String, newState: Boolean)
    fun getNoteListSnapShot(userEmail: String?): Flow<QuerySnapshot>?
}