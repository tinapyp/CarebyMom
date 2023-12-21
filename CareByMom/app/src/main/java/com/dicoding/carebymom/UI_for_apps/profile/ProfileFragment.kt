package com.dicoding.carebymom.UI_for_apps.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.dicoding.carebymom.R
import android.provider.Settings
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

        binding.languageButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        viewModel.getSession().observe(requireActivity()) { user ->
            binding.tvUsername.text = user.username
        }

        binding.logoutButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setMessage(getString(R.string.are_you_sure_want_to_logout))
            builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                viewModel.logout()
                dialog.dismiss()
            }
            builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

}