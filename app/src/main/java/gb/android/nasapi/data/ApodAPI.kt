package gb.android.nasapi.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApodAPI {

    @GET("planetary/apod")
    fun getApod(
        @Query("api_key") apikey: String
    ): Call<ApodDTO>
}