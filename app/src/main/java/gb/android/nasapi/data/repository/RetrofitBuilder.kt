package gb.android.nasapi.data.repository

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitBuilder {

    private val baseUrl = "https://api.nasa.gov/"

    fun buildApodApi(): ApodAPI {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(createOkHttpClient(/*ApodInterceptor()*/))
            .build()
            .create(ApodAPI::class.java)
    }

    private fun createOkHttpClient(/*interceptor: Interceptor*/): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        //httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }

    /*class ApodInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            return chain.proceed(chain.request())
        }
    }*/

}
