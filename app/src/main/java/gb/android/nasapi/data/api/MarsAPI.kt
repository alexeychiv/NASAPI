package gb.android.nasapi.data.api

import gb.android.nasapi.data.models.MarsData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarsAPI {


    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    suspend fun getCuriosityPicsBySol(
        @Query("sol") sol: Int = 100,
        @Query("api_key") apikey: String
    ): Response<MarsData>

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    suspend fun getCuriosityPics(
        @Query("earth_date") earthDate: String,
        @Query("api_key") apikey: String
    ): Response<MarsData>


    @GET("mars-photos/api/v1/rovers/Opportunity/photos")
    suspend fun getOpportunityPicsBySol(
        @Query("sol") sol: Int = 100,
        @Query("api_key") apikey: String
    ): Response<MarsData>

    @GET("mars-photos/api/v1/rovers/Opportunity/photos")
    suspend fun getOpportunityPics(
        @Query("earth_date") earthDate: String,
        @Query("api_key") apikey: String
    ): Response<MarsData>


    @GET("mars-photos/api/v1/rovers/Spirit/photos")
    suspend fun getSpiritPicsBySol(
        @Query("sol") sol: Int = 100,
        @Query("api_key") apikey: String
    ): Response<MarsData>

    @GET("mars-photos/api/v1/rovers/Spirit/photos")
    suspend fun getSpiritPics(
        @Query("earth_date") earthDate: String,
        @Query("api_key") apikey: String
    ): Response<MarsData>
}