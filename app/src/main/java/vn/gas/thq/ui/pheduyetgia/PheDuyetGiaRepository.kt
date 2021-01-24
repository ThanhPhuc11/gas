package vn.gas.thq.ui.pheduyetgia

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class PheDuyetGiaRepository(private val apiService: ApiService) : BaseRepository() {

    suspend fun getListStaff() = flow {
        emit(apiService.getListStaff())
    }

    suspend fun onGetSaleOrderStatus() = flow {
        emit(apiService.saleOrderStatus())
    }

    suspend fun onSearchRetail(status: String?, fromDate: String, toDate: String) = flow {
        emit(apiService.searchRequestRetail(status, fromDate, toDate, 0, 100))
    }

    suspend fun detailApproveLXBH(orderId: String?) = flow {
        emit(apiService.detailApproveLXBH(orderId))
    }

    suspend fun doAcceptDuyetGia(orderId: String?, obj: DuyetGiaModel) = flow {
        emit(apiService.doAcceptDuyetGia(orderId, obj))
    }

    suspend fun doRejectDuyetGia(orderId: String?, obj: DuyetGiaModel) = flow {
        emit(apiService.doRejectDuyetGia(orderId, obj))
    }
}