package vn.gas.thq.ui.qlyeucaucanhan

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService
import vn.gas.thq.ui.lapyeucauxuatkho.InitExportRequest

class QLYCCaNhanRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun onSubmitRequest(status: String?, fromDate: String, toDate: String) = flow {
        emit(
            apiService.searchRequest(
                status, fromDate, toDate, 0, 100
            )
        )
    }

}