package com.dicoding.carebymom.ui.health

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.carebymom.databinding.FragmentHealthBinding
import com.dicoding.carebymom.ui.result.TestResultActivity

class HealthFragment : Fragment() {
    private var _binding: FragmentHealthBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHealthBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.checkUpButton.setOnClickListener{
            val intent = Intent(requireContext(), TestResultActivity::class.java)
            startActivity(intent)
        }
    }
}