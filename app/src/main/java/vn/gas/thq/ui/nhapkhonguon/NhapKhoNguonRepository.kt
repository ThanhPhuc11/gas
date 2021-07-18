package vn.gas.thq.ui.nhapkhonguon

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class NhapKhoNguonRepository(private val apiService: ApiService) : BaseRepository() {

//    suspend fun getShopByStaff() = flow {
//        emit(apiService.getListShopByStaff())
//    }

    suspend fun getListShop(query: String?) = flow {
        emit(apiService.getListShop(query))
    }

    suspend fun getBienXe(query: String?) = flow {
        emit(apiService.getBienXe(query))
    }

    suspend fun initNhapGasNguon(obj: InitNhapGasNguon) = flow {
        emit(apiService.initNhapGasNguon(obj))
    }
}