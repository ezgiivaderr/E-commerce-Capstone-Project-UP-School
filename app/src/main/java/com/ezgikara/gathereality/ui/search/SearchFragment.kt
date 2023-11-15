package com.ezgikara.gathereality.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ezgikara.gathereality.R
import com.ezgikara.gathereality.common.gone
import com.ezgikara.gathereality.common.viewBinding
import com.ezgikara.gathereality.common.visible
import com.ezgikara.gathereality.databinding.FragmentSearchBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding by viewBinding (FragmentSearchBinding::bind)

    private val viewModel by viewModels<SearchViewModel>()

    private val searchAdapter = SearchAdapter(onProductClick = ::onProductClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()

        with(binding) {
            rvSearch.adapter = searchAdapter

            searchView2.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null && newText.length > 3) {
                        viewModel.searchProduct(newText)
                    } else {
                        searchAdapter.submitList(emptyList())
                    }
                    return true
                }
            })

    }
    }
    private fun observeData() = with(binding) {
        viewModel.searchState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SearchState.Loading -> progressBar3.visible()

                is SearchState.SuccessState -> {
                    progressBar3.gone()
                    searchAdapter.submitList(state.products)
                }
                is SearchState.EmptyScreen -> {
                    progressBar3.gone()
                    rvSearch.gone()
                    tvEmpty.text = state.failMessage
                    tvEmpty.visible()
                    ivEmpty.visible()
                }

                is SearchState.ShowPopUp -> {
                    progressBar3.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }

    private fun onProductClick(id: Int) {
        findNavController().navigate(SearchFragmentDirections.searchtodetail(id))
    }
}