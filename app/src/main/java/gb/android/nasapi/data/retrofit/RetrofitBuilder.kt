package gb.android.nasapi.data.retrofit

import com.google.gson.GsonBuilder
import gb.android.nasapi.data.api.ApodAPI
import gb.android.nasapi.data.api.DonkiAPI
import gb.android.nasapi.data.api.EarthAPI
import gb.android.nasapi.data.api.MarsAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitBuilder {

    private const val baseUrl = "https://api.nasa.gov/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .client(
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .build()
        )
        .build()

    fun buildApodAPI(): ApodAPI {
        return retrofit.create(ApodAPI::class.java)
    }

    fun buildDonkiAPI(): DonkiAPI {
        return retrofit.create(DonkiAPI::class.java)
    }

    fun buildMarsAPI(): MarsAPI {
        return retrofit.create(MarsAPI::class.java)
    }

    fun buildEarthAPI(): EarthAPI {
        return retrofit.create(EarthAPI::class.java)
    }
}
