package vn.gas.thq.ui.trano.nhaptrano

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

class NhapTraNoViewModel(private val repository: NhapTraNoRepository) :
    BaseViewModel() {
    val callbackListKH = MutableLiveData<MutableList<Customer>>()

    fun onGetListCustomer(query: String?, page: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.onGetListCustomer(query, page, 1000)
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
                    callbackListKH.value =
                        it.data as MutableList<Customer>
                }
        }
    }
}