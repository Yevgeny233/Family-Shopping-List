package com.mypetprojectbyme.familyshoppinglist.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.Auth
import com.mypetprojectbyme.familyshoppinglist.Constants
import com.mypetprojectbyme.familyshoppinglist.Utils
import com.mypetprojectbyme.familyshoppinglist.data.FireBaseAnalytic
import com.mypetprojectbyme.familyshoppinglist.data.FirebaseAuthGoogle
import com.mypetprojectbyme.familyshoppinglist.data.FirebaseAuthPassword
import com.mypetprojectbyme.familyshoppinglist.databinding.ActivityAuthBinding
import com.mypetprojectbyme.familyshoppinglist.domain.network.InternetConnectionStatus
import com.mypetprojectbyme.familyshoppinglist.ui.viewmodels.CurrentUserViewModel
import com.mypetprojectbyme.familyshoppinglist.ui.viewmodels.NetworkConnectionViewModelState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    @Inject
    lateinit var firebaseAuthGoogle: FirebaseAuthGoogle

    @Inject
    lateinit var firebaseAuthPassword: FirebaseAuthPassword

    @Inject
    lateinit var fireBaseAnalytic: FireBaseAnalytic

    private var authBinding: ActivityAuthBinding? = null

    private val networkConnectionViewModelState: NetworkConnectionViewModelState by viewModels()
    private val currentUserViewModel: CurrentUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authBinding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(authBinding?.root)

        fireBaseAnalytic
        observeAuthState()

        authBinding?.loginBt?.setOnClickListener {
            if (authBinding?.password?.text.toString().length < 6) {
                Utils.makeToast(this, "password should be more 6 values")
            } else {
                if (firebaseAuthPassword.getCurrentUser() != null) {
                    firebaseAuthPassword.signInEmail(
                        this,
                        authBinding?.username?.text.toString(),
                        authBinding?.password?.text.toString()
                    )
                } else {
                    firebaseAuthPassword.createAccount(
                        this,
                        authBinding?.username?.text.toString(),
                        authBinding?.password?.text.toString()
                    )
                }
                observeUserStateAuthPassword()
            }
        }

        authBinding?.signinGoogleImageBt?.setOnClickListener {
            firebaseAuthGoogle.signInGoogle(this)
            Utils.printAuthLog("signinGoogleImageBt is clicked")
            observeUserStateAuthGoogle()
        }

    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch(Dispatchers.IO) {

            networkConnectionViewModelState.connectionStatus?.collect {
                when (it) {
                    InternetConnectionStatus.AVAILABLE -> {
                        withContext(Dispatchers.Main) {
                            Utils.printAuthLog("Network is online")
                            unlockViews()
                        }
                    }

                    InternetConnectionStatus.UNAVAILABLE -> {
                        withContext(Dispatchers.Main) {
                            Utils.printAuthLog("Network is offline")
                            lockViews()
                        }
                    }

                    InternetConnectionStatus.LOST -> {
                        withContext(Dispatchers.Main) {
                            Utils.printAuthLog("Network is lost")
                            lockViews()
                        }
                    }

                    InternetConnectionStatus.LOSING -> {
                        withContext(Dispatchers.Main) {
                            Utils.printAuthLog("Network is losing")
                            lockViews()
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result != null) {
                if (result.isSuccess) {
                    val account = result.signInAccount
                    firebaseAuthGoogle.firebaseAuthWithGoogle(this, account)
                } else {
                    Utils.printAuthLog("Google Sign In failed. by ${result.status.statusMessage}")
                    Utils.makeToast(this, "Google Sign In failed.")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        authBinding = null
        lifecycleScope.cancel()
    }

    private fun lockViews() {
        authBinding?.loginBt?.isEnabled = false
        authBinding?.signinGoogleImageBt?.isEnabled = false
        authBinding?.signinGoogleImageBt?.setImageResource(com.google.android.gms.auth.api.R.drawable.common_google_signin_btn_text_disabled)
    }

    private fun unlockViews() {
        authBinding?.loginBt?.isEnabled = true
        authBinding?.signinGoogleImageBt?.isEnabled = true
        authBinding?.signinGoogleImageBt?.setImageResource(com.google.android.gms.auth.api.R.drawable.common_google_signin_btn_icon_light_normal)
    }

    private fun observeAuthState() {

        lifecycleScope.launch(Dispatchers.Main) {

            currentUserViewModel.observeCurrentUser()
                .collect { userOfAppModel ->
                    if (userOfAppModel != null) {
                        Utils.printUserLog(
                            "AuthActivity User name = ${userOfAppModel.name} : " +
                                    "user email = ${userOfAppModel.email}"
                        )
                        moveToStartActivity()
                    }
                }
        }
    }

    private fun observeUserStateAuthGoogle() {
        lifecycleScope.launch(Dispatchers.Main) {
            firebaseAuthGoogle.isActivatedUser.collect { state ->
                if (state) {
                    observeAuthState()
                }
            }
        }
    }

    private fun observeUserStateAuthPassword() {
        lifecycleScope.launch(Dispatchers.Main) {
            firebaseAuthPassword.isActivatedUser.collect { state ->
                if (state) {
                    observeAuthState()
                }
            }
        }
    }

    private fun moveToStartActivity() {
        val startActivityIntent = Intent(this, StartActivity::class.java)
        startActivity(startActivityIntent)
        finish()
    }
}

