package gb.android.nasapi.data.repository

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface DonkiAPI {
    @GET("DONKI/notifications")
    suspend fun getDonkiRecentNotifications(
        @Query("type") type: String = "all",
        @Query("api_key") apikey: String
    ): Response<List<DonkiData>>
}