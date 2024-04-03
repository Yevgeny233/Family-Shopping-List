package com.mypetprojectbyme.familyshoppinglist.data

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mypetprojectbyme.familyshoppinglist.Constants
import com.mypetprojectbyme.familyshoppinglist.domain.model.UserOfAppModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthPassword @Inject constructor(private val context: Context) {

    private var auth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null
    val isActivatedUser = MutableStateFlow(false)

    init {
        auth = Firebase.auth
        currentUser = auth?.currentUser

    }

    fun createAccount(activity: Activity, email: String, password: String) {
        auth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(Constants.TAG_MASSAGE_AUTH, "createUserWithEmail:success")
                    isActivatedUser.tryEmit(true)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(Constants.TAG_MASSAGE_AUTH, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    fun signInEmail(activity: Activity, email: String, password: String) {
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(Constants.TAG_MASSAGE_AUTH, "signInWithEmail:success")
                    isActivatedUser.tryEmit(true)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(Constants.TAG_MASSAGE_AUTH, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    fun getCurrentUser(): UserOfAppModel? {

        return currentUser?.let {
            val name = it.displayName
            val email = it.email
            val photoUrl = it.photoUrl

            val emailVerified = it.isEmailVerified

            val uid = it.uid
            UserOfAppModel(name, email, uid)
        }
    }

}

