package com.example.snipeagame.ui.introduction.register

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentSuccesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuccessFragment :
    BaseFragment<FragmentSuccesBinding>(FragmentSuccesBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countdownToNavigateBack()
    }

    private fun countdownToNavigateBack() {
        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.congratulationsTextView.text =
                    getString(R.string.account_created, secondsRemaining)
            }

            override fun onFinish() {
                navigateToWelcomeScreen()
            }
        }.start()
    }

    private fun navigateToWelcomeScreen() {
        findNavController().navigate(
            SuccessFragmentDirections
                .actionSuccessFragmentToWelcomeFragment()
        )
    }
}