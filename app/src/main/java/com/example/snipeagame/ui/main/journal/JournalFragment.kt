package com.example.snipeagame.ui.main.journal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.JournalParameters
import com.example.domain.utils.ErrorMessage
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentJournalBinding
import com.example.snipeagame.utils.AlertDialogFragment
import com.example.snipeagame.utils.mapToUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JournalFragment : BaseFragment<FragmentJournalBinding>(FragmentJournalBinding::inflate) {
    private val viewModel: JournalViewModel by viewModels()
    private lateinit var adapter: JournalAdapter
    private lateinit var recyclerView: RecyclerView
    private val TAG = this::class.java.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupAdapter()
        setupObservers()
        setupLoading()
    }

    private fun setupListener() {
        with(binding) {
            swipeRefresh.setOnRefreshListener {
                viewModel.onRefresh()
            }
        }
    }

    private fun setupAdapter() {
        adapter = JournalAdapter(object : JournalAdapter.JournalItemClickListener {
            override fun onJournalItemClick(journal: JournalParameters) {
                findNavController()
                    .navigate(
                        JournalFragmentDirections
                            .actionJournalFragmentToJournalDetailsFragment(journal.gameId)
                    )
            }
        })
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        with(viewModel) {
            journalEntries.observe(viewLifecycleOwner) { journalEntries ->
                adapter.setMyJournalEntries(journalEntries)
            }
            errorLiveData.observe(viewLifecycleOwner) { error ->
                showAlertDialog(error.message)
            }
        }
    }

    private fun setupLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading.shouldShowLoading
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