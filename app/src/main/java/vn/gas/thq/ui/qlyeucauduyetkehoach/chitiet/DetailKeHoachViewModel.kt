package vn.gas.thq.ui.qlyeucauduyetkehoach.chitiet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.gas.thq.base.BaseViewModel
import vn.gas.thq.ui.qlyeucauduyetkehoach.KHBHOrderModel

class DetailKeHoachViewModel(private val detailKeHoachRepository: DetailKeHoachRepository) :
    BaseViewModel() {
    val callbackDetailKHBH = MutableLiveData<DetailKHBHModel>()
    val callbackDuyetSuccessKHBH = MutableLiveData<Unit>()
    val callbackTuChoiSuccessKHBH = MutableLiveData<Unit>()

    fun getKeHoachBH(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            detailKeHoachRepository.getDetailKeHoachBH(id)
                .onStart {
                    callbackStart.value = Unit
                }.onCompletion {

                }.catch {
                    handleError(it)
                }.collect {
                    callbackSuccess.value = Unit
                    callbackDetailKHBH.value = it
                }
        }
    }

    fun duyetKeHoachBH(id: String, type: DetailTypeKHBHModel) {
        viewModelScope.launch(Dispatchers.Main) {
            detailKeHoachRepository.duyetKHBH(id, type)
                .onStart {
                    callbackStart.value = Unit
                }.onCompletion {

                }.catch {
                    handleError(it)
                }.collect {
                    callbackSuccess.value = Unit
                    callbackDuyetSuccessKHBH.value = Unit
                }
        }
    }

    fun tuChoiKeHoachBH(id: String, type: DetailTypeKHBHModel) {
        viewModelScope.launch(Dispatchers.Main) {
            detailKeHoachRepository.tuChoiKHBH(id, type)
                .onStart {
                    callbackStart.value = Unit
                }.onCompletion {

                }.catch {
                    handleError(it)
                }.collect {
                    callbackSuccess.value = Unit
                    callbackTuChoiSuccessKHBH.value = Unit
                }
        }
    }
}