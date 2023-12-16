package com.dicoding.carebymom.UI_for_apps.health

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.carebymom.R
import com.dicoding.carebymom.UI_for_apps.ViewModelFactory
import com.dicoding.carebymom.UI_for_apps.result.TestResultActivity
import com.dicoding.carebymom.databinding.FragmentHealthBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HealthFragment : Fragment() {
    private var _binding: FragmentHealthBinding? = null
    private val viewModel by viewModels<HealthViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

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

        setupAction()
    }

    private fun setupAction() {
        binding.checkUpButton.setOnClickListener {
            showLoading(true)
            val motherAge = binding.AgeEditText.text.toString()
            val pregnantDuration = binding.PregnancyAgeEditText.text.toString()
            val weight = binding.WeightEditText.text.toString()
            val height = binding.HeightEditText.text.toString()
            val armCircum = binding.ArmCircumEditText.text.toString()
            val fundusHeight = binding.AbdominalCircumferenceEditText.text.toString()
            val heartRate = binding.HeartBeatEditText.text.toString()

            val bmi = countBMI(weight.toDouble(), height.toDouble()).toInt()

            if (motherAge.isEmpty()) {
                binding.AgeEditText.error = getString(R.string.this_field_cannot_be_empty)
            } else if (pregnantDuration.isEmpty()) {
                binding.PregnancyAgeEditText.error = getString(R.string.this_field_cannot_be_empty)
            } else if (weight.isEmpty()) {
                binding.WeightEditText.error = getString(R.string.this_field_cannot_be_empty)
            } else if (armCircum.isEmpty()) {
                binding.ArmCircumEditText.error = getString(R.string.this_field_cannot_be_empty)
            } else if (fundusHeight.isEmpty()) {
                binding.AbdominalCircumferenceEditText.error =
                    getString(R.string.this_field_cannot_be_empty)
            } else if (heartRate.isEmpty()) {
                binding.HeartBeatEditText.error = getString(R.string.this_field_cannot_be_empty)
            }

            lifecycleScope.launch {
                try {
                    val response = viewModel.checkup(
                        motherAge.toDouble(),
                        pregnantDuration.toDouble(),
                        weight.toDouble(),
                        height.toDouble(),
                        bmi.toDouble(),
                        armCircum.toDouble(),
                        fundusHeight.toDouble(),
                        heartRate.toDouble()
                    )
                    showLoading(false)
                    val intent = Intent(requireContext(),TestResultActivity::class.java)
                    val advice = response.advice
                    intent.putExtra("advice", advice)
                    startActivity(intent)

                } catch (e: HttpException) {
                    showLoading(false)
//                    val errorBody = e.response()?.errorBody()?.string()
//                    val errorResponse = Gson().fromJson(errorBody, PredictResponse::class.java)
//                    showToast("errorResponse.message")
                    showToast("Something wrong, please try again")
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.checkUpButton.isEnabled = !isLoading
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun countBMI(weight: Double, height: Double): Double {
        return weight / ((height / 100) * (height / 100))
    }
}