package vn.gas.thq.ui.nghiphep.lichsu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.gas.thq.base.BaseViewModel
import vn.gas.thq.model.UserModel
import vn.gas.thq.ui.nghiphep.VacationModel

class LSNghiPhepViewModel(private val repository: LSNghiPhepRepository) :
    BaseViewModel() {
    val callbackListStaff = MutableLiveData<MutableList<UserModel>>()
    val callbackListVacation = MutableLiveData<MutableList<VacationModel>>()

    fun getVacation(staffId: Int, fromDate: String, toDate: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getVacation(staffId, fromDate, toDate)
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
                    callbackListVacation.value = it as MutableList<VacationModel>
                }
        }
    }

    fun getStaffFromShopId(shopId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getStaffFromShopId(shopId)
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
                    callbackListStaff.value = it as MutableList<UserModel>
                }
        }
    }
}