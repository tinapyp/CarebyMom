package com.dicoding.carebymom.data.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("probability")
	val probability: Any? = null,

	@field:SerializedName("advice")
	val advice: String? = null,

	@field:SerializedName("prediction")
	val prediction: String? = null
)
