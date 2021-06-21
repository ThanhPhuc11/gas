package vn.gas.thq.network

import retrofit2.http.*
import vn.gas.thq.model.*
import vn.gas.thq.service.RegisterDeviceResponse
import vn.gas.thq.ui.kehoachbh.RequestKeHoachModel
import vn.gas.thq.ui.kiemkekho.KiemKeRequestModel
import vn.gas.thq.ui.lapyeucauxuatkho.InitExportRequest
import vn.gas.thq.ui.nghiphep.VacationModel
import vn.gas.thq.ui.nhapkho.ProductShopNhapKhoModel
import vn.gas.thq.ui.nhapkho.RequestNhapKho
import vn.gas.thq.ui.pheduyetgiabanle.CommentModel
import vn.gas.thq.ui.pheduyetgiabanle.DuyetGiaModel
import vn.gas.thq.ui.pheduyetgiabanle.HistoryModel
import vn.gas.thq.ui.qlyeucauduyetkehoach.KHBHOrderModel
import vn.gas.thq.ui.qlyeucauduyetkehoach.chitiet.DetailKHBHModel
import vn.gas.thq.ui.qlyeucauduyetkehoach.chitiet.DetailTypeKHBHModel
import vn.gas.thq.ui.retail.*
import vn.gas.thq.ui.retailtongdaily.FeeVanChuyenModel
import vn.gas.thq.ui.sangchiet.nhapsangchiet.AvailableKHLResponse
import vn.gas.thq.ui.sangchiet.nhapsangchiet.InitSangChiet
import vn.gas.thq.ui.sangchiet.qlsangchiet.HistorySangChietModel
import vn.gas.thq.ui.thukho.RequestDetailModel
import vn.gas.thq.ui.vitri.ListResponseCustomer
import vn.gas.thq.ui.vitri.SaleLineModel
import vn.gas.thq.ui.vitri.ShopModel
import vn.gas.thq.ui.vitri.ToaDoModel
import vn.gas.thq.ui.xemkho.KhoModel

interface ApiService {
    @GET("staffs")
    suspend fun getUsers(): UserModel

    @GET("staffs/permissions")
    suspend fun getPermission(): List<String>

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

    @GET("price/plan/agency/")
    suspend fun getGiaTongDaiLy(
        @Query("product_code") product_code: String,
        @Query("cust_id") cust_id: Int
    ): PriceModel

    @POST("orders/agent/transport")
    suspend fun getPhiVanChuyen(@Body obj: RequestInitRetail): FeeVanChuyenModel


    @GET("stocks")
    suspend fun getProductFromCode(
        @Query("shop_code") shop_code: String?,
        @Query("staff_code") staff_code: String?
    ): List<ProductShopNhapKhoModel> // change ProductShopModel -> ProductShopNhapKhoModel

    @GET("stocks")
    suspend fun getProductNhapKhoFromCode(
        @Query("shop_code") shop_code: String?,
        @Query("staff_code") staff_code: String?
    ): List<ProductShopNhapKhoModel>

    @GET("shop-orders/stocks/shop")
    suspend fun getProductFromShop(): List<ProductShopModel>

    @GET("stocks/staff")
    suspend fun getProductFromStaff(): List<ProductShopModel>

    @GET("stocks/owner")
    suspend fun getKho(): List<KhoModel>

    @POST("shop-orders")
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

    @GET("staffs/customer/agent")
    suspend fun getListCustomerBoss(
        @Query("lat") lat: String?,
        @Query("lng") lng: String?
    ): List<Customer>

    @POST("customers/{customerId}/coordinate")
    suspend fun updateToaDoKH(@Path("customerId") customerId: String?, @Body toaDo: ToaDoModel)

    @POST("sale-orders")
    suspend fun doRequestRetail(@Body obj: RequestInitRetail): ResponseInitRetail

    @POST("agent-orders")
    suspend fun doRequestRetailBoss(@Body obj: RequestInitRetail): ResponseInitRetail

    @GET("enums/sale-order-status")
    suspend fun saleOrderStatus(): List<StatusValueModel>


    @GET("agent-orders")
    suspend fun searchRequestRetailTDL(
        @Query("sale_order_type") sale_order_type: Int,
        @Query("status") status: String?,
        @Query("staff_code") staff_code: String?,
        @Query("from_date") from_date: String,
        @Query("to_date") to_date: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): ResponseModel<BussinesRequestModel>

    @POST("orders/agent/{orderId}/accept")
    suspend fun doAcceptDuyetGiaTDL(
        @Path("orderId") orderId: String?,
        @Body obj: DuyetGiaModel
    )

    @POST("orders/agent/{orderId}/reject")
    suspend fun doRejectDuyetGiaTDL(
        @Path("orderId") orderId: String?,
        @Body obj: DuyetGiaModel
    )


