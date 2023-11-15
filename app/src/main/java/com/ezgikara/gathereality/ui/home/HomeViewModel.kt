package com.ezgikara.gathereality.ui.home

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
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState> get() = _homeState


    fun getProducts() = viewModelScope.launch {
        _homeState.value = HomeState.Loading

        _homeState.value = when (val result = productRepository.getProducts()) {
            is Resource.Success -> HomeState.SuccessProductState(result.data)
            is Resource.Fail -> HomeState.EmptyScreen(result.failMessage)
            is Resource.Error -> HomeState.ShowPopUp(result.errorMessage)
        }
    }

    fun getSaleProducts() = viewModelScope.launch {
        _homeState.value = HomeState.Loading
        _homeState.value = when (val result = productRepository.getSaleProducts()) {
            is Resource.Success -> HomeState.SuccessSaleProductState(result.data)
            is Resource.Fail -> HomeState.EmptyScreen(result.failMessage)
            is Resource.Error -> HomeState.ShowPopUp(result.errorMessage)
        }
    }

    fun setFavoriteState(product: ProductUI) = viewModelScope.launch {
        if (product.isfav) {
            productRepository.deleteFromFavorites(product)
        } else {
            productRepository.addToFavorites(product)
        }
        getProducts()
        getSaleProducts()
    }
}

sealed interface HomeState {
    object Loading : HomeState
    data class SuccessProductState(val products: List<ProductUI>) : HomeState
    data class SuccessSaleProductState(val products: List<ProductUI>) : HomeState
    data class EmptyScreen(val failMessage: String) : HomeState
    data class ShowPopUp(val errorMessage: String) : HomeState
}
