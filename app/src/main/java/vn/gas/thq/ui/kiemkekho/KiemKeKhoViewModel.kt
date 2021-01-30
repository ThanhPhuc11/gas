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

class KiemKeKhoViewModel(
    private val kiemKeRepository: KiemKeKhoRepository
) :
    BaseViewModel() {

    val mLiveData = MutableLiveData<MutableList<ProductShopModel>>()

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

}