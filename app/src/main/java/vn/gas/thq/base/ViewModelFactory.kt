package vn.gas.thq.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.gas.thq.network.ApiService
import vn.gas.thq.ui.lapyeucauxuatkho.LapYCXuatKhoRepository
import vn.gas.thq.ui.lapyeucauxuatkho.LapYCXuatKhoViewModel
import vn.gas.thq.ui.login.LoginRepository
import vn.gas.thq.ui.login.LoginViewModel
import vn.gas.thq.ui.qlyeucaucanhan.QLYCCaNhanRepository
import vn.gas.thq.ui.qlyeucaucanhan.QLYCCaNhanViewModel

class ViewModelFactory(private val apiService: ApiService, context: Context?) :
    ViewModelProvider.Factory {
    var creators = mutableMapOf<Class<out ViewModel>, ViewModel>()

    init {
        creators[LoginViewModel::class.java] = LoginViewModel(LoginRepository(apiService), context)
        creators[LapYCXuatKhoViewModel::class.java] =
            LapYCXuatKhoViewModel(LapYCXuatKhoRepository(apiService), context)
        creators[QLYCCaNhanViewModel::class.java] =
            QLYCCaNhanViewModel(QLYCCaNhanRepository(apiService), context)
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