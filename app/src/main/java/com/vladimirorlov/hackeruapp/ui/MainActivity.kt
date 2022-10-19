package com.vladimirorlov.hackeruapp.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.vladimirorlov.hackeruapp.ui.register.person.PersonFragmentDirections
import com.vladimirorlov.hackeruapp.ui.register.LoginActivity
import com.vladimirorlov.hackeruapp.R
import com.vladimirorlov.hackeruapp.data.Repository
import com.vladimirorlov.hackeruapp.databinding.ActivityMainBinding
import com.vladimirorlov.hackeruapp.model.person.Person
import com.vladimirorlov.hackeruapp.util.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialElevationScale
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainBinding

    private val currentNavigationFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
            ?.childFragmentManager
            ?.fragments
            ?.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.bottomNavBar)
        setBottomNavigationAndFab()
        setButtonClickListener()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setButtonClickListener() {

        val button = findViewById<FloatingActionButton>(R.id.fab)
        val input = findViewById<EditText>(R.id.item_name_input)
//        binding.fab.apply {
//            setShowMotionSpecResource(R.animator.fab_show)
//            setHideMotionSpecResource(R.animator.fab_hide)
//            setOnClickListener {
//                navigateToPersonFragment()
//                hideBottomAppBar()
//            }
//        }

        button.setOnClickListener {

            addPersonToList(input.text.toString())
            if (input.text.toString().isNotEmpty())
                input.setText("")
        }

    }

    private fun navigateToPersonFragment() {
        currentNavigationFragment?.apply {
            exitTransition = MaterialElevationScale(false).apply {
                duration = 300L
            }
            reenterTransition = MaterialElevationScale(true).apply {
                duration = 300L
            }
        }
        val directions = PersonFragmentDirections.actionGlobalPersonFragment()
        findNavController(R.id.fragmentContainerView).navigate(directions)
    }

    private fun hideBottomAppBar() {
        binding.run {
            bottomNavBar.performHide()

            bottomNavBar.animate().setListener(object : AnimatorListenerAdapter() {
                var isCanceled = false
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    if (isCanceled) return
                    bottomNavBar.visibility = View.GONE
                    fab.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(animation: Animator) {
                    super.onAnimationCancel(animation)
                    isCanceled = true
                }
            })
        }
    }

    private fun addPersonToList(input: String) {
        if (!isInputValid(input)) {
            Toast.makeText(this, "Please enter a valid Input", Toast.LENGTH_SHORT).show()
        } else {
            thread(start = true) {
                Repository.getInstance(this).addPerson(Person(input))
            }
            Snackbar.make(
                main_activity_layout,
                "$input has Successfully added!",
                Snackbar.LENGTH_SHORT
            ).show()
            NotificationsManager.displayNotification(this@MainActivity, Person(input))
        }
    }

    private fun setBottomNavigationAndFab() {
        binding.run {
            findNavController(R.id.fragmentContainerView).addOnDestinationChangedListener(
                this@MainActivity
            )
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.homeFragment -> {
                setBottomAppBarForHome()
            }
            R.id.personFragment -> setBottomAppBarForPerson()
        }
    }

    private fun setBottomAppBarForPerson() {
        hideBottomAppBar()
    }

    private fun setBottomAppBarForHome() {
        binding.run {
            fab.setImageState(intArrayOf(-android.R.attr.state_activated), true)
            bottomNavBar.visibility = View.VISIBLE
            bottomNavBar.performShow()
            fab.show()
        }
    }

    private fun isInputValid(input: String): Boolean {
        if (input.contains("^[a-zA-Z]*$".toRegex()) && input.isNotEmpty()) {
            return true
        }
        return false
    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_manu_nav, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_om -> {
                logoutUser()
                true
            }
            R.id.home_om -> {
                true
            }
            R.id.search_om -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}







