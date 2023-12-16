package com.dicoding.carebymom.UI_for_apps.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dicoding.carebymom.databinding.FragmentProfileBinding
import com.dicoding.carebymom.UI_for_apps.ViewModelFactory
import com.dicoding.carebymom.UI_for_apps.main.MainViewModel

class ProfileFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSession().observe(requireActivity()) { user ->
            binding.tvUsername.text = user.username
        }

        binding.logoutButton.setOnClickListener{
            viewModel.logout()
        }
    }

}