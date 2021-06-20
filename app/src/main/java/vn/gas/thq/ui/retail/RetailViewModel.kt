package vn.gas.thq.ui.retail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.gas.thq.base.BaseViewModel
import vn.gas.thq.ui.pheduyetgiabanle.HistoryModel

class RetailViewModel(private val retailRepository: RetailRepository) : BaseViewModel() {
    val mLiveDataCustomer = MutableLiveData<MutableList<Customer>>()
    val initRequestSuccess = MutableLiveData<ResponseInitRetail>()
    val doRetailSuccess = MutableLiveData<Unit>()
    val giaTANK12 = MutableLiveData<Int>()
    val giaTANK45 = MutableLiveData<Int>()
    val callbackHistory = MutableLiveData<MutableList<HistoryModel>>()

    fun onGetListCustomer(lat: String?, lng: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            retailRepository.onGetListCustomer(lat, lng)
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

    fun getGiaNiemYet(customer_id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            retailRepository.getGiaNiemYet(customer_id, "TANK12", "1")
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    giaTANK12.value = it.price
                }

            retailRepository.getGiaNiemYet(customer_id, "TANK45", "1")
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    giaTANK45.value = it.price
                }
        }
    }

    fun doRequestRetail(obj: RequestInitRetail) {
        viewModelScope.launch(Dispatchers.Main) {
            retailRepository.doRequestRetail(obj)
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
                    initRequestSuccess.value = it
                }
        }
    }

    //TODO: STEP 2
    fun doRetailLXBH(orderId: String?, obj: GasRemainModel) {
        viewModelScope.launch(Dispatchers.Main) {
            retailRepository.doRetailLXBH(orderId, obj)
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
                    doRetailSuccess.value = Unit
                }
        }
    }

    fun getHistoryAcceptRetail(orderId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            retailRepository.getHistoryAcceptRetail(orderId)
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
                    callbackHistory.value = it as MutableList
                }
        }
    }
}