package com.example.snipeagame.ui.main.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.domain.models.Result
import com.example.domain.utils.ErrorMessage
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentMyProfileBinding
import com.example.snipeagame.ui.models.ProfileUiModel
import com.example.snipeagame.utils.AlertDialogFragment
import com.example.snipeagame.utils.hide
import com.example.snipeagame.utils.hideRefresh
import com.example.snipeagame.utils.mapToUI
import com.example.snipeagame.utils.show
import com.example.snipeagame.utils.showRefresh
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfileFragment :
    BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate) {
    private val viewModel: MyProfileViewModel by viewModels()

    private val TAG = this::class.java.simpleName
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        with(viewModel) {
            profileData.observe(viewLifecycleOwner) { profileUi ->
                setupViews(profileUi)
            }
            errorLiveData.observe(viewLifecycleOwner) { error ->
                showAlertDialog(error.message)
            }
            loadingLiveData.observe(viewLifecycleOwner) {
                updateRefreshAnimation(it)
            }
        }
    }

    private fun setupViews(profileUI: ProfileUiModel) {
        with(binding) {
            with(profileUI) {
                fullNameTextView.text = name
                takedownsTextView.text = takedowns.toString()
                gamesTextView.text = games.toString()
                firstRoleTextView.text = getString(R.string.first_role, roles?.get(0))
                secondRoleTextView.text = getString(R.string.second_role, roles?.get(1))
                emailTextView.text = getString(R.string.email_address, email)
                phoneTextView.text = getString(R.string.phone_number, phone)
            }
        }
    }

    private fun updateRefreshAnimation(value: Result.Loading) {
        when (value.shouldShowLoading) {
            true -> showLoadingAnimation()
            false -> hideLoadingAnimation()
        }
    }

    private fun showLoadingAnimation() {
        with(binding) {
            myProfile.hide()
            swipeRefresh.showRefresh()
        }
    }

    private fun hideLoadingAnimation() {
        with(binding) {
            myProfile.show()
            swipeRefresh.hideRefresh()
        }
    }

    private fun showAlertDialog(error: ErrorMessage) {
        val alertDialogFragment =
            AlertDialogFragment.newInstance(title = getString(R.string.oops_title),
                description = getString(error.mapToUI()),
                onRetryClick = {})
        alertDialogFragment.show(parentFragmentManager, TAG)
    }
}