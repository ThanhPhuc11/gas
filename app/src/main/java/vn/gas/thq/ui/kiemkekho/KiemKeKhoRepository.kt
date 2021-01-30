package vn.gas.thq.ui.kiemkekho

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class KiemKeKhoRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun getDataFromCode(shop_code: String?, staff_code: String?) = flow {
        emit(apiService.getProductFromCode(shop_code, staff_code))
    }
}