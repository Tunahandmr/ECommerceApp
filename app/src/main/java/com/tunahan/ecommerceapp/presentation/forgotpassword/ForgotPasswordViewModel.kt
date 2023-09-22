package com.tunahan.ecommerceapp.presentation.forgotpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.domain.use_case.sign.ForgotPasswordUseCase
import com.tunahan.ecommerceapp.domain.use_case.sign.LiveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val liveUseCase: LiveUseCase
) :ViewModel(){


    private val _result = MutableLiveData<Resource<Void>>()
    val result: LiveData<Resource<Void>> = _result

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            _result.value = Resource.Loading
            _result.value = liveUseCase(email)
        }
    }

   /* private val _requestPassword = MutableStateFlow(ForgotPasswordState())
    val requestPassword:StateFlow<ForgotPasswordState> = _requestPassword
   suspend fun sendPasswordResetEmail(email: String) {

            forgotPasswordUseCase(email).onEach {
            _requestPassword.value = ForgotPasswordState(it)
            }.launchIn(viewModelScope)

    }*/
}