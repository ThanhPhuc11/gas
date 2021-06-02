package vn.gas.thq.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IntentShareViewModel : ViewModel() {
    val callbackFirebaseType = MutableLiveData<String>()
}