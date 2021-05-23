package vn.gas.thq.ui.nghiphep.lichsu

import kotlinx.coroutines.flow.flow
import vn.gas.thq.network.ApiService

class LSNghiPhepRepository(private val apiService: ApiService) {
    suspend fun getVacation(staffId: Int, fromDate: String, toDate: String) = flow {
        emit(apiService.getVacation(staffId, fromDate, toDate))
    }

    suspend fun getStaffFromShopId(shopId: Int) = flow {
        emit(apiService.getStaffByShopId(shopId))
    }
}