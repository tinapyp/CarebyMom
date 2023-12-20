package com.dicoding.carebymom.UI_for_apps.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.dicoding.carebymom.R
import com.dicoding.carebymom.data.response.RegisterResponse
import com.dicoding.carebymom.databinding.ActivityRegisterBinding
import com.dicoding.carebymom.UI_for_apps.ViewModelFactory
import com.dicoding.carebymom.UI_for_apps.login.LoginActivity
import com.dicoding.carebymom.utils.DatePickerFragment
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    private var periodDateMillis: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.loginBtn.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.signUpButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val repassword = binding.repasswordEditText.text.toString()

            val periodTimeRes = periodDateMillis.toBigInteger()
            Log.e("periodTime", periodTimeRes.toString())

            if (username.isEmpty()) {
                binding.usernameEditTextLayout.error = getString(R.string.username_cannot_be_empty)
            } else if (email.isEmpty()) {
                binding.emailEditTextLayout.error = getString(R.string.email_cannot_be_empty)
            } else if (password.isEmpty()) {
                binding.passwordEditTextLayout.error = getString(R.string.password_cannot_be_empty)
            } else if (repassword.isEmpty()) {
                binding.repasswordEditTextLayout.error =
                    getString(R.string.re_confirm_password_cannot_be_empty)
            } else if (password != repassword) {
                binding.repasswordEditTextLayout.error = getString(R.string.password_do_not_match)
            } else {
                showLoading(true)
                lifecycleScope.launch {
                    try {
                        val response = viewModel.register(username, email, password, periodTimeRes)
                        showLoading(false)
                        showToast(response.message)
                        AlertDialog.Builder(this@RegisterActivity).apply {
                            setMessage(getString(R.string.register_success))
                            setPositiveButton(getString(R.string.continuee)) { _, _ ->
                                val intent = Intent(context, LoginActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    } catch (e: HttpException) {
                        showLoading(false)
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                        showToast(errorResponse.message)
                    }
                }
            }
        }
    }

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.lastPeriodDate.text = dateFormat.format(calendar.time)

        periodDateMillis = calendar.timeInMillis
        Log.e("periodTime(2)", periodDateMillis.toString())
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.signUpButton.isEnabled = !isLoading
    }
}