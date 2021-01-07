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
    //    private val liveDataA = MutableLiveData<List<User>>()
    private val loginLiveData = MutableLiveData<TokenModel>()
    private val checkAccesToken = MutableLiveData<Boolean>()
    private var tokenModel: TokenModel? = null

    fun doLogin(username: String, password: String) {
        if (TextUtils.isEmpty(username)) {
            //error
            return
        }
        if (TextUtils.isEmpty(password)) {
            //error
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
                    Log.e("Phuc", "onStart")
                }
                .onCompletion {
                    Log.e("Phuc complete", it.toString() + "onCom")
                    AppPreferencesHelper(context).tokenModel = tokenModel
                    if (!TextUtils.isEmpty(tokenModel?.accessToken)) {
                        checkAccesToken.value = true
                    }
                }
                .catch {
                    checkAccesToken.value = false
                }
                .collect {
                    Log.e("Phuc", it.toString() + "onColect")
                    tokenModel = it
                }

//            loginRepository.getUsers()
//                .onCompletion {
////                    liveDataA.value = listData
//                    Log.e(
//                        "complete",
//                        "liveDataA size: "
//                    )
//                }
//                .collect {
////                    listData.add(it)
//                    Log.e(
//                        "element",
//                        it.userId/* + " listData size " + listData.size + " : liveDataA size : " + liveDataA.value?.size*/
//                    )
//                }

        }
    }

    fun getStatusAccessToken() = checkAccesToken
}