package vn.gas.thq.ui.sharedviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vn.gas.thq.model.TokenModel

class TokenSharedViewModel : ViewModel() {
    val sharedToken = MutableLiveData<String>()

}