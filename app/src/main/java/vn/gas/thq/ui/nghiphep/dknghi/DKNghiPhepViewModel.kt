package vn.gas.thq.ui.nghiphep.dknghi

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.gas.thq.base.BaseViewModel

class DKNghiPhepViewModel(private val repository: DKNghiPhepRepository) :
    BaseViewModel() {
    val callbackRegisterSuccess = MutableLiveData<Unit>()
    fun registerVacation(staffId: Int, fromDate: String, toDate: String, reason: String) {
        viewModelScope.launch {
            repository.registerVacation(staffId, fromDate, toDate, reason)
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
                    callbackRegisterSuccess.value = Unit
                }
        }
    }
}