package com.dicoding.carebymom.data.model

data class UserModel(
    val username: String,
    val token: String,
    val isLogin: Boolean = false
)