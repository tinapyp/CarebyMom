package com.dicoding.carebymom.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tips(
    var name: String,
    var description: String,
    var photo: Int,
): Parcelable