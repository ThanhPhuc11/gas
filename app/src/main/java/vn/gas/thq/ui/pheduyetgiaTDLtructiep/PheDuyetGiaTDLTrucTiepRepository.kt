package vn.gas.thq.ui.pheduyetgiaTDLtructiep

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService
import vn.gas.thq.ui.pheduyetgiabanle.DuyetGiaModel

class PheDuyetGiaTDLTrucTiepRepository(private val apiService: ApiService) : BaseRepository() {

    suspend fun getListStaff() = flow {
        emit(apiService.getListStaff())
    }

    suspend fun onGetSaleOrderStatus() = flow {
        emit(apiService.saleOrderTDLStatus())
    }

    suspend fun getHistoryAcceptRetail(orderId: Int) = flow {
        emit(apiService.getHistoryAcceptRetail(orderId))
    }

    suspend fun onSearchDirectTDL(
        status: String?,
        staffCode: String?,
        fromDate: String,
        toDate: String,
        page: Int
    ) = flow {
        emit(apiService.searchRequestDirectTDL(4, status, staffCode, fromDate, toDate, page, 100))
    }

    suspend fun detailTDL(orderId: String?) = flow {
        emit(apiService.detailApproveLXBH(orderId))
    }

    suspend fun doAcceptDuyetGiaTDL(orderId: String?, obj: DuyetGiaModel) = flow {
        emit(apiService.doAcceptDirectTDL(orderId, obj))
    }

    suspend fun doRejectDuyetGiaTDL(orderId: String?, obj: DuyetGiaModel) = flow {
        emit(apiService.doRejectDirectTDL(orderId, obj))
    }
}