package vn.gas.thq.ui.qlyeucaucanhan

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

class QLYCCaNhanViewModel(
    private val qlycCaNhanRepository: QLYCCaNhanRepository,
    private val context: Context?
) :
    BaseViewModel() {
    val mLiveData = MutableLiveData<MutableList<BussinesRequestModel>>()
    private val mList = mutableListOf<BussinesRequestModel>()
    fun onSubmitData(status: String?, fromDate: String, toDate: String) {
        viewModelScope.launch(Dispatchers.Main) {
            qlycCaNhanRepository.onSubmitRequest(status, fromDate, toDate)
                .onStart {
                }
                .onCompletion {
                    Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show()
//                    mLiveData.value = mList
                }
                .catch {
                    Log.e("Phuc catch", it.message.toString())
                }
                .collect {
                    mLiveData.value = it as MutableList<BussinesRequestModel>
                }
        }
    }

}