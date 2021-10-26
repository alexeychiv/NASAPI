package gb.android.nasapi.domain.mars

class GetSpiritPicsUseCase(
    private val marsRepository: MarsRepository
) : MarsUseCase {
    override suspend fun execute(): List<MarsDomainDataModel> {
        return marsRepository.getSpiritPicsBySol(100)
    }
}