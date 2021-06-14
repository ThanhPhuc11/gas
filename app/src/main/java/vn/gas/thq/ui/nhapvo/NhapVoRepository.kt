package vn.gas.thq.ui.nhapvo

import kotlinx.coroutines.flow.flow
import vn.gas.thq.network.ApiService

class NhapVoRepository(private val apiService: ApiService) {
    suspend fun getListShop(query: String?) = flow {
        emit(apiService.getListShop(query))
    }

    suspend fun getBienXe(query: String?) = flow {
        emit(apiService.getBienXe(query))
    }

    suspend fun getVo() = flow {
        emit(apiService.getTank())
    }

    suspend fun xuatnhapVo(shop_id: Int, license_plate_id: Int, transfer: List<VoModel>) = flow {
        emit(apiService.xuatnhapVo(shop_id, license_plate_id, transfer))
    }
}