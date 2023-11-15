package com.ezgikara.gathereality.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezgikara.gathereality.common.Resource
import com.ezgikara.gathereality.data.model.response.ProductUI
import com.ezgikara.gathereality.data.repository.AuthRepository
import com.ezgikara.gathereality.data.repository.ProductRepository
import com.ezgikara.gathereality.ui.favorites.FavoritesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState> get() = _searchState
    fun searchProduct(query: String) = viewModelScope.launch {
        _searchState.value = SearchState.Loading

        _searchState.value = when (val result = productRepository.searchProduct(query)) {
            is Resource.Success -> SearchState.SuccessState(result.data)
            is Resource.Fail -> SearchState.EmptyScreen(result.failMessage)
            is Resource.Error -> SearchState.ShowPopUp(result.errorMessage)
        }
    }
}
sealed interface SearchState {
    object  Loading : SearchState
    data class SuccessState(val products: List<ProductUI>) : SearchState
    data class EmptyScreen(val failMessage : String) : SearchState
    data class ShowPopUp(val errorMessage : String) : SearchState
}