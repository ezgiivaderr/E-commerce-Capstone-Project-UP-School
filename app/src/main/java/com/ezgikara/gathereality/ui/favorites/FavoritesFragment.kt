package com.ezgikara.gathereality.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ezgikara.gathereality.R
import com.ezgikara.gathereality.common.gone
import com.ezgikara.gathereality.common.viewBinding
import com.ezgikara.gathereality.common.visible
import com.ezgikara.gathereality.data.model.response.ProductUI
import com.ezgikara.gathereality.databinding.FragmentFavoritesBinding
import com.ezgikara.gathereality.databinding.FragmentHomeBinding
import com.ezgikara.gathereality.ui.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val binding by viewBinding (FragmentFavoritesBinding::bind)

    private val viewModel by viewModels<FavoritesViewModel>()

    private val favoritesAdapter = FavoritesAdapter(onProductClick = ::onProductClick, onDeleteClick = ::onDeleteClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavorites()

        observeData()

        with(binding) {
            rvFav.adapter = favoritesAdapter

            ivClear.setOnClickListener {
                viewModel.clearFavorites()
            }

        }
    }

    private fun observeData() = with(binding) {
        viewModel.favoritesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                FavoritesState.Loading -> progressBar.visible()

                is FavoritesState.SuccessState -> {
                    progressBar.gone()
                    favoritesAdapter.submitList(state.products)
                }

                is FavoritesState.EmptyScreen -> {
                    progressBar.gone()
                    ivEmpty.visible()
                    tvEmpty.visible()
                    rvFav.gone()
                    tvEmpty.text = state.failMessage
                }

                is FavoritesState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }

    private fun onProductClick(id: Int) {
       findNavController().navigate(FavoritesFragmentDirections.favoritestodetail(id))
    }

    private fun onDeleteClick(product: ProductUI) {
        viewModel.deleteFromFavorites(product)
    }
}

