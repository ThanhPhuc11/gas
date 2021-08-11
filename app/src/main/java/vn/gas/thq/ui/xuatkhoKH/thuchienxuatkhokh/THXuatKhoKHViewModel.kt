package vn.gas.thq.ui.xuatkhoKH.thuchienxuatkhokh

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
import vn.gas.thq.ui.retail.Customer

class THXuatKhoKHViewModel(private val repository: THXuatKhoKHRepository) :
    BaseViewModel() {
    val callbackListKH = MutableLiveData<MutableList<Customer>>()
    val mListDataSearch = MutableLiveData<MutableList<BussinesRequestModel>>()

//    fun onGetListCustomer(query: String?, page: Int) {
//        viewModelScope.launch(Dispatchers.Main) {
//            repository.onGetListCustomer(query, page, 1000)
//                .onStart {
//                    callbackStart.value = Unit
//                }
//                .onCompletion {
//
//                }
//                .catch {
//                    handleError(it)
//                }
//                .collect {
//                    callbackSuccess.value = Unit
//                    callbackListKH.value =
//                        it.data as MutableList<Customer>
//                }
//        }
//    }


    fun xuatHang(orderId: String?, obj: XuatHangModel) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.xuatHang(orderId, obj)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion {

                }
                .catch {
                    handleError(it)
                    mListDataSearch.value?.clear()
                }
                .collect {
                    callbackSuccess.value = Unit
                }
        }
    }
}