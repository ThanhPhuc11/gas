package vn.gas.thq.ui.lapyeucauxuatkho

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class LapYCXuatKhoRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun getDataFromShop() = flow {
        emit(apiService.getProductFromShop())
    }

    suspend fun onSubmitRequest(initExportRequest: InitExportRequest) = flow {
        emit(
            apiService.initExport(
                initExportRequest
            )
        )
    }
}