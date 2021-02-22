package vn.gas.thq.ui.xemkho

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.gas.thq.base.BaseViewModel
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.model.ProductShopModel
import vn.gas.thq.model.UserModel

class XemKhoViewModel(
    private val xemKhoRepository: XemKhoRepository
) :
    BaseViewModel() {

    val mLiveData = MutableLiveData<MutableList<ProductShopModel>>()
    val listKho = MutableLiveData<MutableList<KhoModel>>()

    fun getDataFromShop(shop_code: String?, staff_code: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            xemKhoRepository.getDataFromShop(shop_code, staff_code)
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

    fun getListKho() {
        viewModelScope.launch(Dispatchers.Main) {
            xemKhoRepository.getDSKho()
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