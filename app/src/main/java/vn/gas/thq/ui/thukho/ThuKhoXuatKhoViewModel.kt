package vn.gas.thq.ui.thukho

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
import vn.gas.thq.model.BussinesRequestModel
import vn.gas.thq.model.UserModel

class ThuKhoXuatKhoViewModel(
    private val thuKhoXuatKhoRepository: ThuKhoXuatKhoRepository,
    private val context: Context?
) :
    BaseViewModel() {

    val mLiveData = MutableLiveData<MutableList<BussinesRequestModel>>()
    val mDetailData = MutableLiveData<RequestDetailModel>()
    val mAcceptData = MutableLiveData<Unit>()
    val mListStaffData = MutableLiveData<MutableList<UserModel>>()
//    val mRejectData = MutableLiveData<Unit>()

    //    val showMessCallback = MutableLiveData<String>()
//
//    val callbackStart = MutableLiveData<Unit>()
//    val callbackSuccess = MutableLiveData<Unit>()
//    val callbackFail = MutableLiveData<Unit>()
    var staffCode: String? = null
    var status: String? = null
    lateinit var fromDate: String
    lateinit var toDate: String

    fun onSearchRequest(
        staffCode: String?,
        status: String?,
        fromDate: String,
        toDate: String,
        offSet: Int
    ) {
        this.staffCode = staffCode
        this.status = status
        this.fromDate = fromDate
        this.toDate = toDate

        viewModelScope.launch(Dispatchers.Main) {
            thuKhoXuatKhoRepository.onSearchRequest(
                staffCode,
                status,
                fromDate,
                toDate,
                offSet,
                100
            )
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion {
                }
                .catch {
                    handleError(it)
                }
                .collect {
                    mLiveData.value = it as MutableList<BussinesRequestModel>
                    callbackSuccess.value = Unit
                }
        }
    }

    fun onDetailRequest(orderId: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            thuKhoXuatKhoRepository.onDetailRequest(orderId)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    mDetailData.value = it
                    callbackSuccess.value = Unit
                }
        }
    }

    fun acceptOrNotRequest(orderId: String?, accept: Boolean) {
        if (accept) {
            viewModelScope.launch(Dispatchers.Main) {
                thuKhoXuatKhoRepository.onAcceptRequest(orderId)
                    .onStart {
                        callbackStart.value = Unit
                    }
                    .onCompletion { }
                    .catch {
                        handleError(it)
                    }
                    .collect {
                        callbackSuccess.value = Unit
                        mAcceptData.value = Unit
//                        onSearchRequest(status, fromDate, toDate)
                    }
            }
            return
        }
        viewModelScope.launch(Dispatchers.Main) {
            thuKhoXuatKhoRepository.onRejectRequest(orderId)
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    mAcceptData.value = Unit
                    onSearchRequest(staffCode, status, fromDate, toDate, 0)
                }
        }
    }

    fun getListStaff() {
        viewModelScope.launch(Dispatchers.Main) {
            thuKhoXuatKhoRepository.getListStaff()
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

}