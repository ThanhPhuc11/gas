package vn.gas.thq.ui.qlyeucauduyetkehoach.chitiet

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class DetailKeHoachRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun getDetailKeHoachBH(id: String) = flow {
        emit(
            apiService.getDetailKHBH(id)
        )
    }
}