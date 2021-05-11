package vn.gas.thq.ui.vitri

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.gas.thq.base.BaseViewModel
import vn.gas.thq.ui.retail.Customer

class ViTriKHViewModel(private val viTriKHRepositoty: ViTriKHRepositoty) : BaseViewModel() {
    val mLiveDataCustomer = MutableLiveData<MutableList<Customer>>()
    val callbackDetailKH = MutableLiveData<Customer>()
    val callbackUpdateSuccess = MutableLiveData<Unit>()
    val callbackListShop = MutableLiveData<MutableList<ShopModel>>()
    val callbackListSaleLine = MutableLiveData<MutableList<SaleLineModel>>()

    fun getAllShop() {
        viewModelScope.launch(Dispatchers.Main) {
            viTriKHRepositoty.getAllShop("")
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
            viTriKHRepositoty.getSaleLine("shopId==$shopId")
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

    fun onGetListCustomer(query: String?, page: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            viTriKHRepositoty.onGetListCustomer(query, page, 100)
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
                    mLiveDataCustomer.value =
                        (it as ListResponseCustomer).data as MutableList<Customer>
                }
        }
    }

    fun onGetDetailCustomer(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            viTriKHRepositoty.onGetDetailCustomer(id)
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
                    callbackDetailKH.value = it
//                    mLiveDataCustomer.value = it as MutableList<Customer>
                }
        }
    }

    fun capNhatToaDoKH(custId: String?, toaDoModel: ToaDoModel) {
        viewModelScope.launch(Dispatchers.Main) {
            viTriKHRepositoty.updateToaDoKH(custId, toaDoModel)
                .onStart {
                    callbackStart.value
                }
                .onCompletion {

                }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    callbackUpdateSuccess.value = Unit
                }
        }
    }
}