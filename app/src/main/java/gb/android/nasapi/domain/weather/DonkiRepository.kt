package gb.android.nasapi.domain.weather

interface DonkiRepository {
    suspend fun getDonki(): List<DonkiDomainDataModel>
}