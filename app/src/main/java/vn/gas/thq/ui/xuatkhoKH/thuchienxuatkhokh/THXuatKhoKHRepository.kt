package vn.gas.thq.ui.xuatkhoKH.thuchienxuatkhokh

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class THXuatKhoKHRepository(private val apiService: ApiService) : BaseRepository() {
//    suspend fun onGetListCustomer(query: String?, page: Int, size: Int) = flow {
//        emit(apiService.queryCustomer(query, page, size))
//    }

    suspend fun xuatHang(
        orderId: String?, obj: XuatHangModel
    ) = flow {
        emit(
            apiService.xuatHang(orderId, obj)
        )
    }
}