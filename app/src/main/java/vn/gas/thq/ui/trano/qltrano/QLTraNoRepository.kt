package vn.gas.thq.ui.trano.qltrano

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class QLTraNoRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun onGetListCustomer(query: String?, page: Int, size: Int) = flow {
        emit(apiService.queryCustomer(query, page, size))
    }

    suspend fun historyTraNo(cust_id: Int?, debit_type: String?, from_date: String, to_date: String, page: Int, size: Int) =
        flow {
            emit(apiService.historyTraNo(cust_id, debit_type, from_date, to_date, page, size))
        }
}