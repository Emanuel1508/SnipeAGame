package com.example.snipeagame.ui.main

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseActivity
import com.example.snipeagame.databinding.ActivityMainBinding
import com.example.snipeagame.utils.StringConstants
import com.example.snipeagame.utils.gone
import com.example.snipeagame.utils.hide
import com.example.snipeagame.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val TAG = this::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar.mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupNavController()
    }

    private fun setupNavController() {
        val navController =
            (supportFragmentManager.findFragmentById(R.id.mainFragmentContainerView)
                    as NavHostFragment).navController
        binding.mainBottomNavigation.setupWithNavController(navController)
        setupBottomNavBar(navController)
        setupListener(navController)
        navigationVisibility(navController)
    }

    private fun setupListener(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            setupToolbarVisibility(destination.id)
        }
    }

    private fun setupBottomNavBar(navController: NavController) {
        with(binding.mainBottomNavigation) {
            setOnItemSelectedListener { item ->
                setupToolbarTitle(item.itemId)
                when (item.itemId) {
                    R.id.myProfile_fragment -> navController.navigate(R.id.myProfile_fragment)
                    R.id.games_fragment -> navController.navigate(R.id.games_fragment)
                    R.id.achievements_fragment -> navController.navigate(R.id.achievements_fragment)
                    R.id.journal_fragment -> navController.navigate(R.id.journal_fragment)
                }
                true
            }
        }
    }

    private fun setupToolbarTitle(id: Int) {
        var title: String = StringConstants.EMPTY_STRING
        when (id) {
            R.id.myProfile_fragment -> title = getString(R.string.my_profile)
            R.id.games_fragment -> title = getString(R.string.games)
            R.id.achievements_fragment -> title = getString(R.string.achievements)
            R.id.journal_fragment -> title = getString(R.string.journal)
        }
        binding.toolbar.toolbarTitle.text = title
    }

    private fun setupToolbarVisibility(id: Int) {
        with(binding) {
            with(toolbar) {
                when (id) {
                    R.id.createGameFragment,
                    R.id.myGameDetailsFragment,
                    R.id.journalDetailsFragment -> mainToolbar.hide()

                    else -> mainToolbar.show()
                }
            }
        }
    }

    private fun navigationVisibility(navController: NavController) {
        with(binding) {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.createGameFragment,
                    R.id.myGameDetailsFragment,
                    R.id.journalDetailsFragment -> {
                        mainBottomNavigation.gone()
                    }

                    else -> {
                        mainBottomNavigation.show()
                    }
                }
            }
        }
    }
}
