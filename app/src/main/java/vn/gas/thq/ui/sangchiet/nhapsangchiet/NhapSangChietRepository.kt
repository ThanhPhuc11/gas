package vn.gas.thq.ui.sangchiet.nhapsangchiet

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class NhapSangChietRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun getAvailableKHL() = flow {
        emit(apiService.getAvailableKHL())
    }

    suspend fun checkTransfer() = flow {
        emit(apiService.checkTransfer())
    }

    suspend fun initSangChiet(obj: InitSangChiet) = flow {
        emit(apiService.initSangChiet(obj))
    }
}