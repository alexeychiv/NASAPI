package gb.android.nasapi.domain.weather

class GetDonkiUseCase(
    private val donkiRepository: DonkiRepository
) {
    suspend fun invoke(): List<DonkiDomainDataModel> {
        return donkiRepository.getDonki()
    }
}