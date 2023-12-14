package com.dicoding.carebymom.ui.register

import androidx.lifecycle.ViewModel
import com.dicoding.carebymom.data.response.RegisterResponse
import com.dicoding.carebymom.repo.UserRepository

class RegisterViewModel(private var repository: UserRepository) : ViewModel() {
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return repository.register(name, email, password)
    }
}
