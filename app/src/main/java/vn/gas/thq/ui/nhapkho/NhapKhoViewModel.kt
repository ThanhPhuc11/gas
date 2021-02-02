package vn.gas.thq.ui.nhapkho

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.gas.thq.base.BaseViewModel
import vn.gas.thq.model.PriceModel
import vn.gas.thq.model.ProductShopModel
import vn.gas.thq.model.UserModel

class NhapKhoViewModel(private val nhapKhoRepository: NhapKhoRepository) : BaseViewModel() {

    val mListStaffData = MutableLiveData<MutableList<UserModel>>()
    val mListDataProduct = MutableLiveData<MutableList<ProductShopModel>>()
    val gasPriceData = MutableLiveData<Int>()

    fun getListStaff() {
        viewModelScope.launch(Dispatchers.Main) {
            nhapKhoRepository.getListStaff()
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    mListStaffData.value = it as MutableList
                }
        }
    }

    fun getDataFromStaff(staff_code: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            nhapKhoRepository.getProductFromCode("admin", staff_code)
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
                    mListDataProduct.value = it as MutableList<ProductShopModel>
                }
        }
    }

    fun nhapKho(obj: RequestNhapKho) {
        viewModelScope.launch(Dispatchers.Main) {
            nhapKhoRepository.nhapKho(obj)
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
//                    mListDataProduct.value = it as MutableList<ProductShopModel>
                }
        }
    }

    fun gasRemainPrice() {
        viewModelScope.launch(Dispatchers.Main) {
            nhapKhoRepository.gasRemainPrice()
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
                    gasPriceData.value = it.price
//                    mListDataProduct.value = it as MutableList<ProductShopModel>
                }
        }
    }
}