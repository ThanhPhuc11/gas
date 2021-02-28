package vn.gas.thq.network

import retrofit2.http.*
import vn.gas.thq.model.*
import vn.gas.thq.service.RegisterDeviceResponse
import vn.gas.thq.ui.kehoachbh.RequestKeHoachModel
import vn.gas.thq.ui.kiemkekho.KiemKeRequestModel
import vn.gas.thq.ui.lapyeucauxuatkho.InitExportRequest
import vn.gas.thq.ui.nhapkho.RequestNhapKho
import vn.gas.thq.ui.pheduyetgia.DuyetGiaModel
import vn.gas.thq.ui.qlyeucauduyetkehoach.KHBHOrderModel
import vn.gas.thq.ui.retail.*
import vn.gas.thq.ui.thukho.RequestDetailModel
import vn.gas.thq.ui.xemkho.KhoModel

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

    @POST("device")
    suspend fun registerFcmDevice(@Body requestFcmDeviceModel: RequestDeviceModel): RegisterDeviceResponse

    //Gia niem yet
    @GET("prices/today")
    suspend fun getGiaNiemYet(
        @Query("customer_id") customer_id: String,
        @Query("product_code") product_code: String,
        @Query("sale_trans_type") sale_type: String
    ): PriceModel


    @GET("stocks")
    suspend fun getProductFromCode(
        @Query("shop_code") shop_code: String?,
        @Query("staff_code") staff_code: String?
    ): List<ProductShopModel>

    @GET("stocks/shop")
    suspend fun getProductFromShop(): List<ProductShopModel>

    @GET("stocks/staff")
    suspend fun getProductFromStaff(): List<ProductShopModel>

    @GET("stocks/owner")
    suspend fun getKho(): List<KhoModel>

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

    @GET("staffs/customer")
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
        @Query("staff_code") staff_code: String?,
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

    @POST("orders/sale/{orderId}/accept")
    suspend fun doAcceptDuyetGia(
        @Path("orderId") orderId: String?,
        @Body obj: DuyetGiaModel
    )

    @POST("orders/sale/{orderId}/reject")
    suspend fun doRejectDuyetGia(
        @Path("orderId") orderId: String?,
        @Body obj: DuyetGiaModel
    )

    @POST("stocks/return")
    suspend fun nhapKho(@Body obj: RequestNhapKho)

    @POST("stocks/sync")
    suspend fun kiemKeKho(@Body obj: KiemKeRequestModel)

    @GET("prices/gas-remain")
    suspend fun gasRemainPrice(): PriceModel

    //Lap KH ban hang
    @POST("sale-plans")
    suspend fun lapKeHoachBH(@Body obj: RequestKeHoachModel)

    @GET("sale-plans")
    suspend fun getKeHoachBH(
        @Query("status") status: Int?,
        @Query("from_date") from_date: String,
        @Query("to_date") to_date: String,
        @Query("staff_code") staff_code: String?,
        @Query("shop_code") shop_code: String?,
        @Query("sale_line_code") sale_line_code: String?,
        @Query("offset") offset: Int,
        @Query("size") size: Int
    ): List<KHBHOrderModel>

}