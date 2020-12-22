package vn.gas.thq.ui.login

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

    fun onGetData() {
        viewModelScope.launch(Dispatchers.Main) {
//            loginRepository.login()
//                .onCompletion {
////                    liveDataA.value = listData
//                    Log.e("Phuc", it.toString())
//                }
//                .onStart {
//                    Log.e("Phuc", "onStart")
//                }
//                .catch {
//                    Log.e("Phuc", it.message.toString())
//                }
//                .collect {
//                    Log.e("Phuc", it.toString())
//                }

            loginRepository.getUsers()
                .onCompletion {
//                    liveDataA.value = listData
                    Log.e(
                        "complete",
                        "liveDataA size: "
                    )
                }
                .collect {
//                    listData.add(it)
                    Log.e(
                        "element",
                        it.userId/* + " listData size " + listData.size + " : liveDataA size : " + liveDataA.value?.size*/
                    )
                }

        }
    }

//    fun getLiveData() = liveDataA
}