package gb.android.nasapi.domain.ApodFragment

interface ApodRepository {
    suspend fun getCurrentApod(): ApodDomainDataModel
    suspend fun getApodByDate(date: String): ApodDomainDataModel
}