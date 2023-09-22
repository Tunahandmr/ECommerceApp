package com.tunahan.ecommerceapp.presentation.updatepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.domain.use_case.sign.CheckCurrentUserUseCase
import com.tunahan.ecommerceapp.domain.use_case.sign.CurrentUserUseCase
import com.tunahan.ecommerceapp.domain.use_case.sign.UpdatePasswordUseCase
import com.tunahan.ecommerceapp.presentation.profile.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdatePasswordViewModel @Inject constructor(
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val checkCurrentUserUseCase: CheckCurrentUserUseCase
) : ViewModel() {

    private val _updatePassword = MutableStateFlow(UpdatePasswordState())
    val updatePassword: StateFlow<UpdatePasswordState> = _updatePassword

    private var updateJob: Job? = null

    fun updatePassword(email: String, currentPassword: String, newpassword: String) {
        updateJob?.cancel()
        updateJob = updatePasswordUseCase(email, currentPassword, newpassword).onEach {
            _updatePassword.value = UpdatePasswordState(password = it)
        }.launchIn(viewModelScope)
    }

    suspend fun checkCurrentUser(): Boolean {
        return checkCurrentUserUseCase()
    }

}