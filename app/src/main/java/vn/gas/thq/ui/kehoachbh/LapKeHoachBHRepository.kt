package vn.gas.thq.ui.kehoachbh

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class LapKeHoachBHRepository(private val apiService: ApiService) : BaseRepository() {

    suspend fun onGetListCustomer(lat: String?, lng: String?) = flow {
        emit(apiService.getListCustomer(lat, lng))
    }

    suspend fun lapKeHoachBH(obj: RequestKeHoachModel) = flow {
        emit(apiService.lapKeHoachBH(obj))
    }
}