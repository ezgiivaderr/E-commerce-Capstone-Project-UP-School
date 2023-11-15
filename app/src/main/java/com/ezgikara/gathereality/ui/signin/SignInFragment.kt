package com.ezgikara.gathereality.ui.signin

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
import com.ezgikara.gathereality.databinding.FragmentSignInBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding(FragmentSignInBinding::bind)

    private val viewModel by viewModels<SignInViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            btnSignin.setOnClickListener {
                viewModel.signIn(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
            }

            tvSignup.setOnClickListener {
                findNavController().navigate(R.id.signInToSignUp)
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.signInState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SignInState.Loading -> progressBar2.visible()

                is SignInState.GoToHome -> {
                    progressBar2.gone()
                    findNavController().navigate(R.id.signInToMainGraph)
                }

                is SignInState.ShowPopUp -> {
                    progressBar2.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}
