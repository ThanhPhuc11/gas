package vn.gas.thq.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.gas.thq.network.ApiService
import vn.gas.thq.ui.login.LoginRepository
import vn.gas.thq.ui.login.LoginViewModel

class ViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    var creators = mutableMapOf<Class<out ViewModel>, ViewModel>()

    init {
        creators[LoginViewModel::class.java] = LoginViewModel(LoginRepository(apiService))
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        for (a in creators.entries) {
            if (modelClass.isAssignableFrom(a.key)) {
                return a.value as T
            }
        }
        throw IllegalArgumentException("Unknown class name")
    }

}