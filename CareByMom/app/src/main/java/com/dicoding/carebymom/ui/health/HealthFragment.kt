package com.dicoding.carebymom.ui.health

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.dicoding.carebymom.databinding.FragmentHealthBinding

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
        val healthViewModel =
            ViewModelProvider(this).get(HealthViewModel::class.java)

        _binding = FragmentHealthBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHealth
        healthViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}