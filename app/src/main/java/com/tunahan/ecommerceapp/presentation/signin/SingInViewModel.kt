package com.tunahan.ecommerceapp.presentation.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.tunahan.ecommerceapp.common.Resource
import com.tunahan.ecommerceapp.domain.use_case.sign.CheckCurrentUserUseCase
import com.tunahan.ecommerceapp.domain.use_case.sign.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val checkCurrentUserUseCase: CheckCurrentUserUseCase
) : ViewModel() {


    private val _result = MutableStateFlow(SignInState())
    val result: StateFlow<SignInState> = _result

    private var signInJob: Job? = null
    fun signInWithEmailAndPassword(email: String, password: String) {

        signInJob?.cancel()

        signInJob = signInUseCase(email, password)
            .onEach { resource ->
                _result.emit(SignInState(resource))
            }
            .launchIn(viewModelScope)

    }

    suspend fun checkCurrentUser(): Boolean {
        return checkCurrentUserUseCase()
    }


}