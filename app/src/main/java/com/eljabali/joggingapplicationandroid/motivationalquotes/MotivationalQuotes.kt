package com.eljabali.joggingapplicationandroid.motivationalquotes

import com.google.gson.annotations.SerializedName

data class MotivationalQuotes(
    @SerializedName("text")
    val quote: String,
    val author: String
)