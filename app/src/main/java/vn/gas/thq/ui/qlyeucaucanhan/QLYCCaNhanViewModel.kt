package vn.gas.thq.ui.qlyeucaucanhan

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.model.StatusValueModel
import vn.gas.thq.ui.retail.ApproveRequestModel

class QLYCCaNhanViewModel(
    private val qlycCaNhanRepository: QLYCCaNhanRepository,
    private val context: Context?
) :
    BaseViewModel() {
    var mCancelData = MutableLiveData<Unit>()
    val mLiveData = MutableLiveData<MutableList<BussinesRequestModel>>()
    val listStatus = MutableLiveData<MutableList<StatusValueModel>>()
    val detailApproveCallback = MutableLiveData<ApproveRequestModel>()

    fun onSubmitData(status: String?, fromDate: String, toDate: String, offSet: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            val staffCode = AppPreferencesHelper(context).userModel.staffCode
            qlycCaNhanRepository.onSubmitRequest(staffCode, status, fromDate, toDate, offSet, 100)
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
                    mLiveData.value = it as MutableList<BussinesRequestModel>
                }
        }
    }

    fun onCancelRequest(orderId: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            qlycCaNhanRepository.onCancelRequest(orderId)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    mCancelData.value = Unit
                }
        }
    }

    fun onGetSaleOrderStatus() {
        viewModelScope.launch(Dispatchers.Main) {
            qlycCaNhanRepository.onGetSaleOrderStatus()
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    listStatus.value = it as MutableList
                }
        }
    }

    fun onSearchRetail(
        status: String?, fromDate: String, toDate: String, offSet: Int
    ) {
        val staffCode = AppPreferencesHelper(context).userModel.staffCode
        viewModelScope.launch(Dispatchers.Main) {
            qlycCaNhanRepository.onSearchRetail(status, staffCode, fromDate, toDate, offSet, 100)
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
                    mLiveData.value = it as MutableList<BussinesRequestModel>
                }
        }
    }

    fun detailApproveLXBH(orderId: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            qlycCaNhanRepository.detailApproveLXBH(orderId)
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
                    detailApproveCallback.value = it
                }
        }
    }

}