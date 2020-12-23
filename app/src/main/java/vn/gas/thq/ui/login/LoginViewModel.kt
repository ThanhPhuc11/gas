package vn.gas.thq.ui.login

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
import vn.gas.thq.base.User

class LoginViewModel(private val loginRepository: LoginRepository) : BaseViewModel() {
//    private val liveDataA = MutableLiveData<List<User>>()

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
//                    liveDataA.value = listData
                    Log.e("Phuc", it.toString())
                }
                .catch {
//                    Log.e("Phuc", it.message.toString())
                        cause ->
                    println("Error")
                }
                .collect {
                    Log.e("Phuc", it.toString())
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

//    fun getLiveData() = liveDataA
}