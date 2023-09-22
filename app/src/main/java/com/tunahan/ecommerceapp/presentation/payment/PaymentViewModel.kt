package com.tunahan.ecommerceapp.presentation.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunahan.ecommerceapp.domain.use_case.cart.DeleteAllCartUseCase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
private val deleteAllCartUseCase: DeleteAllCartUseCase
):ViewModel(){

    fun deleteAllCarts(){
        viewModelScope.launch(Dispatchers.IO) {
            deleteAllCartUseCase()
        }
    }

}