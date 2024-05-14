package com.example.snipeagame.ui.main.journal.journal_details

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.domain.models.JournalParameters
import com.example.domain.models.Result
import com.example.domain.utils.ErrorMessage
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentJournalDetailsBinding
import com.example.snipeagame.utils.AlertDialogFragment
import com.example.snipeagame.utils.hide
import com.example.snipeagame.utils.hideRefresh
import com.example.snipeagame.utils.mapToUI
import com.example.snipeagame.utils.show
import com.example.snipeagame.utils.showRefresh
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JournalDetailsFragment :
    BaseFragment<FragmentJournalDetailsBinding>(FragmentJournalDetailsBinding::inflate) {
    private val viewModel: JournalDetailsViewModel by viewModels()
    private lateinit var journalId: String

    private val TAG = this::class.java.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getJournalId()
        passJournalId()
        setupListeners()
        setupLoading()
        setupObservers()
    }

    private fun setupListeners() {
        with(binding) {
            editButton.setOnClickListener {
                viewModel.onEditButtonClick(
                    journalTextView.text.toString(),
                    ratingBar.rating.toString()
                )
            }
            journalInputText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    editButton.hide()
                } else {
                    editButton.show()
                }
            }
            swipeRefresh.setOnRefreshListener {
                viewModel.onRefresh()
            }
        }
        setupInputTextListener()
    }

    private fun setupLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading.shouldShowLoading
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            errorLiveData.observe(viewLifecycleOwner) { error ->
                showAlertDialog(error.message)
            }
            loadingLiveData.observe(viewLifecycleOwner) {
                updateRefreshAnimation(it)
            }
            journalDetailsData.observe(viewLifecycleOwner) { journalDetails ->
                setupUI(journalDetails)
            }
            isEditing.observe(viewLifecycleOwner) { editValue ->
                updateUIEditState(editValue)
            }
        }
    }

    private fun passJournalId() {
        viewModel.setJournalId(journalId)
    }

    private fun getJournalId() {
        val args: JournalDetailsFragmentArgs by navArgs()
        journalId = args.journalId
    }

    private fun updateRefreshAnimation(value: Result.Loading) {
        when (value.shouldShowLoading) {
            true -> showLoadingAnimation()
            false -> hideLoadingAnimation()
        }
    }

    private fun showLoadingAnimation() {
        with(binding) {
            journalDetailLayout.hide()
            swipeRefresh.showRefresh()
        }
    }

    private fun hideLoadingAnimation() {
        with(binding) {
            journalDetailLayout.show()
            swipeRefresh.hideRefresh()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI(journalDetails: JournalParameters) {
        with(binding) {
            ratingBar.setIsIndicator(true)
            with(journalDetails) {
                locationTextView.text = location
                timeTextView.text = "$date $time"
                ratingBar.rating = rating.toFloat()
                journalTextView.text = journalText
                takedownsTextView.text = journalDetails.takedowns
            }
        }
    }

    private fun updateUIEditState(editState: Boolean) {
        with(binding) {
            when (editState) {
                true -> {
                    ratingBar.setIsIndicator(false)
                    journalInputLayout.show()
                    journalInputText.show()
                    journalTextView.hide()
                    journalCardView.hide()
                    editButton.text = getString(R.string.save_journal)
                }

                false -> {
                    ratingBar.setIsIndicator(true)
                    journalInputLayout.hide()
                    journalInputText.hide()
                    journalTextView.show()
                    journalCardView.show()
                    editButton.text = getString(R.string.edit_journal)
                }
            }
        }
    }

    private fun setupInputTextListener() {
        with(binding) {
            journalInputText.setOnEditorActionListener { view, actionId, _ ->
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    journalTextView.text = journalInputText.text
                    handleFocus(view)
                    inputMethodManager.hideSoftInputFromWindow(
                        journalInputText.windowToken,
                        0
                    )
                }
                true
            }
        }
    }

    private fun handleFocus(view: TextView) {
        Handler().postDelayed({
            view.clearFocus()
        }, 200)
    }

    private fun showAlertDialog(error: ErrorMessage) {
        val alertDialogFragment =
            AlertDialogFragment.newInstance(title = getString(R.string.oops_title),
                description = getString(error.mapToUI()),
                onRetryClick = {})
        alertDialogFragment.show(parentFragmentManager, TAG)
    }
}