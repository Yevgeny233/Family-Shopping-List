package com.mypetprojectbyme.familyshoppinglist.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.snapshots
import com.mypetprojectbyme.familyshoppinglist.Utils
import com.mypetprojectbyme.familyshoppinglist.domain.model.NoteModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class NoteFirestoreDataSource @Inject constructor() {

    private val db = FirebaseFirestore.getInstance()

    fun addNote(noteModel: NoteModel) {

        db.collection("notes").document().set(noteModel).addOnSuccessListener {
            Utils.fireStoreLog("DocumentSnapshot added successful")

        }.addOnFailureListener { e ->
            Utils.fireStoreLog("addNote OnFailureListener = " + e.message.toString())
        }
    }

    fun getNotesFlowSnapShot(userEmail: String?): Flow<QuerySnapshot>? {
        return  userEmail?.let { email ->
            db.collection("notes")
                .whereArrayContains("arrayUserEmail", email)
        }?.snapshots()?.flowOn(Dispatchers.IO)
    }

    fun getSnapShotChanges(userEmail: String?): Query? {
        val ref = userEmail?.let { email ->
            db.collection("notes")
                .whereArrayContains("arrayUserEmail", email)
        }
        return ref
    }

    fun removeNote(noteModel: NoteModel, id: String) {
        db.collection("notes").document(id).delete().addOnSuccessListener {
            Utils.fireStoreLog("DocumentSnapshot by id => $noteModel successfully deleted!")
        }.addOnFailureListener { e -> Utils.fireStoreLog("Error deleting document -> $e") }
    }

    fun updateNote(newNoteModel: NoteModel, id: String) {
        val docRef = db.collection("notes").document(id)

        docRef.set(newNoteModel).addOnCompleteListener {
            Utils.fireStoreLog("update done ${it.isSuccessful}")
        }.addOnFailureListener {
            Utils.fireStoreLog("update have not done -> ${it.message}")

        }
    }

    fun changeNoteStatus(id: String, newState: Boolean) {
        val docRef = db.collection("notes").document(id)

        val updateMap = hashMapOf<String, Any>("checked" to newState)
        docRef.update(updateMap).addOnCompleteListener {
            Utils.fireStoreLog("changeNoteStatus done ${it.isSuccessful}")
        }.addOnFailureListener {
            Utils.fireStoreLog("changeNoteStatus have not done -> ${it.message}")
        }
    }
}
