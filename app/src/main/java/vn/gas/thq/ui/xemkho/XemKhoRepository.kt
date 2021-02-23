package vn.gas.thq.ui.xemkho

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class XemKhoRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun getDataFromShop(shop_code: String?, staff_code: String?) = flow {
        emit(apiService.getProductFromCode(shop_code, staff_code))
    }

    suspend fun getDSKho() = flow {
        emit(apiService.getKho())
    }
}