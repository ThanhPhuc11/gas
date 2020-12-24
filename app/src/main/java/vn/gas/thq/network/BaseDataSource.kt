package vn.gas.thq.network

import retrofit2.Response
import java.lang.Exception

abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): ResultData<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                return ResultData.success(body)
            }
            return error(" ${response.code()} ${response.message()}")

        } catch (e: Exception) {
//            if(e.message != null && e.message!!.contains("BEGIN_OBJECT but was BEGIN_ARRAY")){
//                return Result.error(code = 1)
//            }
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): ResultData<T> {

        return ResultData.error("Network call has failed for a following reason: $message")
    }
}