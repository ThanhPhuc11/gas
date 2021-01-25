package vn.gas.thq.ui.xemkho

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class XemKhoRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun getDataFromShop() = flow {
        emit(apiService.getProductFromShop())
    }
}