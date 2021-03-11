package vn.gas.thq.ui.vitri

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class ViTriKHRepositoty(private val apiService: ApiService) : BaseRepository() {
    suspend fun onGetListCustomer(query: String?, page: Int, size: Int) = flow {
        emit(apiService.queryCustomer(query, page, size))
    }

    suspend fun onGetDetailCustomer(id: String) = flow {
        emit(apiService.detailKH(id))
    }

    suspend fun updateToaDoKH(custId: String?, toaDoModel: ToaDoModel) = flow {
        emit(apiService.updateToaDoKH(custId, toaDoModel))
    }

    suspend fun getAllShop(query: String?) = flow {
        emit(apiService.getListShop(query))
    }

    suspend fun getSaleLine(query: String?) = flow {
        emit(apiService.getListSaleLine(query))
    }
}