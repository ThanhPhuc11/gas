package vn.gas.thq.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import retrofit2.HttpException
import vn.gas.thq.model.ErrorModel
import java.io.IOException

abstract class BaseViewModel : ViewModel() {

    val showMessCallback = MutableLiveData<String>()

    val callbackStart = MutableLiveData<Unit>()
    val callbackSuccess = MutableLiveData<Unit>()
    val callbackFail = MutableLiveData<Unit>()

    fun handleError(it: Throwable) {
        try {
            callbackFail.value = Unit
            val response = (it as HttpException).response()
            val gson = GsonBuilder().create()
            val mError: ErrorModel
            try {
                mError = gson.fromJson(
                    response?.errorBody()?.string(),
                    ErrorModel::class.java
                )
                showMessCallback.value = mError.detail
                when (response?.code()) {
                    400 -> {
//                showMessCallback.value = response.code().toString()
                    }
                    401 -> {

                    }
                }
            } catch (e: IOException) {
                // handle failure to read error
            }
        } catch (e: Exception) {
            showMessCallback.value = "Vui lòng kiểm tra lại đường truyền"
        }

    }
}