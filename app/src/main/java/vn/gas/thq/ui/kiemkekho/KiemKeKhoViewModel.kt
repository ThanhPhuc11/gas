package vn.gas.thq.ui.kiemkekho

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.gas.thq.base.BaseViewModel
import vn.gas.thq.model.ProductShopModel
import vn.gas.thq.ui.xemkho.KhoModel

class KiemKeKhoViewModel(
    private val kiemKeRepository: KiemKeKhoRepository
) :
    BaseViewModel() {

    val mLiveData = MutableLiveData<MutableList<ProductShopModel>>()
    val callbackKiemKeKho = MutableLiveData<Unit>()
    val listKho = MutableLiveData<MutableList<KhoModel>>()

    fun getDataFromCode(shop_code: String?, staff_code: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            kiemKeRepository.getDataFromCode(shop_code, staff_code)
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
                    mLiveData.value = it as MutableList<ProductShopModel>
                }
        }
    }

    fun kiemKeKho(obj: KiemKeRequestModel) {
        viewModelScope.launch(Dispatchers.Main) {
            kiemKeRepository.kiemKeKho(obj)
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
                    callbackKiemKeKho.value = Unit
                }
        }
    }

    fun getListKho() {
        viewModelScope.launch(Dispatchers.Main) {
            kiemKeRepository.getDSKho()
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
                    listKho.value = it as MutableList
                }
        }
    }

}