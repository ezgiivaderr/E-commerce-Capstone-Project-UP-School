package com.ezgikara.gathereality.ui.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ezgikara.gathereality.R
import com.ezgikara.gathereality.common.gone
import com.ezgikara.gathereality.common.viewBinding
import com.ezgikara.gathereality.common.visible
import com.ezgikara.gathereality.databinding.FragmentCartBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {

    private val binding by viewBinding(FragmentCartBinding::bind)

    private val viewModel by viewModels<CartViewModel>()

    private val cartAdapter =
        CartAdapter(onProductClick = ::onProductClick, onDeleteClick = ::onDeleteClick)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCartProducts()

        with(binding) {

            rvCard.adapter = cartAdapter

            ivClear.setOnClickListener {
                viewModel.clearCart()
            }

            btnBuyNow.setOnClickListener {
                findNavController().navigate(CartFragmentDirections.carttopayment())
            }
        }

        observeData()
    }
    private fun observeData() = with(binding) {
        viewModel.cartState.observe(viewLifecycleOwner) { state ->
            when (state) {
                CartState.Loading -> progressBar.visible()

                is CartState.SuccessState -> {
                    progressBar.gone()
                    cartAdapter.submitList(state.products)
                    tvTextTotal.visible()
                    tvTotal.visible()
                    ivClear.visible()
                    btnBuyNow.visible()
                }

                is CartState.EmptyScreen -> {
                    progressBar.gone()
                    rvCard.gone()
                    tvTextTotal.gone()
                    btnBuyNow.gone()
                    tvTotal.gone()
                    tvEmpty.visible()
                    tvEmpty.text = state.failMessage
                }

                is CartState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
        viewModel.totalAmount.observe(viewLifecycleOwner) { amount ->
            tvTotal.text = String.format("$ %.2f", amount)
        }
    }
    private fun onProductClick(id: Int) {
        findNavController().navigate(CartFragmentDirections.carttodetail(id))
    }
    private fun onDeleteClick(productId: Int) {
        viewModel.deleteFromCart(productId)
    }
}