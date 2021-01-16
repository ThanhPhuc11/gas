package vn.gas.thq.ui.lapyeucauxuatkho

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.HttpException
import vn.gas.thq.base.BaseViewModel
import vn.gas.thq.model.ErrorModel
import vn.gas.thq.model.ProductShopModel
import java.io.IOException


class LapYCXuatKhoViewModel(
    private val lapYCXuatKhoRepository: LapYCXuatKhoRepository,
    private val context: Context?
) :
    BaseViewModel() {
    val mLiveData = MutableLiveData<MutableList<ProductShopModel>>()
    val callbackInitSuccess = MutableLiveData<Unit>()

    fun getDataFromShop() {
        viewModelScope.launch(Dispatchers.Main) {
            lapYCXuatKhoRepository.getDataFromShop()
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
                    mLiveData.value = it as MutableList<ProductShopModel>
                }
        }
    }

    fun onSubmitData(initExportRequest: InitExportRequest) {
        viewModelScope.launch(Dispatchers.Main) {
            lapYCXuatKhoRepository.onSubmitRequest(initExportRequest)
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
                    callbackInitSuccess.value = Unit
//                    showMessCallback.value = "Thành công"
                }
        }
    }
}