package com.example.snipeagame.ui.introduction.faction

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentFactionItemOneBinding

class FactionItemOneFragment :
    BaseFragment<FragmentFactionItemOneBinding>(FragmentFactionItemOneBinding::inflate) {
    private val TAG = this::class.java.simpleName
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.informationCardView.setOnClickListener {
            binding.informationTextView.isVisible = true
        }
    }
}