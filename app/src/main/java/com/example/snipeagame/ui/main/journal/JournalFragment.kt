package com.example.snipeagame.ui.main.journal

import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentJournalBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JournalFragment: BaseFragment<FragmentJournalBinding>(FragmentJournalBinding::inflate) {
    private val TAG = this::class.java.simpleName
}