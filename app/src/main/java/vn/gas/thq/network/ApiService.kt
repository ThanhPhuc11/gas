package vn.gas.thq.network

import retrofit2.http.*
import vn.gas.thq.model.*
import vn.gas.thq.ui.lapyeucauxuatkho.InitExportRequest
import vn.gas.thq.ui.retail.*
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

    //Tim kiem ycxk
    @GET("orders/shop")
    suspend fun searchRequest(
        @Query("staff_code") staff_code: String?,
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

    @GET("enums/sale-order-status")
    suspend fun saleOrderStatus(): List<StatusValueModel>

    @GET(" orders/sale")
    suspend fun searchRequestRetail(
        @Query("status") status: String?,
        @Query("from_date") from_date: String,
        @Query("to_date") to_date: String,
        @Query("offset") offset: Int,
        @Query("size") size: Int,
    ): List<BussinesRequestModel>

    @GET("orders/sale/{orderId}")
    suspend fun detailApproveLXBH(
        @Path("orderId") orderId: String?
    ): ApproveRequestModel

    //Thuc hien Ban le
    @POST("orders/sale/{orderId}/process")
    suspend fun doRetailLXBH(
        @Path("orderId") orderId: String?,
        @Body obj: GasRemainModel
    )

}