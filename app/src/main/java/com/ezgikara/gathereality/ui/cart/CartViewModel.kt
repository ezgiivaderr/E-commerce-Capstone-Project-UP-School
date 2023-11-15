package com.ezgikara.gathereality.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezgikara.gathereality.common.Resource

import com.ezgikara.gathereality.data.model.response.ProductUI
import com.ezgikara.gathereality.data.repository.AuthRepository
import com.ezgikara.gathereality.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _cartState = MutableLiveData<CartState>()
    val cartState: LiveData<CartState> get() = _cartState

    private val _totalAmount = MutableLiveData(0.0)
    val totalAmount: LiveData<Double> = _totalAmount

    fun getCartProducts() = viewModelScope.launch {
        _cartState.value = CartState.Loading

       when (val result = productRepository.getCartProducts(authRepository.getUserId())) {
            is Resource.Success -> {
                _cartState.value = CartState.SuccessState(result.data)
                _totalAmount.value = result.data.sumOf { product ->
                    if (product.saleState) {
                        product.salePrice
                    } else {
                        product.price
                    }
                }
            }
            is Resource.Fail -> CartState.EmptyScreen(result.failMessage)
            is Resource.Error -> CartState.ShowPopUp(result.errorMessage)
        }
    }
    fun deleteFromCart(productId:Int)=viewModelScope.launch {
        productRepository.deleteFromCart(authRepository.getUserId(),productId)
        getCartProducts()
        clearTotalAmount()
    }
    fun clearCart() = viewModelScope.launch {
        productRepository.clearCart(authRepository.getUserId())
        getCartProducts()
        clearTotalAmount()
    }
     fun clearTotalAmount() {
        _totalAmount.value = 0.0
    }

}

sealed interface CartState {
    object Loading : CartState
    data class SuccessState(val products: List<ProductUI>) : CartState
    data class EmptyScreen(val failMessage: String) : CartState
    data class ShowPopUp(val errorMessage: String) : CartState
}