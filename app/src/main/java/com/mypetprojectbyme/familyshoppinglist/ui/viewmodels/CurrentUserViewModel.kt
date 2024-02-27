package com.mypetprojectbyme.familyshoppinglist.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mypetprojectbyme.familyshoppinglist.domain.model.UserOfAppModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class CurrentUserViewModel : ViewModel() {

    private var auth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null

    fun observeCurrentUser() = flow {
        updateCurrUser()
        val user = getCurrentUser()
        emit(user)
    }.flowOn(Dispatchers.IO)

    private fun updateCurrUser() {
        auth = FirebaseAuth.getInstance()
        currentUser = auth?.currentUser
    }

    fun getCurrentUser(): UserOfAppModel? {
        updateCurrUser()
        return currentUser?.let {
            // Name, email address, and profile photo Url
            val name = it.displayName
            val email = it.email
            val photoUrl = it.photoUrl

            val emailVerified = it.isEmailVerified

            val uid = it.uid
            Log.i("TAG_USER_ID_", uid)

            UserOfAppModel(name, email, photoUrl, uid)
        }

    }
}