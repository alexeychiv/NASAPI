package gb.android.nasapi.domain.apod

interface ApodRepository {
    suspend fun getCurrentApod(): ApodDomainDataModel
    suspend fun getApodByDate(date: String): ApodDomainDataModel
}