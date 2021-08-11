package vn.gas.thq.ui.xuatkhoKH.qlxuatkhokh

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class QLXuatKhoKHRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun onGetListCustomer(query: String?, page: Int, size: Int) = flow {
        emit(apiService.queryCustomer(query, page, size))
    }

    suspend fun onSearchDirectTDL(
        customer_id: Int?,
        sale_order_type: Int,
        complete_order: Int?,
        fromDate: String,
        toDate: String,
        page: Int
    ) = flow {
        emit(
            apiService.searchRequestDirectTDL(
                customer_id,
                sale_order_type,
                complete_order,
                null,
                null,
                fromDate,
                toDate,
                page,
                100
            )
        )
    }
}