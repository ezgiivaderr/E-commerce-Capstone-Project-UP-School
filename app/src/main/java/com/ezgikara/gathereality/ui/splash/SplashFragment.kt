package com.ezgikara.gathereality.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ezgikara.gathereality.R
import com.ezgikara.gathereality.common.viewBinding

import com.ezgikara.gathereality.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {


    private val binding by viewBinding(FragmentSplashBinding::bind)

    private val viewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
    }


    private fun observeData() {
        viewModel.splashState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SplashState.GoToSignIn -> {
                    findNavController().navigate(R.id.splashToSignIn)
                }

                SplashState.GoToHome -> {
                    findNavController().navigate(R.id.splashToMainGraph)
                }
            }
        }
    }

}