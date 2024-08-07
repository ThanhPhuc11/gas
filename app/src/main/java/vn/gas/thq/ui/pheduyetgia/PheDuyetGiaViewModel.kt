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
    val callbackHistory = MutableLiveData<MutableList<HistoryModel>>()

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

    fun onHandleSearch(
        type: String,
        status: String?,
        staffCode: String?,
        fromDate: String,
        toDate: String,
        page: Int
    ) {
        if (type == "1") onSearchRetail(status, staffCode, fromDate, toDate, page)
        else onSearchRetailTDL(status, staffCode, fromDate, toDate, page)
    }

    fun onHandleAccept(type: String, orderId: String?, obj: DuyetGiaModel) {
        if (type == "1") doAcceptDuyetGia(orderId, obj) else doAcceptDuyetGiaTDL(orderId, obj)
    }

    fun onHandleReject(type: String, orderId: String?, obj: DuyetGiaModel) {
        if (type == "1") doRejectDuyetGia(orderId, obj) else doRejectDuyetGiaTDL(orderId, obj)
    }

    private fun onSearchRetail(
        status: String?,
        staffCode: String?,
        fromDate: String,
        toDate: String,
        page: Int
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            pheDuyetGiaRepository.onSearchRetail(status, staffCode, fromDate, toDate, page)
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
                    mListDataSearch.value = it.listData as MutableList<BussinesRequestModel>
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

    private fun doAcceptDuyetGia(orderId: String?, obj: DuyetGiaModel) {
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

    private fun doRejectDuyetGia(orderId: String?, obj: DuyetGiaModel) {
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

    private fun onSearchRetailTDL(
        status: String?,
        staffCode: String?,
        fromDate: String,
        toDate: String,
        page: Int
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            pheDuyetGiaRepository.onSearchRetailTDL(status, staffCode, fromDate, toDate, page)
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

    private fun doAcceptDuyetGiaTDL(orderId: String?, obj: DuyetGiaModel) {
        viewModelScope.launch(Dispatchers.Main) {
            pheDuyetGiaRepository.doAcceptDuyetGiaTDL(orderId, obj)
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

    private fun doRejectDuyetGiaTDL(orderId: String?, obj: DuyetGiaModel) {
        viewModelScope.launch(Dispatchers.Main) {
            pheDuyetGiaRepository.doRejectDuyetGiaTDL(orderId, obj)
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
            pheDuyetGiaRepository.getHistoryAcceptRetail(orderId)
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