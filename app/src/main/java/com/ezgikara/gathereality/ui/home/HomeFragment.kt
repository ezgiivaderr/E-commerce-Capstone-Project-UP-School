package com.ezgikara.gathereality.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ezgikara.gathereality.R
import com.ezgikara.gathereality.common.gone
import com.ezgikara.gathereality.common.viewBinding
import com.ezgikara.gathereality.common.visible
import com.ezgikara.gathereality.data.model.response.ProductUI
import com.ezgikara.gathereality.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding (FragmentHomeBinding::bind)

    private val viewModel by viewModels<HomeViewModel>()

    private val productAdapter = ProductsAdapter(onProductClick = ::onProductClick, onFavClick = ::onFavClick)

    private val saleProductsAdapter = SaleProductsAdapter(onProductClick = ::onProductClick, onFavClick = ::onFavClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProducts()
        viewModel.getSaleProducts()

        with(binding) {
            rvProduct.adapter = productAdapter
            rvSaleproduct.adapter = saleProductsAdapter
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                HomeState.Loading -> progressBar.visible()

                is HomeState.SuccessProductState -> {
                    progressBar.gone()
                    productAdapter.submitList(state.products)
                }
                is HomeState.SuccessSaleProductState -> {
                    progressBar.gone()
                    saleProductsAdapter.submitList(state.products)
                }

                is HomeState.EmptyScreen -> {
                    progressBar.gone()
                    tvAll.gone()
                    tvSaleall.gone()
                    ivEmpty.visible()
                    tvEmpty.visible()
                    tvEmpty.text = state.failMessage
                }

                is HomeState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
}

    private fun onProductClick(id: Int) {
    findNavController().navigate(HomeFragmentDirections.hometodetail(id))

    }
    private fun onFavClick(product: ProductUI) {
        viewModel.setFavoriteState(product)
    }


}