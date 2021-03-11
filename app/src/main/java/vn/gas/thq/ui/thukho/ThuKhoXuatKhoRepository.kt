package vn.gas.thq.ui.thukho

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class ThuKhoXuatKhoRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun onSearchRequest(
        staffCode: String?,
        status: String?,
        fromDate: String,
        toDate: String,
        offSet: Int,
        size: Int
    ) = flow {
        emit(
            apiService.searchRequest(
                staffCode, status, fromDate, toDate, offSet, size
            )
        )
    }

    suspend fun onDetailRequest(orderId: String?) = flow {
        emit(
            apiService.detailRequest(orderId)
        )
    }

    suspend fun onAcceptRequest(orderId: String?) = flow {
        emit(
            apiService.accpetRequest(orderId)
        )
    }

    suspend fun onRejectRequest(orderId: String?) = flow {
        emit(
            apiService.rejectRequest(orderId)
        )
    }

    suspend fun getListStaff() = flow {
        emit(
            apiService.getListStaff()
        )
    }

}