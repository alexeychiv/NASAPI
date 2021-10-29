package gb.android.nasapi.data.api

import gb.android.nasapi.data.models.ApodData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApodAPI {

    @GET("planetary/apod")
    suspend fun getTodayApod(
        @Query("api_key") apikey: String
    ): Response<ApodData>

    @GET("planetary/apod")
    suspend fun getApodByDate(
        @Query("date") date: String,
        @Query("api_key") apikey: String
    ): Response<ApodData>
}