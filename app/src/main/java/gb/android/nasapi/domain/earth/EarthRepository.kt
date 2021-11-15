package gb.android.nasapi.domain.earth

interface EarthRepository {
    suspend fun getEarth(lat: Float, lon: Float, date: String): EarthDomainDataModel
}