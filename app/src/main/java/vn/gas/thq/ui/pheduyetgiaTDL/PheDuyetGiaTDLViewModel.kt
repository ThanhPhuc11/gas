package vn.gas.thq.ui.pheduyetgiaTDL

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
import vn.gas.thq.ui.pheduyetgiabanle.DuyetGiaModel
import vn.gas.thq.ui.pheduyetgiabanle.HistoryModel
import vn.gas.thq.ui.retail.ApproveRequestModel

class PheDuyetGiaTDLViewModel(private val pheDuyetGiaTDLRepository: PheDuyetGiaTDLRepository) :
    BaseViewModel() {
    val mListStaffData = MutableLiveData<MutableList<UserModel>>()
    val listStatus = MutableLiveData<MutableList<StatusValueModel>>()
    val mListDataSearch = MutableLiveData<MutableList<BussinesRequestModel>>()
    val detailApproveCallback = MutableLiveData<ApproveRequestModel>()
    val callbackAccept = MutableLiveData<Unit>()
    val callbackReject = MutableLiveData<Unit>()
    val callbackHistory = MutableLiveData<MutableList<HistoryModel>>()

    fun getListStaff() {
        viewModelScope.launch(Dispatchers.Main) {
            pheDuyetGiaTDLRepository.getListStaff()
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
            pheDuyetGiaTDLRepository.onGetSaleOrderStatus()
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

    fun onSearchRetailTDL(
        status: String?,
        staffCode: String?,
        fromDate: String,
        toDate: String,
        page: Int
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            pheDuyetGiaTDLRepository.onSearchRetailTDL(status, staffCode, fromDate, toDate, page)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion {

                }
                .catch {
                    handleError(it)
                    mListDataSearch.value?.clear()
                }
                .collect {
                    callbackSuccess.value = Unit
                    mListDataSearch.value = (it.listData) as MutableList<BussinesRequestModel>
                }
        }
    }

    fun detailTDL(orderId: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            pheDuyetGiaTDLRepository.detailTDL(orderId)
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

    fun doAcceptDuyetGiaTDL(orderId: String?, obj: DuyetGiaModel) {
        viewModelScope.launch(Dispatchers.Main) {
            pheDuyetGiaTDLRepository.doAcceptDuyetGiaTDL(orderId, obj)
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

    fun doRejectDuyetGiaTDL(orderId: String?, obj: DuyetGiaModel) {
        viewModelScope.launch(Dispatchers.Main) {
            pheDuyetGiaTDLRepository.doRejectDuyetGiaTDL(orderId, obj)
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

    fun getHistoryAcceptRetail(orderId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            pheDuyetGiaTDLRepository.getHistoryAcceptRetail(orderId)
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
                    callbackHistory.value = it as MutableList
                }
        }
    }
}