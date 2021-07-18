package vn.gas.thq.ui.trano.qltrano

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class QLTraNoRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun getDSKho() = flow {
        emit(apiService.getKho())
    }

    suspend fun historySangChiet(shop_id: Int, from_date: String, to_date: String) = flow {
        emit(apiService.historySangChiet(shop_id, from_date, to_date))
    }
}