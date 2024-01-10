package com.example.snipeagame.ui.main

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseActivity
import com.example.snipeagame.databinding.ActivityMainBinding
import com.example.snipeagame.ui.main.games.create_game.CreateGameFragmentDirections
import com.example.snipeagame.utils.StringConstants
import com.example.snipeagame.utils.gone
import com.example.snipeagame.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavController()
        setupToolbar()
    }

    private fun setupNavController() {
        val navController =
            (supportFragmentManager.findFragmentById(R.id.mainFragmentContainerView)
                    as NavHostFragment).navController
        binding.mainBottomNavigation.setupWithNavController(navController)
        setupBottomNavBar(navController)
        navigationVisibility(navController)
    }

    private fun setupBottomNavBar(navController: NavController) {
        with(binding.mainBottomNavigation) {
            setOnItemSelectedListener { item ->
                setupToolbarTitle(item.itemId)
                when (item.itemId) {
                    R.id.myProfile_fragment -> navController.navigate(R.id.myProfile_fragment)
                    R.id.games_fragment -> navController.navigate(R.id.games_fragment)
                    R.id.achievements_fragment -> navController.navigate(R.id.achievements_fragment)
                }
                true
            }
        }
    }

    private fun setupToolbar() = setSupportActionBar(binding.mainToolbar)

    private fun setupToolbarTitle(id: Int) {
        var title: String = StringConstants.EMPTY_STRING
        when (id) {
            R.id.myProfile_fragment -> title = getString(R.string.my_profile)
            R.id.games_fragment -> title = getString(R.string.games)
            R.id.achievements_fragment -> title = getString(R.string.achievements)
        }
        binding.mainToolbar.title = title
    }

    private fun navigationVisibility(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.createGameFragment) {
                binding.mainBottomNavigation.gone()
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                navigateToGamesFragment(navController)
            } else {
                binding.mainBottomNavigation.show()
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    private fun navigateToGamesFragment(navController: NavController) {
        binding.mainToolbar.setNavigationOnClickListener {
            navController.navigate(
                CreateGameFragmentDirections
                    .actionCreateGameFragmentToGamesFragment()
            )
        }
    }
}