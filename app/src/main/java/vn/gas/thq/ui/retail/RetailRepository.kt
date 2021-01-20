package vn.gas.thq.ui.retail

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class RetailRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun onGetListCustomer(lat: String?, lng: String?) = flow {
        emit(apiService.getListCustomer(lat, lng))
    }

    suspend fun doRequestRetail(obj: RequestInitRetail) = flow {
        emit(apiService.doRequestRetail(obj))
    }
}