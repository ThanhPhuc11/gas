package vn.gas.thq.ui.vitri

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class ViTriKHRepositoty(private val apiService: ApiService) : BaseRepository() {
    suspend fun onGetListCustomer(lat: String?, lng: String?) = flow {
        emit(apiService.getListCustomer(lat, lng))
    }

    suspend fun updateToaDoKH(custId: String?, toaDoModel: ToaDoModel) = flow {
        emit(apiService.updateToaDoKH(custId, toaDoModel))
    }
}