package gb.android.nasapi.domain.Earth

interface EarthRepository {
    suspend fun getEarth(lat: Float, lon: Float, date: String): EarthDomainDataModel
}