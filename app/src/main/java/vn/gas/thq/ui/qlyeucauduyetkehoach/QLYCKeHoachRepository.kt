package vn.gas.thq.ui.qlyeucauduyetkehoach

import kotlinx.coroutines.flow.flow
import retrofit2.http.Query
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService
import vn.gas.thq.ui.kehoachbh.RequestKeHoachModel

class QLYCKeHoachRepository(private val apiService: ApiService) : BaseRepository() {

    suspend fun getAllShop() = flow {
        emit(apiService.getListShopByStaff())
    }

    suspend fun getSaleLine(query: String?) = flow {
        emit(apiService.getListSaleLine(query))
    }

    suspend fun getKeHoachBH(
        status: Int?,
        from_date: String,
        to_date: String,
        staff_code: String?,
        shop_code: String?,
        sale_line_code: String?,
        offset: Int,
        size: Int
    ) = flow {
        emit(
            apiService.getKeHoachBH(
                status,
                from_date,
                to_date,
                staff_code,
                shop_code,
                sale_line_code,
                offset,
                size
            )
        )
    }
}