package vn.gas.thq.network

import kotlinx.coroutines.flow.Flow
import retrofit2.http.*
import vn.gas.thq.base.User
import vn.gas.thq.model.ResponseModel
import vn.gas.thq.ui.login.LoginModel

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>

    @FormUrlEncoded
    @POST("staff/oauth/token")
    suspend fun login(
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("grant_type") grant_type: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginModel
}