package com.eljabali.joggingapplicationandroid.motivationalquotes

import com.google.gson.annotations.SerializedName

data class MotivationalQuotes(
    @SerializedName("content")
    val quote: String,
    @SerializedName("author")
    val author: String
)