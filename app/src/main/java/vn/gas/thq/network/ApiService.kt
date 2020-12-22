package vn.gas.thq.network

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.POST
import vn.gas.thq.base.User
import vn.gas.thq.model.ResponseModel
import vn.gas.thq.ui.login.LoginModel

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>

    @POST("staff/oauth/token")
    suspend fun login(): Flow<ResponseModel<LoginModel>>
}