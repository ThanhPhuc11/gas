package vn.gas.thq.network

import android.content.Context
import android.os.Handler
import android.os.Looper
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.gas.thq.datasourse.prefs.AppPreferencesHelper
import vn.gas.thq.model.TokenModel
import vn.gas.thq.ui.downloadApk.NeedUpgradeApkEvent
import vn.hongha.ga.BuildConfig
import vn.hongha.ga.R
import java.io.IOException
import java.io.InputStream
import java.security.*
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


object FcmRetrofitBuilder {
    private var retrofit: Retrofit? = null

    //    private const val BASE_URL = BuildConfig.BASE_URL
    private const val BASE_URL = "http://thq-gas.duckdns.org:8080/"
    private var httpClientBuilder: OkHttpClient.Builder? = null
    fun getInstance(context: Context): Retrofit? {
        val auth = Credentials.basic("user1", "user1Pass")
        if (retrofit == null) {
            httpClientBuilder = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
            httpClientBuilder?.addNetworkInterceptor(Interceptor { chain: Interceptor.Chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", auth)
                    .addHeader("Accept", "application/json")
                    .build()
                chain.proceed(request)
            })
            httpClientBuilder?.addInterceptor(OAuthInterceptor())
            initHttpLogging()
//            initSSL(context)
            val builder = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder!!.build())
            retrofit = builder.build()
        }
        return retrofit
    }

    class OAuthInterceptor : Interceptor {
        @kotlin.jvm.Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            if (response.code == 426) { // client_need_upgrade -> co phien ban moi can phai nang cap
                Handler(Looper.getMainLooper()).post(Runnable {
                    EventBus.getDefault()
                        .post(NeedUpgradeApkEvent(NeedUpgradeApkEvent.MOVE_LOGIN_SCREEN, ""))
                })
            }
            return response
        }
    }

    private fun initHttpLogging() {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        if (BuildConfig.DEBUG) httpClientBuilder?.addInterceptor(logging)
    }

    private fun initSSL(context: Context) {
        var sslContext: SSLContext? = null
        try {
            sslContext = createCertificate(context.resources.openRawResource(R.raw.cert))
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        if (sslContext != null) {
            httpClientBuilder?.sslSocketFactory(
                sslContext.socketFactory,
                systemDefaultTrustManager()
            )
        }
    }

    @Throws(
        CertificateException::class,
        IOException::class,
        KeyStoreException::class,
        KeyManagementException::class,
        NoSuchAlgorithmException::class
    )
    private fun createCertificate(trustedCertificateIS: InputStream): SSLContext {
        val cf = CertificateFactory.getInstance("X.509")
        val ca: Certificate
        ca = try {
            cf.generateCertificate(trustedCertificateIS)
        } finally {
            trustedCertificateIS.close()
        }

        // creating a KeyStore containing our trusted CAs
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)

        // creating a TrustManager that trusts the CAs in our KeyStore
        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)

        // creating an SSLSocketFactory that uses our TrustManager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, null)
        return sslContext
    }

    private fun systemDefaultTrustManager(): X509TrustManager {
        return try {
            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                "Unexpected default trust managers:" + Arrays.toString(
                    trustManagers
                )
            }
            trustManagers[0] as X509TrustManager
        } catch (e: GeneralSecurityException) {
            throw AssertionError() // The system has no TLS. Just give up.
        }
    }
}