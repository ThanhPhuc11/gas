package vn.gas.thq.ui.lapyeucauxuatkho

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.gas.thq.base.BaseViewModel
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.model.ProductShopModel

class LapYCXuatKhoViewModel(
    private val lapYCXuatKhoRepository: LapYCXuatKhoRepository,
    private val context: Context?
) :
    BaseViewModel() {
    val mLiveData = MutableLiveData<MutableList<ProductShopModel>>()
    var successCallBack1 = MutableLiveData<Unit>()
    var successCallBack2 = MutableLiveData<Unit>()

    fun getDataFromShop() {
        viewModelScope.launch(Dispatchers.Main) {
            lapYCXuatKhoRepository.getDataFromShop()
                .onStart {
                }
                .onCompletion {
                    successCallBack1.value = Unit
                }
                .catch {
                    Log.e("Phuc catch", it.message.toString())
                }
                .collect {
                    Log.e("Phuc", it.toString() + "onColect")
                    mLiveData.value = it as MutableList<ProductShopModel>
                }
        }
    }

    fun onSubmitData(initExportRequest: InitExportRequest) {
        viewModelScope.launch(Dispatchers.Main) {
            lapYCXuatKhoRepository.onSubmitRequest(initExportRequest)
                .onStart {
                }
                .onCompletion {
//                    Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show()
                    successCallBack2.value = Unit
                }
                .catch {
                    Log.e("Phuc catch", it.message.toString())
                }
                .collect {
                }
        }
    }
}