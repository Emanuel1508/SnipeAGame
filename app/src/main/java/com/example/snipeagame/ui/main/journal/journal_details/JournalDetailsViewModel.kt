package com.example.snipeagame.ui.main.journal.journal_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.JournalParameters
import com.example.domain.usecases.GetJournalDetailsUseCase
import com.example.domain.usecases.GetUserIdUseCase
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.example.snipeagame.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalDetailsViewModel @Inject constructor(
    private var getUserIdUseCase: GetUserIdUseCase,
    private var getJournalDetailsUseCase: GetJournalDetailsUseCase
) : BaseViewModel() {
    private var _journalDetailsData = MutableLiveData<JournalParameters>()
    var journalDetailsData: LiveData<JournalParameters> = _journalDetailsData

    private var journalId: String = ""

    private val TAG = this::class.java.simpleName

    init {
        getJournalDetailsData()
    }

    private fun getJournalDetailsData() {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = getUserIdUseCase()) {
                is UseCaseResponse.Success -> {
                    onGetUserIdSuccess(response.body)
                }

                is UseCaseResponse.Failure -> {
                    onDataFailure(response.error)
                }
            }
        }
    }

    private suspend fun onGetUserIdSuccess(userId: String) {
        when (val journalDataResponse = getJournalDetailsUseCase(userId, journalId)) {
            is UseCaseResponse.Success -> onGetJournalDetailsSuccess(journalDataResponse.body)
            is UseCaseResponse.Failure -> {
                showError(journalDataResponse.error)
            }
        }
        hideLoading()
    }

    private fun onGetJournalDetailsSuccess(journalDetails: JournalParameters) {
        if (journalDetails.rating.isEmpty()) journalDetails.rating = 0f.toString()
        _journalDetailsData.postValue(
            JournalParameters(
                journalDetails.gameId,
                journalDetails.userId,
                journalDetails.location,
                journalDetails.date,
                journalDetails.time,
                journalDetails.numberOfPlayers,
                journalDetails.takedowns,
                journalDetails.rating,
                journalDetails.journalText
            )
        )
    }

    private fun onDataFailure(errorMessage: ErrorMessage) {
        showError(errorMessage)
        hideLoading()
    }


    fun setJournalId(id: String) {
        journalId = id
    }
}