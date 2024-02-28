package com.huda.kickfoot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.momentkesh.kickfootball.R
import ru.momentkesh.kickfootball.databinding.FragmentGoBinding

class GoFragment : Fragment() {
    private var _binding: FragmentGoBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoBinding.inflate(inflater, container, false)
        events()
        return binding.root

    }

    private fun events() {
        binding.playButton.setOnClickListener {
            findNavController().navigate(R.id.play)
        }
        binding.exit.setOnClickListener {
            requireActivity().finish()
        }
    }













    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}