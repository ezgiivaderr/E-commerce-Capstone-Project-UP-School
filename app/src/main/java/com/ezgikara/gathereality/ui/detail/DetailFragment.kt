package com.ezgikara.gathereality.ui.detail

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ezgikara.gathereality.R
import com.ezgikara.gathereality.common.gone

import com.ezgikara.gathereality.common.viewBinding
import com.ezgikara.gathereality.common.visible
import com.ezgikara.gathereality.databinding.FragmentDetailBinding
import com.ezgikara.gathereality.ui.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding by viewBinding(FragmentDetailBinding::bind)

    private val viewModel by viewModels<DetailViewModel>()

    private val args by navArgs<DetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProductDetail(args.id)

        with(binding) {
            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }
            btnAddtocart.setOnClickListener {
                viewModel.addToCart(args.id)
            }
            ivFavorite.setOnClickListener {
                viewModel.setFavoriteState(args.id)
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.detailState.observe(viewLifecycleOwner) { state ->
            when (state) {
                DetailState.Loading -> progressBar.visible()

                is DetailState.SuccessState -> {
                    progressBar.gone()
                    Glide.with(iVProduct).load(state.product.imageOne).into(iVProduct)
                    tVTitle.text = state.product.title
                    tVPrice.text = "${state.product.price} $"
                    tVDescription.text = state.product.description
                    ratingBar.rating = state.product.rate.toFloat()
                    btnAddtocart.visible()

                    if (state.product.saleState) {
                        tVPrice.paintFlags = tVPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        tVSaleprice.text = "${state.product.salePrice} $"
                    } else {
                        tVPrice.paintFlags = 0
                        tVSaleprice.gone()
                    }
                }

                is DetailState.EmptyScreen -> {
                    progressBar.gone()
                    ivEmpty.visible()
                    tvEmpty.visible()
                    tvEmpty.text = state.failMessage
                }

                is DetailState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }


}