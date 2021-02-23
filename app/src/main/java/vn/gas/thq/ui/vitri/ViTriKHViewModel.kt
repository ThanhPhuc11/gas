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
    fun onGetListCustomer(lat: String?, lng: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            viTriKHRepositoty.onGetListCustomer(lat, lng)
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
                    mLiveDataCustomer.value = it as MutableList<Customer>
                }
        }
    }
}