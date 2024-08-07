package vn.gas.thq.ui.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.gas.thq.base.BaseViewModel
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.model.RequestDeviceModel
import vn.gas.thq.model.UserModel
import vn.gas.thq.util.AppConstants

class FcmHomeViewModel(private val homeRepository: FcmHomeRepository) :
    BaseViewModel() {
    var userModelCallback = MutableLiveData<UserModel>()

    fun registerFcm(requestFcmDeviceModel: RequestDeviceModel) {
        viewModelScope.launch(Dispatchers.Main) {
            homeRepository.registerFcmDevice(requestFcmDeviceModel)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
//                    AppPreferencesHelper(context).userModel = it
//                    userModelCallback.value = it
                    callbackSuccess.value = Unit
                }
        }
    }
}