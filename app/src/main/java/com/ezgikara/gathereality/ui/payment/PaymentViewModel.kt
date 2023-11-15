package com.ezgikara.gathereality.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezgikara.gathereality.common.Resource
import com.ezgikara.gathereality.data.repository.AuthRepository
import com.ezgikara.gathereality.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _paymentState = MutableLiveData<PaymentState>()
    val paymentState: LiveData<PaymentState> get() = _paymentState

    fun clearCart() = viewModelScope.launch {
        val result = productRepository.clearCart(authRepository.getUserId())
        _paymentState.value = when (result) {
            is Resource.Success -> PaymentState.SuccessState
            else -> PaymentState.ShowSnackbar("Failed")
        }
    }
    fun payment(name: String, cardNum: String, month: String, year: String, cvc: String, address: String) = viewModelScope.launch {
        _paymentState.value = PaymentState.Loading

        if (isValidInput(name, cardNum, month, year, cvc, address)) {
            _paymentState.value = PaymentState.SuccessState
        }
        else {
            _paymentState.value = PaymentState.ShowSnackbar("Please enter the empty fields correctly.")
        }
    }
    private fun isValidInput(
        name: String,
        cardNum: String,
        month: String,
        year: String,
        cvc: String,
        address: String
    ): Boolean {
        return  name.isNotEmpty() &&
                cardNum.length == 16 &&
                month.length == 2  &&
                year.length == 4 &&
                cvc.length == 3 &&
                address.isNotEmpty()
    }
}
sealed interface PaymentState {
    object Loading : PaymentState
    object SuccessState: PaymentState
    data class ShowSnackbar(val errorMessage: String) : PaymentState
}