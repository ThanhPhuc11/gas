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

class PheDuyetGiaViewModel(private val pheDuyetGiaRepository: PheDuyetGiaRepository) :
    BaseViewModel() {
    val mListStaffData = MutableLiveData<MutableList<UserModel>>()
    val listStatus = MutableLiveData<MutableList<StatusValueModel>>()
    val mListDataSearch = MutableLiveData<MutableList<BussinesRequestModel>>()

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

    fun onSearchRetail(status: String?, fromDate: String, toDate: String) {
        viewModelScope.launch(Dispatchers.Main) {
            pheDuyetGiaRepository.onSearchRetail(status, fromDate, toDate)
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
}