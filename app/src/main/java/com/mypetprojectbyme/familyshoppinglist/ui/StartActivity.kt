package com.mypetprojectbyme.familyshoppinglist.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.mypetprojectbyme.familyshoppinglist.Constants.FAMILY_KEY
import com.mypetprojectbyme.familyshoppinglist.R
import com.mypetprojectbyme.familyshoppinglist.Utils
import com.mypetprojectbyme.familyshoppinglist.databinding.ActivityStartBinding
import com.mypetprojectbyme.familyshoppinglist.databinding.AlertDialogEmailBinding
import com.mypetprojectbyme.familyshoppinglist.domain.adapters.AlertAdapter
import com.mypetprojectbyme.familyshoppinglist.ui.viewmodels.CurrentUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint

class StartActivity : AppCompatActivity() {

    private var startBinding: ActivityStartBinding? = null
    private val currentUserViewModel: CurrentUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startBinding = ActivityStartBinding.inflate(layoutInflater)

        setContentView(startBinding?.root)
        this.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        startBinding?.logOut?.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.createNoteFragment -> {
                    startBinding?.drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }

                R.id.editNoteFragment -> {
                    startBinding?.drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }

                R.id.noteFragment -> {
                    startBinding?.drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }
        }

        val appBarConfiguration =
            AppBarConfiguration(navController.graph, startBinding?.drawerLayout)

        startBinding?.toolBar?.setupWithNavController(navController, appBarConfiguration)

        startBinding?.navView?.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.create_item -> {
                    navController.navigate(R.id.action_noteFragment_to_createNoteFragment)
                    startBinding?.drawerLayout?.close()
                    true
                }

                R.id.create_item_family -> {
                    showAlertDialog()
                    true
                }

                else -> {
                    startBinding?.drawerLayout?.close()
                    true
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            currentUserViewModel.observeCurrentUser().collect {

                val navigationView = startBinding?.navView
                val header = navigationView?.getHeaderView(0)
                val headerViewTextName = header?.findViewById<TextView>(R.id.user_name_view)
                val headerViewTextEmail = header?.findViewById<TextView>(R.id.user_email_view)
                headerViewTextName?.text = it?.name
                headerViewTextEmail?.text = it?.email

                Utils.printUserLog("StartActivity User name = ${it?.name} : user email = ${it?.email}")
            }
        }
    }

    private fun showAlertDialog() {
        val builder = MaterialAlertDialogBuilder(this)
        val alertDialogEmailBinding = AlertDialogEmailBinding.inflate(layoutInflater)
        val adapter = AlertAdapter()
        val recyclerView: RecyclerView = alertDialogEmailBinding.recyclerListUserEmail
        recyclerView.adapter = adapter
        val addFamilyMemberButton = alertDialogEmailBinding.actionAddUserEmailButton
        builder.setView(alertDialogEmailBinding.root)

        addFamilyMemberButton.setOnClickListener {
            val editTextUserEmail: String = alertDialogEmailBinding.inputEmailField.text.toString()
            adapter.addUserEmail(editTextUserEmail)
            Log.i("TAG_showAlertDialog_", "addFamilyMemberButton is clicked")
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i ->
            dialogInterface.cancel()
        }
        builder.setPositiveButton(getString(R.string.add_alert_dialog)) { dialogInterface, i ->
            val familyMembers = adapter.getFamilyMembers()
            val bundle = bundleOf(FAMILY_KEY to familyMembers.toTypedArray())

            startBinding?.navHostFragment?.findNavController()
                ?.navigate(R.id.action_noteFragment_to_createNoteFragment, bundle)
            dialogInterface.dismiss()
        }
        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        startBinding = null
    }
}

