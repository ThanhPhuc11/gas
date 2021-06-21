package vn.gas.thq.ui.pheduyetgiabanle

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class PheDuyetGiaBanLeRepository(private val apiService: ApiService) : BaseRepository() {

    suspend fun getListStaff() = flow {
        emit(apiService.getListStaff())
    }

    suspend fun onGetListCustomer(lat: String?, lng: String?) = flow {
        emit(apiService.getListCustomer(lat, lng))
    }

    suspend fun onGetSaleOrderStatus() = flow {
        emit(apiService.saleOrderStatus())
    }

    suspend fun onSearchRetail(
        status: String?,
        staffCode: String?,
        custId: Int?,
        needMyInput: Boolean?,
        fromDate: String,
        toDate: String,
        page: Int
    ) = flow {
        emit(
            apiService.searchRequestRetail(
                1,
                status,
                staffCode,
                custId,
                needMyInput,
                fromDate,
                toDate,
                page,
                100
            )
        )
    }

    suspend fun detailBanLe(orderId: String?) = flow {
        emit(apiService.detailApproveLXBH(orderId))
    }

    suspend fun doAcceptDuyetGia(orderId: String?, obj: DuyetGiaModel) = flow {
        emit(apiService.doAcceptDuyetGia(orderId, obj))
    }

    suspend fun doRejectDuyetGia(orderId: String?, obj: DuyetGiaModel) = flow {
        emit(apiService.doRejectDuyetGia(orderId, obj))
    }

    suspend fun getHistoryAcceptRetail(orderId: Int) = flow {
        emit(apiService.getHistoryBanLe(orderId))
    }

    suspend fun onSearchRetailTDL(
        status: String?,
        staffCode: String?,
        fromDate: String,
        toDate: String,
        page: Int
    ) = flow {
        emit(apiService.searchRequestRetailTDL(3, status, staffCode, fromDate, toDate, page, 100))
    }

//    suspend fun detailTDL(orderId: String?) = flow {
//        emit(apiService.detailApproveLXBH(orderId))
//    }

    suspend fun doAcceptDuyetGiaTDL(orderId: String?, obj: DuyetGiaModel) = flow {
        emit(apiService.doAcceptDuyetGiaTDL(orderId, obj))
    }

    suspend fun doRejectDuyetGiaTDL(orderId: String?, obj: DuyetGiaModel) = flow {
        emit(apiService.doRejectDuyetGiaTDL(orderId, obj))
    }

    suspend fun commentBanLe(orderId: String?, commentModel: CommentModel) = flow {
        emit(apiService.commentBanLe(orderId, commentModel))
    }
}