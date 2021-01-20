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
import vn.gas.thq.model.UserModel

class HomeViewModel(private val homeRepository: HomeRepository, private val context: Context?) :
    BaseViewModel() {
    var userModelCallback = MutableLiveData<UserModel>()

    fun getUserInfo() {
        viewModelScope.launch(Dispatchers.Main) {
            homeRepository.getUserInfo()
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                    AppPreferencesHelper(context).userModel = null
                }
                .collect {
                    AppPreferencesHelper(context).userModel = it
                    userModelCallback.value = it
                    callbackSuccess.value = Unit
                }
        }
    }
}