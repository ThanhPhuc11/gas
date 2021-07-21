package vn.gas.thq.ui.trano.qltrano

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

class QLTraNoViewModel(private val repository: QLTraNoRepository) :
    BaseViewModel() {
    val callbackListHistory = MutableLiveData<MutableList<HistoryTraNoModel>>()
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


    fun historyTraNo(cust_id: Int, debit_type: String, from_date: String, to_date: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.historyTraNo(cust_id, debit_type, from_date, to_date)
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
                    callbackListHistory.value = it
                }
        }
    }
}