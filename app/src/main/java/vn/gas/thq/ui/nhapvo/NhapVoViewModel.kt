package vn.gas.thq.ui.nhapvo

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
import vn.gas.thq.ui.vitri.ShopModel

class NhapVoViewModel(private val repository: NhapVoRepository) :
    BaseViewModel() {
    val callbackListShop = MutableLiveData<MutableList<ShopModel>>()
    val callbackListBienXe = MutableLiveData<MutableList<BienXeModel>>()
    val callbackListTank = MutableLiveData<MutableList<VoModel>>()
    val callbackNhapXuatVo = MutableLiveData<Unit>()

    fun getListShop(query: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getListShop(query)
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
                    callbackListShop.value = it as MutableList<ShopModel>
                }
        }
    }

    fun getBienXe(query: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getBienXe(query)
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
                    callbackListBienXe.value = it as MutableList<BienXeModel>
                }
        }
    }

    fun getVo() {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getVo()
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
                    callbackListTank.value = it as MutableList<VoModel>
                }
        }
    }

    fun xuatnhapVo(shop_id: Int, license_plate_id: Int, transfer: List<VoModel>) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.xuatnhapVo(shop_id, license_plate_id, transfer)
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
                    callbackNhapXuatVo.value = Unit
                }
        }
    }
}