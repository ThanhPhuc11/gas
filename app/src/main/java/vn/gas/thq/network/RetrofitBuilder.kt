package vn.gas.thq.network

import android.R
import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLContext


object RetrofitBuilder {
//    private const val BASE_URL = "https://5e510330f2c0d300147c034c.mockapi.io/"
    private const val BASE_URL = "https://thq-gas.duckdns.org:8090/thq-gas/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build() //Doesn't require the adapter
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)


//    private fun initSSL(context: Context) {
//        var sslContext: SSLContext? = null
//        try {
//            sslContext = createCertificate(context.getResources().openRawResource(R.raw.cert))
//        } catch (e: CertificateException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } catch (e: KeyStoreException) {
//            e.printStackTrace()
//        } catch (e: KeyManagementException) {
//            e.printStackTrace()
//        } catch (e: NoSuchAlgorithmException) {
//            e.printStackTrace()
//        }
//        if (sslContext != null) {
//            httpClientBuilder.sslSocketFactory(
//                sslContext.getSocketFactory(),
//                systemDefaultTrustManager()
//            )
//        }
//    }
//
//    @Throws(
//        CertificateException::class,
//        IOException::class,
//        KeyStoreException::class,
//        KeyManagementException::class,
//        NoSuchAlgorithmException::class
//    )
//    private fun createCertificate(trustedCertificateIS: InputStream): SSLContext? {
//        val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
//        val ca: Certificate
//        ca = try {
//            cf.generateCertificate(trustedCertificateIS)
//        } finally {
//            trustedCertificateIS.close()
//        }
//
//        // creating a KeyStore containing our trusted CAs
//        val keyStoreType: String = KeyStore.getDefaultType()
//        val keyStore: KeyStore = KeyStore.getInstance(keyStoreType)
//        keyStore.load(null, null)
//        keyStore.setCertificateEntry("ca", ca)
//
//        // creating a TrustManager that trusts the CAs in our KeyStore
//        val tmfAlgorithm: String = TrustManagerFactory.getDefaultAlgorithm()
//        val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm)
//        tmf.init(keyStore)
//
//        // creating an SSLSocketFactory that uses our TrustManager
//        val sslContext: SSLContext = SSLContext.getInstance("TLS")
//        sslContext.init(null, tmf.getTrustManagers(), null)
//        return sslContext
//    }
//
//    private fun systemDefaultTrustManager(): X509TrustManager? {
//        return try {
//            val trustManagerFactory: TrustManagerFactory =
//                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
//            trustManagerFactory.init(null as KeyStore?)
//            val trustManagers: Array<TrustManager> = trustManagerFactory.getTrustManagers()
//            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
//                "Unexpected default trust managers:" + Arrays.toString(
//                    trustManagers
//                )
//            }
//            trustManagers[0] as X509TrustManager
//        } catch (e: GeneralSecurityException) {
//            throw AssertionError() // The system has no TLS. Just give up.
//        }
//    }
}