package vn.gas.thq.ui.pheduyetgia

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
import vn.gas.thq.model.StatusValueModel
import vn.gas.thq.model.UserModel
import vn.gas.thq.ui.retail.ApproveRequestModel

class PheDuyetGiaViewModel(private val pheDuyetGiaRepository: PheDuyetGiaRepository) :
    BaseViewModel() {
    val mListStaffData = MutableLiveData<MutableList<UserModel>>()
    val listStatus = MutableLiveData<MutableList<StatusValueModel>>()
    val mListDataSearch = MutableLiveData<MutableList<BussinesRequestModel>>()
    val detailApproveCallback = MutableLiveData<ApproveRequestModel>()
    val callbackAccept = MutableLiveData<Unit>()
    val callbackReject = MutableLiveData<Unit>()

    fun getListStaff() {
        viewModelScope.launch(Dispatchers.Main) {
            pheDuyetGiaRepository.getListStaff()
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    mListStaffData.value = it as MutableList
                }
        }
    }

    fun onGetSaleOrderStatus() {
        viewModelScope.launch(Dispatchers.Main) {
            pheDuyetGiaRepository.onGetSaleOrderStatus()
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

    fun onSearchRetail(status: String?, staffCode: String?, fromDate: String, toDate: String) {
        viewModelScope.launch(Dispatchers.Main) {
            pheDuyetGiaRepository.onSearchRetail(status, staffCode, fromDate, toDate)
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
                    mListDataSearch.value = it as MutableList<BussinesRequestModel>
                }
        }
    }

    fun detailApproveLXBH(orderId: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            pheDuyetGiaRepository.detailApproveLXBH(orderId)
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

    fun doAcceptDuyetGia(orderId: String?, obj: DuyetGiaModel) {
        viewModelScope.launch(Dispatchers.Main) {
            pheDuyetGiaRepository.doAcceptDuyetGia(orderId, obj)
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
                    callbackAccept.value = Unit
                }
        }
    }

    fun doRejectDuyetGia(orderId: String?, obj: DuyetGiaModel) {
        viewModelScope.launch(Dispatchers.Main) {
            pheDuyetGiaRepository.doRejectDuyetGia(orderId, obj)
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
                    callbackReject.value = Unit
                }
        }
    }
}