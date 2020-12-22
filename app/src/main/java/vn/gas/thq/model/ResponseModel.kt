package vn.gas.thq.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ResponseModel<V> : Serializable {
    @SerializedName("data")
    @Expose
    var singleData: V? = null

    @SerializedName("status")
    @Expose
    var mess: StatusModel? = null
}