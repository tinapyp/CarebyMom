package com.dicoding.carebymom.UI_for_apps.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.carebymom.data.model.UserModel
import com.dicoding.carebymom.repo.UserRepository

class HomeViewModel (private var repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

}