package com.vladimirorlov.hackeruapp.api

import com.google.gson.annotations.SerializedName

data class ApiImage(@SerializedName("largeImageURL") val  imageUrl: String)
data class ApiResponse(@SerializedName("hits") val  imagesList: List<ApiImage>)
