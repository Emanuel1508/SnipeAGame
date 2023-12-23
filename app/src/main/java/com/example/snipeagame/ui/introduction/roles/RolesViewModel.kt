package com.example.snipeagame.ui.introduction.roles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.GetUserIdUseCase
import com.example.domain.usecases.UpdateUserGroupsUseCase
import com.example.domain.utils.UseCaseResponse
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseViewModel
import com.example.snipeagame.utils.ButtonState
import com.example.snipeagame.ui.models.Roles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RolesViewModel @Inject constructor(
    private val updateUserGroupsUseCase: UpdateUserGroupsUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : BaseViewModel() {
    private val _roles = MutableLiveData<List<Roles>>()
    var roles: LiveData<List<Roles>> = _roles

    private val _buttonState = MutableLiveData<ButtonState>()
    val buttonState: LiveData<ButtonState> = _buttonState

    private var roleCount = 0
    private val roleList = mutableListOf<Roles>()
    private val selectedRoles = ArrayList<String>()

    private val TAG = this::class.java.simpleName

    fun setupRole(imageId: Int, title: String, description: String, colorId: Int) {
        val role = Roles(
            imageResource = imageId,
            title = title,
            description = description,
            colorResource = colorId
        )
        roleList.add(role)
        _roles.value = roleList
    }

    fun onRoleItemClick(position: Int) {
        val role = roleList[position]
        if (role.colorResource == R.color.white) {
            roleCount++
            role.colorResource = R.color.selected_green
            selectedRoles.add(role.title)
        } else {
            roleCount--
            role.colorResource = R.color.white
            selectedRoles.remove(role.title)
        }
        toggleButtonState()
    }

    fun onFinishButtonClick(chosenFaction: String) {
        getId(chosenFaction)
    }

    private fun toggleButtonState() {
        _buttonState.value = if (roleCount == 2) ButtonState.IsEnabled else ButtonState.NotEnabled
    }

    private fun getId(chosenFaction: String) {
        viewModelScope.launch {
            showLoading()
            when (val resultId = getUserIdUseCase()) {
                is UseCaseResponse.Success -> updateUserGroupsUseCase(
                    userId = resultId.body,
                    faction = chosenFaction,
                    roles = selectedRoles
                )

                is UseCaseResponse.Failure -> showError(resultId.error)
            }
        }
        hideLoading()
    }
}