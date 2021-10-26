package gb.android.nasapi.data.repository

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface EarthAPI {
    @GET("planetary/earth/assets")
    suspend fun getEarth(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float,
        @Query("dim") dim: Float = 0.9f,
        @Query("date") date: String,
        @Query("api_key") apikey: String
    ): Response<EarthData>
}