package com.ezgikara.gathereality.ui.detail

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
class DetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _detailState = MutableLiveData<DetailState>()
    val detailState: LiveData<DetailState> get() = _detailState

    private var favProduct: ProductUI? = null
    fun getProductDetail(id: Int) = viewModelScope.launch {
        _detailState.value = DetailState.Loading

        _detailState.value = when (val result = productRepository.getProductDetail(id)) {
            is Resource.Success -> DetailState.SuccessState(result.data)
            is Resource.Fail -> DetailState.EmptyScreen(result.failMessage)
            is Resource.Error -> DetailState.ShowPopUp(result.errorMessage)
        }
    }
    fun addToCart(productId: Int) = viewModelScope.launch {
        productRepository.addToCart(authRepository.getUserId(), productId)
    }
    fun setFavoriteState(id: Int) = viewModelScope.launch {
        favProduct?.let { product ->
            if (product.isfav) {
                productRepository.deleteFromFavorites(product)
            } else {
                productRepository.addToFavorites(product)
            }
        }
        getProductDetail(id)
    }
}
sealed interface DetailState {
    object Loading : DetailState
    data class SuccessState(val product: ProductUI) : DetailState
    data class EmptyScreen(val failMessage: String) : DetailState
    data class ShowPopUp(val errorMessage: String) : DetailState
}