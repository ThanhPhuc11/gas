package vn.gas.thq.ui.sangchiet.nhapsangchiet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.gas.thq.base.BaseViewModel

class NhapSangChietViewModel(private val nhapSangChietRepository: NhapSangChietRepository) :
    BaseViewModel() {
    val callbackAvailableKHL = MutableLiveData<AvailableKHLResponse>()
    val callbackCheckTransfer = MutableLiveData<Boolean>()
    val callbackInitSangChietSuccess = MutableLiveData<Unit>()

    fun getAvailableKHL() {
        viewModelScope.launch(Dispatchers.Main) {
            nhapSangChietRepository.getAvailableKHL()
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
                    callbackAvailableKHL.value = it
                }
        }
    }

    fun checkTransfer() {
        viewModelScope.launch(Dispatchers.Main) {
            nhapSangChietRepository.checkTransfer()
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
                    callbackCheckTransfer.value = it
                }
        }
    }

    fun initSangChiet(obj: InitSangChiet) {
        viewModelScope.launch(Dispatchers.Main) {
            nhapSangChietRepository.initSangChiet(obj)
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
                    callbackInitSangChietSuccess.value = Unit
                }
        }
    }
}