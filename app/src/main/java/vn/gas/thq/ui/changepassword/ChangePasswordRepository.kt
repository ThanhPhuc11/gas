package vn.gas.thq.ui.changepassword

import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class ChangePasswordRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun changePassword(body: NewPasswordModel) = flow {
        emit(apiService.changePassword(body))
    }
}