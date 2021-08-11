package vn.gas.thq.ui.changepassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.gas.thq.base.BaseViewModel

class ChangePasswordViewModel(private val repository: ChangePasswordRepository) :
    BaseViewModel() {
    val changeSuccess = MutableLiveData<Unit>()

    fun changePassword(body: NewPasswordModel) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.changePassword(body)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion {
                }
                .catch {
                    handleError(it)
                }
                .collect {
                    changeSuccess.value = Unit
                    callbackSuccess.value = Unit
                }
        }
    }
}