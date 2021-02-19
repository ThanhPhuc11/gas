package vn.gas.thq.ui.kehoachbh

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

class LapKeHoachBHViewModel(private val lapKeHoachBHRepository: LapKeHoachBHRepository) :
    BaseViewModel() {
    val mLiveDataCustomer = MutableLiveData<MutableList<Customer>>()
    val callbackKHBH = MutableLiveData<Unit>()

    fun onGetListCustomer(lat: String?, lng: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            lapKeHoachBHRepository.onGetListCustomer(lat, lng)
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

    fun lapKeHoachBH(obj: RequestKeHoachModel) {
        viewModelScope.launch(Dispatchers.Main) {
            lapKeHoachBHRepository.lapKeHoachBH(obj)
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
                    callbackKHBH.value = Unit
                }
        }
    }
}