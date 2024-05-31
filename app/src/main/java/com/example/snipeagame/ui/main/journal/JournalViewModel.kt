package com.example.snipeagame.ui.main.journal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.JournalParameters
import com.example.domain.usecases.GetJournalEntriesUseCase
import com.example.domain.usecases.GetUserIdUseCase
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.example.snipeagame.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getJournalEntriesUseCase: GetJournalEntriesUseCase
) : BaseViewModel() {
    private var _journalEntries = MutableLiveData<List<JournalParameters>>()
    var journalEntries: LiveData<List<JournalParameters>> = _journalEntries

    private val TAG = this::class.java.simpleName

    init {
        getUserId()
    }

    private fun getUserId() {
        viewModelScope.launch(Dispatchers.IO) {
            showLoading()
            when (val resultId = getUserIdUseCase()) {
                is UseCaseResponse.Success -> onIdSuccess(resultId.body)
                is UseCaseResponse.Failure -> onDataFailure(resultId.error)
            }
        }
    }

    private suspend fun onIdSuccess(userId: String) {
        when (val journalResponse = getJournalEntriesUseCase(userId)) {
            is UseCaseResponse.Success -> onFetchJournalEntriesSuccess(journalResponse.body)
            is UseCaseResponse.Failure -> onDataFailure(journalResponse.error)
        }
        hideLoading()
    }

    private fun onDataFailure(error: ErrorMessage) {
        showError(error)
        hideLoading()
    }

    private fun onFetchJournalEntriesSuccess(journalList: List<JournalParameters>) {
        _journalEntries.postValue(journalList)
        hideLoading()
    }

    fun onRefresh() {
        getUserId()
    }
}