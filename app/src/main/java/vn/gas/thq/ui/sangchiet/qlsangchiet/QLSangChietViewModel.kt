package vn.gas.thq.ui.sangchiet.qlsangchiet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vn.gas.thq.base.BaseViewModel
import vn.gas.thq.ui.xemkho.KhoModel

class QLSangChietViewModel(private val qlSangChietRepository: QLSangChietRepository) :
    BaseViewModel() {
    val listHistory = MutableLiveData<MutableList<HistorySangChietModel>>()
    val listKho = MutableLiveData<MutableList<KhoModel>>()

    fun getListKho() {
        viewModelScope.launch(Dispatchers.Main) {
            qlSangChietRepository.getDSKho()
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
                    listKho.value = it as MutableList
                }
        }
    }


    fun historySangChiet(shop_id: Int, from_date: String, to_date: String) {
        viewModelScope.launch(Dispatchers.Main) {
            qlSangChietRepository.historySangChiet(shop_id, from_date, to_date)
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
                    listHistory.value = it as MutableList<HistorySangChietModel>
                }
        }
    }
}