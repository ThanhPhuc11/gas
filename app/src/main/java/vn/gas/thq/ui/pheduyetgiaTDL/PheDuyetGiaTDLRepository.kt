package vn.gas.thq.ui.pheduyetgiaTDL

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService
import vn.gas.thq.ui.pheduyetgiabanle.DuyetGiaModel

class PheDuyetGiaTDLRepository(private val apiService: ApiService) : BaseRepository() {

    suspend fun getListStaff() = flow {
        emit(apiService.getListStaff())
    }

    suspend fun onGetSaleOrderStatus() = flow {
        emit(apiService.saleOrderTDLStatus())
    }

    suspend fun getHistoryAcceptRetail(orderId: Int) = flow {
        emit(apiService.getHistoryAcceptRetail(orderId))
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

    suspend fun detailTDL(orderId: String?) = flow {
        emit(apiService.detailApproveLXBH(orderId))
    }

    suspend fun doAcceptDuyetGiaTDL(orderId: String?, obj: DuyetGiaModel) = flow {
        emit(apiService.doAcceptDuyetGiaTDL(orderId, obj))
    }

    suspend fun doRejectDuyetGiaTDL(orderId: String?, obj: DuyetGiaModel) = flow {
        emit(apiService.doRejectDuyetGiaTDL(orderId, obj))
    }
}