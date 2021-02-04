package vn.gas.thq.ui.nhapkho

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class NhapKhoRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun getListStaff() = flow {
        emit(apiService.getListStaff())
    }

    suspend fun getProductFromCode(shop_code: String?, staff_code: String?) = flow {
        emit(apiService.getProductFromCode(shop_code, staff_code))
    }

    suspend fun nhapKho(obj: RequestNhapKho) = flow {
        emit(apiService.nhapKho(obj))
    }

    suspend fun gasRemainPrice() = flow {
        emit(apiService.gasRemainPrice())
    }
}