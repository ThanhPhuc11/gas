package vn.gas.thq.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.gas.thq.network.ApiHelper
import vn.gas.thq.network.ApiService
import vn.gas.thq.ui.login.LoginRepository
import vn.gas.thq.ui.login.LoginViewModel

class ViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(LoginRepository(apiService)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}