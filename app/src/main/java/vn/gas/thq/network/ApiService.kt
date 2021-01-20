package vn.gas.thq.network

import retrofit2.http.*
import vn.gas.thq.model.*
import vn.gas.thq.ui.lapyeucauxuatkho.InitExportRequest
import vn.gas.thq.ui.retail.Customer
import vn.gas.thq.ui.retail.RequestInitRetail
import vn.gas.thq.ui.retail.ResponseInitRetail
import vn.gas.thq.ui.thukho.RequestDetailModel

interface ApiService {
    @GET("staffs")
    suspend fun getUsers(): UserModel

    @FormUrlEncoded
    @POST("staff/oauth/token")
    suspend fun login(
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("grant_type") grant_type: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): TokenModel

    @GET("stocks/shop")
    suspend fun getProductFromShop(): List<ProductShopModel>

    @POST("orders/shop")
    suspend fun initExport(@Body obj: InitExportRequest): IdentityModel

    @GET("orders/shop")
    suspend fun searchRequest(
        @Query("status") status: String?,
        @Query("from_date") from_date: String,
        @Query("to_date") to_date: String,
        @Query("offset") offset: Int,
        @Query("size") size: Int,
    ): List<BussinesRequestModel>

    @GET("orders/shop/{orderId}")
    suspend fun detailRequest(
        @Path("orderId") orderId: String?
    ): RequestDetailModel

    @POST("orders/shop/{orderId}/accept")
    suspend fun accpetRequest(
        @Path("orderId") orderId: String?
    )

    @POST("orders/shop/{orderId}/reject")
    suspend fun rejectRequest(
        @Path("orderId") orderId: String?
    )

    @DELETE("orders/shop/{orderId}")
    suspend fun deleteRequest(
        @Path("orderId") orderId: String?
    )

    @GET("staffs/driver")
    suspend fun getListStaff(): List<UserModel>

    @GET("customers/nearby")
    suspend fun getListCustomer(
        @Query("lat") lat: String?,
        @Query("lng") lng: String?
    ): List<Customer>

    @POST("orders/sale")
    suspend fun doRequestRetail(@Body obj: RequestInitRetail): ResponseInitRetail
}