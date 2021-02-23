package vn.gas.thq.ui.login

import android.content.Context
import android.text.TextUtils
import android.util.Log
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
import vn.gas.thq.model.TokenModel

class LoginViewModel(private val loginRepository: LoginRepository, private val context: Context?) :
    BaseViewModel() {
    private val loginSuccess = MutableLiveData<Unit>()

    fun doLogin(username: String, password: String) {
        if (TextUtils.isEmpty(username)) {
            //error
            showMessCallback.value = "Vui lòng nhập Tên đăng nhập"
            return
        }
        if (TextUtils.isEmpty(password)) {
            //error
            showMessCallback.value = "Vui lòng nhập Mật khẩu"
            return
        }
        viewModelScope.launch(Dispatchers.Main) {
            loginRepository.login(
                "DIF4R04G1MCPGATCJ3O4ZYTX2KC19TAD",
                "NFPZ8S7U9UJCOEM3TPHTWAC37I1DAL8DHLSDGCL94J0OR3D18FKHKX11CGX5WS8V",
                "password",
                username,
                password
            )
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion {
                }
                .catch {
                    handleError(it)
                }
                .collect {
                    AppPreferencesHelper(context).tokenModel = it
                    if (!TextUtils.isEmpty(it.accessToken)) {
                        loginSuccess.value = Unit
                        callbackSuccess.value = Unit
                    }
                }
        }
    }

    fun getSuccessToken() = loginSuccess
}