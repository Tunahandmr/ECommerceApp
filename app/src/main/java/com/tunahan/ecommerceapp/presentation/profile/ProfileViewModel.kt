package com.tunahan.ecommerceapp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunahan.ecommerceapp.domain.use_case.sign.CheckCurrentUserUseCase
import com.tunahan.ecommerceapp.domain.use_case.sign.CurrentUserUseCase
import com.tunahan.ecommerceapp.domain.use_case.sign.DeleteUserUseCase
import com.tunahan.ecommerceapp.domain.use_case.sign.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val currentUserUseCase: CurrentUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel() {

    private val _currentUser = MutableStateFlow(ProfileState())
    val currentUser: StateFlow<ProfileState> = _currentUser

    private val _uidCheck = MutableStateFlow(ProfileState())
    val uidCheck: StateFlow<ProfileState> = _uidCheck

    private val _deleteUser = MutableStateFlow(ProfileState())
    val deleteUser: StateFlow<ProfileState> = _deleteUser

    private var currentUserJob: Job? = null
    private var deleteUserJob: Job? = null

    init {
        getCurrentUser()
    }

    fun uidCheck(uid: String) {
        if (uid == "kWv2pe2t0VSbZH9I0TWp0P8PLwU2") {
            _uidCheck.value = ProfileState(isBoolean = true)
        } else {
            _uidCheck.value = ProfileState(isBoolean = false)
        }
    }

    private fun getCurrentUser() {

        currentUserJob?.cancel()
        currentUserJob = currentUserUseCase().onEach {
            _currentUser.value = ProfileState(currentUser = it)
        }.launchIn(viewModelScope)


    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
        }
    }

    fun deleteUser() {
        deleteUserJob?.cancel()
        deleteUserJob = deleteUserUseCase().onEach {
            _deleteUser.value = ProfileState(deleteUser = it)
        }.launchIn(viewModelScope)

    }

}