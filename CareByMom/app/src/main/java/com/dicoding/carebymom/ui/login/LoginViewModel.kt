package com.dicoding.carebymom.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.carebymom.data.response.LoginResponse
import com.dicoding.carebymom.pref.UserModel
import com.dicoding.carebymom.repo.UserRepository

class LoginViewModel(private var repository: UserRepository) : ViewModel() {

    val isLoading: LiveData<Boolean>
        get() = repository.isLoading

    val loginResponse: LiveData<LoginResponse>
        get() = repository.loginResponse

    fun login(email: String, password: String) {
        repository.login(email, password)
    }

    suspend fun saveSession(user: UserModel) {
        repository.saveSession(user)
    }

}