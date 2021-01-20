package vn.gas.thq.ui.login

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import vn.gas.thq.base.BaseRepository
import vn.gas.thq.network.ApiService

class LoginRepository(private val apiService: ApiService) : BaseRepository() {
    suspend fun login(
        client_id: String,
        client_secret: String,
        grant_type: String,
        username: String,
        password: String
    ) = flow {
        emit(
            apiService.login(
                client_id,
                client_secret,
                grant_type,
                username,
                password
            )
        )

    }
}