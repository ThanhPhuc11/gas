package vn.gas.thq.ui.home

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.model.RequestDeviceModel
import vn.gas.thq.network.ApiService

class FcmHomeRepository(private val apiService: ApiService) : BaseRepository() {

    suspend fun registerFcmDevice(requestFcmDeviceModel: RequestDeviceModel) =
        flow { emit(apiService.registerFcmDevice(requestFcmDeviceModel)) }
}