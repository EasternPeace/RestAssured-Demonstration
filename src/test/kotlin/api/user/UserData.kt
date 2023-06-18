package api.user

import com.google.gson.annotations.SerializedName

data class UserData(
    val avatar: String,
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    val id: Int,
    @SerializedName("last_name")
    val lastName: String
)

data class UserRegister(
    val email: String,
    val password: String
)

data class UserRegisterSuccess(
    val id: Int,
    val token: String
)

data class UserRegisterError(
    val error: String
)