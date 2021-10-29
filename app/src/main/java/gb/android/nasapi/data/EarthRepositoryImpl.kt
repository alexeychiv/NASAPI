package gb.android.nasapi.data

import gb.android.nasapi.BuildConfig
import gb.android.nasapi.data.api.EarthAPI
import gb.android.nasapi.data.models.EarthData
import gb.android.nasapi.domain.Earth.EarthDomainDataModel
import gb.android.nasapi.domain.Earth.EarthRepository

class EarthRepositoryImpl(
    private val earthAPI: EarthAPI
) : EarthRepository {

    override suspend fun getEarth(lat: Float, lon: Float, date: String): EarthDomainDataModel {
        val earthDataResult =
            earthAPI.getEarth(lat = lat, lon = lon, date = date, apikey = BuildConfig.NASA_API_KEY)

        if (!earthDataResult.isSuccessful
            || earthDataResult.body() == null
        )
            throw(Exception("ERROR: Unable to load Earth picture!"))

        return mapEarthDataToEarthDomainDataModel(earthDataResult.body() as EarthData)
    }

    private fun mapEarthDataToEarthDomainDataModel(earthData: EarthData): EarthDomainDataModel {
        return EarthDomainDataModel(
            date = earthData.date,
            url = earthData.url
        )
    }
}