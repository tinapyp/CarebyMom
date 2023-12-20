package com.dicoding.carebymom.UI_for_apps.register

import androidx.lifecycle.ViewModel
import com.dicoding.carebymom.data.response.RegisterResponse
import com.dicoding.carebymom.repo.UserRepository
import java.math.BigInteger

class RegisterViewModel(private var repository: UserRepository) : ViewModel() {
    suspend fun register(name: String, email: String, password: String, periodTime:BigInteger): RegisterResponse {
        return repository.register(name, email, password, periodTime)
    }

}
