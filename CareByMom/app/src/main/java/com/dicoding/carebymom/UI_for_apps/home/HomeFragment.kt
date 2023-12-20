package com.dicoding.carebymom.UI_for_apps.home

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.dicoding.carebymom.R
import com.dicoding.carebymom.UI_for_apps.ViewModelFactory
import com.dicoding.carebymom.UI_for_apps.health.HealthFragment
import com.dicoding.carebymom.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {


    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val currentDate =
        SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault()).format(Date())

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDate.text = currentDate

        val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        val date = dateFormat.parse(currentDate)
        val localDate = date?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()

        viewModel.getSession().observe(requireActivity()) { user ->
            if (user.periodTime.isNotEmpty()) {
                val sessionDate =
                    Instant.ofEpochMilli(user.periodTime.toLong()).atZone(ZoneId.systemDefault())
                        .toLocalDate()

                val pregnancyAge = ChronoUnit.DAYS.between(sessionDate, localDate).toInt()

                binding.tvDays.text = getString(R.string.days, pregnancyAge.toString())

                binding.tvGrowth.text = when (pregnancyAge) {
                    in 0 until 14 -> getString(R.string.week1_2)
                    in 14 until 28 -> getString(R.string.week3_4)
                    in 28 until 56 -> getString(R.string.week5_8)
                    in 56 until 84 -> getString(R.string.week9_12)
                    in 84 until 112 -> getString(R.string.week13_16)
                    in 112 until 140 -> getString(R.string.week17_20)
                    in 140 until 168 -> getString(R.string.week21_24)
                    in 168 until 182 -> getString(R.string.week25_26)
                    in 182 until 210 -> getString(R.string.week27_30)
                    in 210 until 238 -> getString(R.string.week31_34)
                    in 238 until 266 -> getString(R.string.week35_38)
                    else -> getString(R.string.week39_40)
                }
            }
        }
    }



}