package com.mypetprojectbyme.familyshoppinglist.data

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mypetprojectbyme.familyshoppinglist.Constants.RC_SIGN_IN
import com.mypetprojectbyme.familyshoppinglist.R
import com.mypetprojectbyme.familyshoppinglist.Utils
import com.mypetprojectbyme.familyshoppinglist.domain.model.UserOfAppModel
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@ActivityScoped
class FirebaseAuthGoogle @Inject constructor(
    private val context: Context,
    fragmentActivity: FragmentActivity
) {
    val isActivatedUser = MutableStateFlow(false)

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.my_web_client_id)).requestEmail().build()
    }

    private var googleApiClient: GoogleApiClient? = null

    private var auth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null

    init {
        googleApiClient = GoogleApiClient.Builder(context)
            .enableAutoManage(fragmentActivity) { connectionResult ->
                if (connectionResult.isSuccess) {
                    Utils.printAuthLog("Connection isSuccess = ${connectionResult.isSuccess}")
                } else {
                    Utils.printAuthLog("Connection failed: ${connectionResult.errorMessage}")
                }

            }.addApi(Auth.GOOGLE_SIGN_IN_API, gso).build()
        auth = Firebase.auth
        currentUser = auth?.currentUser

    }

    fun signInGoogle(activity: Activity) {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun firebaseAuthWithGoogle(activity: Activity, account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth?.signInWithCredential(credential)?.addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                // Sign in success
                Utils.printAuthLog("signInWithCredential:success")
                currentUser = auth?.currentUser
                Utils.makeToast(context, "Authentication successful.")
                isActivatedUser.tryEmit(true)

            } else {
                // If sign in fails, display a message to the user.
                Utils.printAuthLog("signInWithCredential:failure ${task.exception}")
                Utils.makeToast(context, "Authentication failed.")
            }
        }
    }

    fun getCurrentUser(): UserOfAppModel? {
        return currentUser?.let {
            // Name, email address, and profile photo Url
            val name = it.displayName
            val email = it.email
            val photoUrl = it.photoUrl

            val emailVerified = it.isEmailVerified

            val uid = it.uid
            UserOfAppModel(name, email, uid)
        }
    }
}
