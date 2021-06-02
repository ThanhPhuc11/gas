package vn.gas.thq.ui.qlyeucauduyetkehoach

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.gas.thq.base.BaseViewModel
import vn.gas.thq.ui.vitri.SaleLineModel
import vn.gas.thq.ui.vitri.ShopModel

class QLYCKeHoachViewModel(private val qlycKeHoachRepository: QLYCKeHoachRepository) :
    BaseViewModel() {
    val callbackListShop = MutableLiveData<MutableList<ShopModel>>()
    val callbackListSaleLine = MutableLiveData<MutableList<SaleLineModel>>()
    val callbackListKHBH = MutableLiveData<MutableList<KHBHOrderModel>>()

    fun getAllShop() {
        viewModelScope.launch(Dispatchers.Main) {
            qlycKeHoachRepository.getAllShop()
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    callbackListShop.value = it as MutableList
                }
        }
    }

    fun getSaleLine(shopId: String) {
        viewModelScope.launch(Dispatchers.Main) {
            qlycKeHoachRepository.getSaleLine("shopId==$shopId")
                .onStart { callbackStart.value = Unit }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    callbackListSaleLine.value = it as MutableList
                }
        }
    }

    fun getKeHoachBH(
        status: Int?,
        from_date: String,
        to_date: String,
        staff_code: String?,
        shop_code: String?,
        sale_line_code: String?,
        offset: Int,
        size: Int
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            qlycKeHoachRepository.getKeHoachBH(
                status,
                from_date,
                to_date,
                staff_code,
                shop_code,
                sale_line_code,
                offset,
                size
            ).onStart {
                callbackStart.value = Unit
            }.onCompletion {

            }.catch {
                handleError(it)
            }.collect {
                callbackSuccess.value = Unit
                callbackListKHBH.value = it.listData as MutableList<KHBHOrderModel>
            }
        }
    }
}