package com.tunahan.ecommerceapp.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.domain.use_case.sign.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {


    private val _result = MutableStateFlow(SignUpState())
    val result: StateFlow<SignUpState> = _result

    private var signUpJob: Job? = null
    fun signUpWithEmailAndPassword(email: String, password: String) {

        signUpJob?.cancel()

        signUpJob = signUpUseCase(email, password).onEach {
            _result.emit(SignUpState(it))
        }.launchIn(viewModelScope)


    }

}