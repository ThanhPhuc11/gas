package vn.gas.thq.datasourse.rest


import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.hongha.ga.BuildConfig
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws


object ApiClient {
    private var token: String = "";
    fun getInstance(): Retrofit? {
        token = "token"
        val builder = OkHttpClient().newBuilder()
        // set timeout
        builder.readTimeout(60, TimeUnit.SECONDS)
        builder.connectTimeout(60, TimeUnit.SECONDS)
        builder.writeTimeout(60, TimeUnit.SECONDS)
        // set header
        builder.addNetworkInterceptor { chain: Interceptor.Chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Content-Type", "application/json; charset=utf-8")
             //   .addHeader("CommonParams", getCommonParam())
                .build()
            chain.proceed(request)
        }
        // log request
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(logging)
        }
        //OAuthInterceptor

        builder.addInterceptor(OAuthInterceptor())
        val client = builder.build()
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    class OAuthInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            // Client error responses
            // 400: Bad Request
            /* if (response.code() == ErrorCode.BAD_REQUEST) {
                new Handler(Looper.getMainLooper()).post(() ->
                        EventBus.getDefault().post(new CMErrorEvent(response.code(), "[400-BAD_REQUEST] "))
                );
            }*/
            return response
            /*var request: Request = chain.request()
            val requestBuilder: Request.Builder = request.newBuilder()

            val formBody = FormBody.Builder()
                .add("key", "123")
                .add("tel", "90301171XX")
                .build()
            var postBodyString: String = bodyToString(request.body())
            postBodyString += (if (postBodyString.length > 0) "&" else "") + bodyToString(
                formBody
            )
            request = requestBuilder
                .post(
                    RequestBody.create(
                        MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"),
                        postBodyString
                    )
                )
                .build()
            return chain.proceed(request)*/
        }
    }

    private fun getCommonParam(): String? {
        try {
            val params = JSONObject()
            params.put("test1", "test1")
            params.put("test2", "test2")
            return params.toString();
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun bodyToString(request: RequestBody?): String? {
        return try {
            val buffer = Buffer()
            if (request != null) request.writeTo(buffer) else return ""
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
        return "";
    }
}

