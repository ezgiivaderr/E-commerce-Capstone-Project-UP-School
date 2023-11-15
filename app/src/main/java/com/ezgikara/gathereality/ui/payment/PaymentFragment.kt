package com.ezgikara.gathereality.ui.payment

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
import com.ezgikara.gathereality.databinding.FragmentPaymentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private val binding by viewBinding (FragmentPaymentBinding::bind)

    private val viewModel by viewModels<PaymentViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            btnPayment.setOnClickListener {
                viewModel.payment(
                    etPayName.text.toString(),
                    etCard.text.toString(),
                    etMonth.text.toString(),
                    etYear.text.toString(),
                    etCVC.text.toString(),
                    etAddress.text.toString()
                )
                viewModel.clearCart()
            }
        }
        observeData()
    }

   private fun observeData() = with(binding) {
    viewModel.paymentState.observe(viewLifecycleOwner) {state ->
        when(state) {
            PaymentState.Loading -> progressBar3.visible()

            is PaymentState.SuccessState -> {
                progressBar3.gone()
                findNavController().navigate(PaymentFragmentDirections.paymenttosuccess())
            }
            is PaymentState.ShowSnackbar -> {
                progressBar3.gone()
                Snackbar.make(requireView(), state.errorMessage, 1000).show() }
        }
     }
  }
}