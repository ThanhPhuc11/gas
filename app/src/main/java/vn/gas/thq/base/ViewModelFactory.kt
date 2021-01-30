package vn.gas.thq.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.gas.thq.network.ApiService
import vn.gas.thq.ui.home.HomeRepository
import vn.gas.thq.ui.home.HomeViewModel
import vn.gas.thq.ui.kiemkekho.KiemKeKhoRepository
import vn.gas.thq.ui.kiemkekho.KiemKeKhoViewModel
import vn.gas.thq.ui.lapyeucauxuatkho.LapYCXuatKhoRepository
import vn.gas.thq.ui.lapyeucauxuatkho.LapYCXuatKhoViewModel
import vn.gas.thq.ui.login.LoginRepository
import vn.gas.thq.ui.login.LoginViewModel
import vn.gas.thq.ui.pheduyetgia.PheDuyetGiaRepository
import vn.gas.thq.ui.pheduyetgia.PheDuyetGiaViewModel
import vn.gas.thq.ui.qlyeucaucanhan.QLYCCaNhanRepository
import vn.gas.thq.ui.qlyeucaucanhan.QLYCCaNhanViewModel
import vn.gas.thq.ui.retail.RetailRepository
import vn.gas.thq.ui.retail.RetailViewModel
import vn.gas.thq.ui.thukho.ThuKhoXuatKhoRepository
import vn.gas.thq.ui.thukho.ThuKhoXuatKhoViewModel
import vn.gas.thq.ui.xemkho.XemKhoRepository
import vn.gas.thq.ui.xemkho.XemKhoViewModel

class ViewModelFactory(apiService: ApiService, context: Context?) :
    ViewModelProvider.Factory {
    var creators = mutableMapOf<Class<out ViewModel>, ViewModel>()

    init {
        creators[LoginViewModel::class.java] = LoginViewModel(LoginRepository(apiService), context)
        creators[HomeViewModel::class.java] = HomeViewModel(HomeRepository(apiService), context)
        creators[LapYCXuatKhoViewModel::class.java] =
            LapYCXuatKhoViewModel(LapYCXuatKhoRepository(apiService), context)
        creators[QLYCCaNhanViewModel::class.java] =
            QLYCCaNhanViewModel(QLYCCaNhanRepository(apiService), context)
        creators[ThuKhoXuatKhoViewModel::class.java] =
            ThuKhoXuatKhoViewModel(ThuKhoXuatKhoRepository(apiService), context)
        creators[RetailViewModel::class.java] =
            RetailViewModel(RetailRepository(apiService))
        creators[PheDuyetGiaViewModel::class.java] =
            PheDuyetGiaViewModel(PheDuyetGiaRepository(apiService))
        creators[XemKhoViewModel::class.java] =
            XemKhoViewModel(XemKhoRepository(apiService))
        creators[KiemKeKhoViewModel::class.java] =
            KiemKeKhoViewModel(KiemKeKhoRepository(apiService))
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