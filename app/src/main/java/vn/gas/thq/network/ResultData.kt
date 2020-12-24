package vn.gas.thq.network

data class ResultData<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val code: Int = 0
) {
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): ResultData<T> {
            return ResultData(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String? = null, data: T? = null, code: Int = 0): ResultData<T> {
            return ResultData(Status.ERROR, data, message, code)
        }

        fun <T> loading(data: T? = null): ResultData<T> {
            return ResultData(Status.LOADING, data, null)
        }
    }
}