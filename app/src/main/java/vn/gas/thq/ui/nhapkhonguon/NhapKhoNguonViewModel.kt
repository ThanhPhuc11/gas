package vn.gas.thq.ui.nhapkhonguon

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.gas.thq.base.BaseViewModel
import vn.gas.thq.ui.nhapvo.BienXeModel
import vn.gas.thq.ui.vitri.ShopModel

class NhapKhoNguonViewModel(private val repository: NhapKhoNguonRepository) :
    BaseViewModel() {
    val callbackListShopByStaff = MutableLiveData<MutableList<ShopModel>>()
    val callbackListShop = MutableLiveData<MutableList<ShopModel>>()
    val callbackListBienXe = MutableLiveData<MutableList<BienXeModel>>()
    val callbackInitNhapGasNguonSuccess = MutableLiveData<Unit>()

//    fun getShopByStaff() {
//        viewModelScope.launch(Dispatchers.Main) {
//            repository.getShopByStaff()
//                .onStart { callbackStart.value = Unit }
//                .onCompletion { }
//                .catch {
//                    handleError(it)
//                }
//                .collect {
//                    callbackSuccess.value = Unit
//                    callbackListShopByStaff.value = it as MutableList
//                }
//        }
//    }

    fun getListShop(query: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getListShop(query)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion {

                }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    callbackListShop.value = it as MutableList<ShopModel>
                }
        }
    }

    fun getBienXe(query: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getBienXe(query)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion {

                }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    callbackListBienXe.value = it as MutableList<BienXeModel>
                }
        }
    }

    fun initNhapGasNguon(obj: InitNhapGasNguon) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.initNhapGasNguon(obj)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion {

                }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    callbackInitNhapGasNguonSuccess.value = Unit
                }
        }
    }
}