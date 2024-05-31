package com.example.snipeagame.ui.main.journal.journal_details

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.JournalParameters
import com.example.domain.models.JournalUpdateParameters
import com.example.domain.usecases.GetJournalDetailsUseCase
import com.example.domain.usecases.GetUserIdUseCase
import com.example.domain.usecases.SaveJournalImagesUseCase
import com.example.domain.usecases.UpdateJournalUseCase
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
    private var getJournalDetailsUseCase: GetJournalDetailsUseCase,
    private var updateJournalUseCase: UpdateJournalUseCase,
    private var saveJournalImagesUseCase: SaveJournalImagesUseCase
) : BaseViewModel() {
    private var _journalDetailsData = MutableLiveData<JournalParameters>()
    var journalDetailsData: LiveData<JournalParameters> = _journalDetailsData

    private var _isEditing = MutableLiveData<Boolean>()
    val isEditing: LiveData<Boolean> = _isEditing

    private var journalId: String = ""
    private var userId: String = ""

    private val TAG = this::class.java.simpleName

    init {
        _isEditing.value = false
        getUserId()
    }

    private fun getUserId() {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = getUserIdUseCase()) {
                is UseCaseResponse.Success -> {
                    userId = response.body
                    getJournalDetailsData()
                }

                is UseCaseResponse.Failure -> showError(response.error)
            }
        }
        hideLoading()
    }

    private fun getJournalDetailsData() {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            when (val journalDataResponse = getJournalDetailsUseCase(userId, journalId)) {
                is UseCaseResponse.Success -> {
                    onGetJournalDetailsSuccess(journalDataResponse.body)
                }

                is UseCaseResponse.Failure -> {
                    onDataFailure(journalDataResponse.error)
                }
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
                journalDetails.journalText,
                journalDetails.imageUrls
            )
        )
    }

    private fun onDataFailure(errorMessage: ErrorMessage) {
        showError(errorMessage)
        hideLoading()
    }

    private fun updateJournal(journalUpdateParameters: JournalUpdateParameters) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            with(journalUpdateParameters) {
                when (val updateResponse = updateJournalUseCase(
                    userId,
                    journalId,
                    rating,
                    journalText
                )) {
                    is UseCaseResponse.Success -> getJournalDetailsData()
                    is UseCaseResponse.Failure -> showError(updateResponse.error)
                }
            }
            hideLoading()
        }
    }

    fun onEditButtonClick(journalText: String, rating: String) {
        if (_isEditing.value == true) {
            val journalUpdateParameters = JournalUpdateParameters(
                userId = userId,
                journalId = journalId,
                rating = rating,
                journalText = journalText
            )
            updateJournal(journalUpdateParameters)
        }
        _isEditing.value?.let { editValue ->
            _isEditing.value = !editValue
        }
    }

    fun setJournalId(id: String) {
        journalId = id
    }

    fun onRefresh() {
        getJournalDetailsData()
    }

    fun handleSelectedImages(imageUris: MutableList<Uri>) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = saveJournalImagesUseCase(imageUris, userId, journalId)) {
                is UseCaseResponse.Success -> showSuccess(result.body)
                is UseCaseResponse.Failure -> showError(result.error)
            }
            hideLoading()
        }
    }
}
