package vn.gas.thq.ui.nghiphep.dknghi

import kotlinx.coroutines.flow.flow
import vn.gas.thq.network.ApiService

class DKNghiPhepRepository(private val apiService: ApiService) {

    suspend fun registerVacation(staffId: Int, fromDate: String, toDate: String, reason: String) =
        flow {
            emit(apiService.registerVacation(staffId, fromDate, toDate, reason))
        }

    suspend fun getStaffFromShopId(shopId: Int) = flow {
        emit(apiService.getStaffByShopId(shopId))
    }
}