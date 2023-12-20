package com.dicoding.carebymom.data.model

data class UserModel(
    val periodTime: String,
    val id: String,
    val email: String,
    val username: String,
    val token: String,
    val isLogin: Boolean = false
)