package gb.android.nasapi.domain.earth

import getDateDaysBefore

class GetEarthUseCase(
    private val earthRepository: EarthRepository
) {
    suspend fun invoke(lat: Float, lon: Float, daysBefore: Long): EarthDomainDataModel {
        return earthRepository.getEarth(lat = lat, lon = lon, getDateDaysBefore(daysBefore))
    }
}