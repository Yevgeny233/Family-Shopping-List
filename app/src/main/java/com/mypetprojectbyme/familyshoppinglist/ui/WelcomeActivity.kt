package com.mypetprojectbyme.familyshoppinglist.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mypetprojectbyme.familyshoppinglist.Utils
import com.mypetprojectbyme.familyshoppinglist.databinding.ActivityWelcomeBinding
import com.mypetprojectbyme.familyshoppinglist.ui.viewmodels.CurrentUserViewModel
import com.romainpiel.shimmer.Shimmer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {
    private var startIntent: Intent? = null
    private var welcomeBinding: ActivityWelcomeBinding? = null
    private var shimmer: Shimmer? = null
    private var authenticationIntent: Intent? = null
    private val currentUserViewModel: CurrentUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Utils.printAuthLog("done onCreate by WelcomeActivity")

        lifecycleScope.launch(Dispatchers.Main) {
            currentUserViewModel.observeCurrentUser().collect { userOfAppModel ->
                if (userOfAppModel != null) {
                    startIntent = Intent(this@WelcomeActivity, StartActivity::class.java)
                    startActivity(startIntent)
                    finish()
                } else {
                    welcomeBinding = ActivityWelcomeBinding.inflate(layoutInflater)
                    setContentView(welcomeBinding?.root)

                    shimmer = Shimmer()
                    shimmer?.start(welcomeBinding?.textWelcome)

                    welcomeBinding?.buttonNext?.setOnClickListener {
                        authenticationIntent =
                            Intent(this@WelcomeActivity, AuthActivity::class.java)
                        startActivity(authenticationIntent)
                        finish()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        welcomeBinding = null
        shimmer = null
        authenticationIntent = null
        startIntent = null
        Utils.printAuthLog("done onDestroy by WelcomeActivity")

    }
}