    //Chi tiet yc TDL
    @GET("agent-orders/{orderId}")
    suspend fun detailApproveTDLLXBH(
        @Path("orderId") orderId: String?
    ): ApproveRequestModel

    //Thuc hien Ban le
    @POST("sale-orders/{orderId}/process")
    suspend fun doRetailLXBH(
        @Path("orderId") orderId: String?,
        @Body obj: GasRemainModel
    )

    @POST("orders/agent/{orderId}/process")
    suspend fun doRetailTDLLXBH(
        @Path("orderId") orderId: String?,
        @Body obj: GasRemainModel
    )

    @POST("sale-orders/{orderId}/accept")
    suspend fun doAcceptDuyetGia(
        @Path("orderId") orderId: String?,
        @Body obj: DuyetGiaModel
    )

    @POST("sale-orders/{orderId}/reject")
    suspend fun doRejectDuyetGia(
        @Path("orderId") orderId: String?,
        @Body obj: DuyetGiaModel
    )

    @GET("orders/sale/history")
    suspend fun getHistoryAcceptRetail(@Query("sale_order_id") sale_order_id: Int): List<HistoryModel>

    @POST("stocks/return")
    suspend fun nhapKho(@Body obj: RequestNhapKho)

    @POST("stocks/sync")
    suspend fun kiemKeKho(@Body obj: KiemKeRequestModel)

    @GET("prices/gas-remain")
    suspend fun gasRemainPrice(): PriceModel

    @GET("prices/gas-remain")
    suspend fun gasRemainPriceNhaoKho(@Query("staff_code") staff_code: String): PriceModel

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
    ): ResponseModel<KHBHOrderModel>

    @GET("sale-plans/{Id}")
    suspend fun getDetailKHBH(@Path("Id") Id: String): DetailKHBHModel

    @GET("shops")
    suspend fun getListShop(
        @Query("query") query: String?
    ): List<ShopModel>

    @GET("staffs/shops")
    suspend fun getListShopByStaff(
//        @Query("query") query: String?
    ): List<ShopModel>

    @GET("sale-lines")
    suspend fun getListSaleLine(
        @Query("query") query: String?
    ): List<SaleLineModel>

    @GET("customers")
    suspend fun queryCustomer(
        @Query("query") query: String?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ListResponseCustomer

    @GET("customers/{id} ")
    suspend fun detailKH(@Path("id") id: String): Customer

    @POST("sale-plans/{salePlanId}/accept")
    suspend fun duyetKHBH(@Path("salePlanId") salePlanId: String, @Body type: DetailTypeKHBHModel)

    @POST("sale-plans/{salePlanId}/reject")
    suspend fun tuChoiKHBH(@Path("salePlanId") salePlanId: String, @Body type: DetailTypeKHBHModel)

    @GET("stocks/gas/amount")
    suspend fun getAvailableKHL(): AvailableKHLResponse

    @GET("stocks/gas/check-transfer")
    suspend fun checkTransfer(): Boolean

    @POST("stocks/gas/transfer")
    suspend fun initSangChiet(@Body obj: InitSangChiet)

    @GET("stocks/gas/history")
    suspend fun historySangChiet(
        @Query("shop_id") shop_id: Int,
        @Query("from_date") from_date: String,
        @Query("to_date") to_date: String
    ): List<HistorySangChietModel>

    @GET("vacations")
    suspend fun getVacation(
        @Query("staff_id") staff_id: Int,
        @Query("from_date") from_date: String,
        @Query("to_date") to_date: String
    ): List<VacationModel>

    @PUT("vacations")
    suspend fun registerVacation(
        @Query("staff_id") staff_id: Int,
        @Query("from_date") from_date: String,
        @Query("to_date") to_date: String,
        @Query("reason") reason: String
    )

    @GET("shops/{shopId}/staffs")
    suspend fun getStaffByShopId(
        @Path("shopId") shopId: Int
    ): List<UserModel>

    // TODO: Tong hop cac Api Ban le new

    //ban le
    @GET("sale-orders")
    suspend fun searchRequestRetail(
        @Query("sale_order_type") sale_order_type: Int,
        @Query("status") status: String?,
        @Query("staff_code") staff_code: String?,
        @Query("customer_id") customer_id: Int?,
        @Query("need_my_input") need_my_input: Boolean?,
        @Query("from_date") from_date: String,
        @Query("to_date") to_date: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): ResponseModel<BussinesRequestModel>

    //Chi tiet ban le
    @GET("sale-orders/{orderId}")
    suspend fun detailApproveLXBH(
        @Path("orderId") orderId: String?
    ): ApproveRequestModel

    //Comment
    @POST("sale-orders/{orderId}/comment")
    suspend fun commentBanLe(
        @Path("orderId") orderId: String?,
        @Body commentModel: CommentModel
    )

    //History Ban le new
    @GET("sale-orders/history")
    suspend fun getHistoryBanLe(@Query("sale_order_id") sale_order_id: Int): List<HistoryModel>
}