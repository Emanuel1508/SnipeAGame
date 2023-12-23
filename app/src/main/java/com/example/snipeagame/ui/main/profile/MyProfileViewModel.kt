package com.example.snipeagame.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.UserDataParameters
import com.example.domain.usecases.GetProfileDataUseCase
import com.example.domain.usecases.GetUserIdUseCase
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.example.snipeagame.base.BaseViewModel
import com.example.snipeagame.ui.models.ProfileUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getProfileDataUseCase: GetProfileDataUseCase

) : BaseViewModel() {
    private val _profileData = MutableLiveData<ProfileUiModel>()
    val profileData: LiveData<ProfileUiModel> = _profileData

    init {
        getUserId()
    }

    private suspend fun getProfileData(userId: String) {
        when (val result = getProfileDataUseCase(userId)) {
            is UseCaseResponse.Success -> onDataSuccess(result.body)
            is UseCaseResponse.Failure -> onDataFailure(result.error)
        }
    }

    private fun onDataSuccess(profileParameters: UserDataParameters) {
        hideLoading()
        viewModelScope.launch(Dispatchers.Main) {
            _profileData.postValue(
                ProfileUiModel(
                    profileParameters.name,
                    profileParameters.email,
                    profileParameters.phone,
                    profileParameters.faction,
                    profileParameters.roles,
                    profileParameters.takedowns,
                    profileParameters.games

                )
            )
        }
    }

    private fun onDataFailure(error: ErrorMessage) {
        hideLoading()
        showError(error)
    }

    private fun getUserId() {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getUserIdUseCase()) {
                is UseCaseResponse.Success -> getProfileData(result.body)
                is UseCaseResponse.Failure -> showError(result.error)
            }
        }
    }
}