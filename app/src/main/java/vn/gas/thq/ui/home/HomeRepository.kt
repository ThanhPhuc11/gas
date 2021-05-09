package vn.gas.thq.ui.home

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class HomeRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun getUserInfo() = flow { emit(apiService.getUsers()) }
    suspend fun getUserPermission() = flow { emit(apiService.getPermission()) }
}