package com.dicoding.carebymom.UI_for_apps.health

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.carebymom.R
import com.dicoding.carebymom.UI_for_apps.ViewModelFactory
import com.dicoding.carebymom.UI_for_apps.result.TestResultActivity
import com.dicoding.carebymom.databinding.FragmentHealthBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

class HealthFragment : Fragment() {
    private var _binding: FragmentHealthBinding? = null
    private val viewModel by viewModels<HealthViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val binding get() = _binding!!
    private val currentDate =
        SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault()).format(Date())
    val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
    val date = dateFormat.parse(currentDate)
    @RequiresApi(Build.VERSION_CODES.O)
    val localDate = date?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHealthBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSession().observe(requireActivity()) { user ->
            val sessionDate =
                Instant.ofEpochMilli(user.periodTime.toLong()).atZone(ZoneId.systemDefault())
                    .toLocalDate()

            val pregnancyAge = ChronoUnit.DAYS.between(sessionDate, localDate).toInt()
            binding.PregnancyAgeEditText.text = Editable.Factory.getInstance().newEditable(pregnancyAge.toString())

        }

        setupAction()
    }

    private fun setupAction() {

        binding.armHelp.setOnClickListener {
            val intent = Intent(requireContext(), HealthHelpActivity::class.java)
            intent.putExtra("idHelp", "arm")
            startActivity(intent)
        }

        binding.fundusHelp.setOnClickListener {
            val intent = Intent(requireContext(), HealthHelpActivity::class.java)
            intent.putExtra("idHelp", "fundus")
            startActivity(intent)
        }

        binding.hearthelp.setOnClickListener {
            val intent = Intent(requireContext(), HealthHelpActivity::class.java)
            intent.putExtra("idHelp", "heart")
            startActivity(intent)
        }

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
                    val intent = Intent(requireContext(), TestResultActivity::class.java)
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