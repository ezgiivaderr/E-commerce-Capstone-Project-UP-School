package com.ezgikara.gathereality.ui.signup

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
import com.ezgikara.gathereality.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val binding by viewBinding(FragmentSignUpBinding::bind)

    private val viewModel by viewModels<SignUpViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnSignup.setOnClickListener {
                viewModel.signUp(
                    etMail.text.toString(),
                    etPasswor.text.toString()
                )
            }
        }
        observeData()
    }
    private fun observeData() = with(binding) {
        viewModel.signUpState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SignUpState.Loading -> progressBar.visible()

                is SignUpState.GoToHome -> {
                    progressBar.gone()
                    findNavController().navigate(R.id.signUpToMainGraph)
                }

                is SignUpState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}