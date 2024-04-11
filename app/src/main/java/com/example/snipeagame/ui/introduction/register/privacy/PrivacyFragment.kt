package com.example.snipeagame.ui.introduction.register.privacy

import android.os.Bundle
import android.view.View
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentPrivacyPolicyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyFragment :
    BaseFragment<FragmentPrivacyPolicyBinding>(FragmentPrivacyPolicyBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}