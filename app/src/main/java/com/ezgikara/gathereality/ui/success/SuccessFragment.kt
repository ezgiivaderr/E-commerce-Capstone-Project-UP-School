package com.ezgikara.gathereality.ui.success

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ezgikara.gathereality.R
import com.ezgikara.gathereality.common.viewBinding
import com.ezgikara.gathereality.databinding.FragmentSuccessBinding

class SuccessFragment : Fragment(R.layout.fragment_success) {

    private val binding by viewBinding (FragmentSuccessBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGohome.setOnClickListener {
            findNavController().navigate(SuccessFragmentDirections.successtohome())
        }
    }
}