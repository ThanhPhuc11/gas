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
}