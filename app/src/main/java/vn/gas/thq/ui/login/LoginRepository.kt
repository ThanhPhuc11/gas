package vn.gas.thq.ui.login

import kotlinx.coroutines.flow.asFlow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class LoginRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun login() = apiService.login()

    suspend fun getUsers() = apiService.getUsers().asFlow()
}