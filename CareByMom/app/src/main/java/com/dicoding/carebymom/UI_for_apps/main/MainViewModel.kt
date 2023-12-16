package com.dicoding.carebymom.UI_for_apps.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.carebymom.data.model.UserModel
import com.dicoding.carebymom.repo.UserRepository
import kotlinx.coroutines.launch

class MainViewModel( private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}