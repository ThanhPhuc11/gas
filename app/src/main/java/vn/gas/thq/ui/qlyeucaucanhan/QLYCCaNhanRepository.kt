package vn.gas.thq.ui.qlyeucaucanhan

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class QLYCCaNhanRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun onSubmitRequest(
        staffCode: String?,
        status: String?,
        fromDate: String,
        toDate: String,
        offSet: Int,
        size: Int
    ) = flow {
        emit(apiService.searchRequest(staffCode, status, fromDate, toDate, offSet, size))
    }

    suspend fun onCancelRequest(orderId: String?) = flow {
        emit(apiService.deleteRequest(orderId))
    }

    // Ban le
    suspend fun onSearchRetail(
        status: String?,
        staffCode: String?,
        fromDate: String,
        toDate: String,
        offSet: Int,
        size: Int
    ) = flow {
        emit(apiService.searchRequestRetail(status, staffCode, fromDate, toDate, offSet, size))
    }

    suspend fun onGetSaleOrderStatus() = flow {
        emit(apiService.saleOrderStatus())
    }

    suspend fun detailApproveLXBH(orderId: String?) = flow {
        emit(apiService.detailApproveLXBH(orderId))
    }

}