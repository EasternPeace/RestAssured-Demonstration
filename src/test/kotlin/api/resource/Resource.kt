package api.resource

import com.google.gson.annotations.SerializedName

data class Resource(
    val id: Int,
    val name: String,
    val year: Int,
    val color: String,
    @SerializedName("pantone_value")
    val pantoneValue: String
)