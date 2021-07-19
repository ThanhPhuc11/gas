package vn.gas.thq.ui.trano.nhaptrano

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class NhapTraNoRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun onGetListCustomer(query: String?, page: Int, size: Int) = flow {
        emit(apiService.queryCustomer(query, page, size))
    }
}