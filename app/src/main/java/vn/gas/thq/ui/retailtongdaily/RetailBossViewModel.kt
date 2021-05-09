package vn.gas.thq.ui.retailtongdaily

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.gas.thq.base.BaseViewModel
import vn.gas.thq.ui.retail.Customer
import vn.gas.thq.ui.retail.GasRemainModel
import vn.gas.thq.ui.retail.RequestInitRetail
import vn.gas.thq.ui.retail.ResponseInitRetail

class RetailBossViewModel(private val retailRepository: RetailBossRepository) : BaseViewModel() {
    val mLiveDataCustomer = MutableLiveData<MutableList<Customer>>()
    val initRequestSuccess = MutableLiveData<ResponseInitRetail>()
    val doRetailSuccess = MutableLiveData<Unit>()
    val giaTANK12 = MutableLiveData<Int>()
    val giaTANK45 = MutableLiveData<Int>()
    val giaTDLGAS12 = MutableLiveData<Int?>()
    val giaTDLGAS45 = MutableLiveData<Int?>()
    val fee12 = MutableLiveData<FeeVanChuyenModel>()
    val fee45 = MutableLiveData<FeeVanChuyenModel>()

    fun onGetListCustomer(lat: String?, lng: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            retailRepository.onGetListCustomer(lat, lng)
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
                    mLiveDataCustomer.value = it as MutableList<Customer>
                }
        }
    }

    fun getGiaTongDaiLy(cust_id: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            retailRepository.getGiaKHBHTongDaiLy(cust_id, "GAS12")
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    giaTDLGAS12.value = it.price
                }

            retailRepository.getGiaKHBHTongDaiLy(cust_id, "GAS45")
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    giaTDLGAS45.value = it.price
                }
        }
    }

    fun getGiaNiemYet(customer_id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            retailRepository.getGiaNiemYet(customer_id, "TANK12", "3")
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    giaTANK12.value = it.price
                }

            retailRepository.getGiaNiemYet(customer_id, "TANK45", "3")
                .onStart {
                    callbackStart.value = Unit
                }
                .onCompletion { }
                .catch {
                    handleError(it)
                }
                .collect {
                    callbackSuccess.value = Unit
                    giaTANK45.value = it.price
                }
        }
    }

    fun doRequestRetail(obj: RequestInitRetail) {
        viewModelScope.launch(Dispatchers.Main) {
            retailRepository.doRequestRetailBoss(obj)
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
                    initRequestSuccess.value = it
                }
        }
    }

    fun getGiaVanChuyen(obj: RequestInitRetail, isGas12: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            if (isGas12) {
                retailRepository.getPhiVanChuyen(obj)
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
                        fee12.value = it
                    }
            } else {
                retailRepository.getPhiVanChuyen(obj)
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
                        fee45.value = it
                    }
            }

        }
    }

    //TODO: STEP 2
    fun doRetailLXBH(orderId: String?, obj: GasRemainModel) {
        viewModelScope.launch(Dispatchers.Main) {
            retailRepository.doRetailBossLXBH(orderId, obj)
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
                    doRetailSuccess.value = Unit
                }
        }
    }
}