package ramzi.eljabali.justjog.motivationalquotes

import com.google.gson.annotations.SerializedName

data class MotivationalQuote(
    @SerializedName("_id")
    val id: String,
    @SerializedName("content")
    val quote: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("tags")
    val tags: Array<String>,
    @SerializedName("authorSlug")
    val authorSlug: String,
    @SerializedName("length")
    val length: Int,
    @SerializedName("dateAdded")
    val dateAdded: String,
    @SerializedName("dateModified")
    val dateModified: String
)
