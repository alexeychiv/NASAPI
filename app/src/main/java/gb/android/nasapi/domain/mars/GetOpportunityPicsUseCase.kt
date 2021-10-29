package gb.android.nasapi.domain.mars

class GetOpportunityPicsUseCase(
    private val marsRepository: MarsRepository
) : MarsUseCase {
    override suspend fun invoke(): List<MarsDomainDataModel> {
        return marsRepository.getOpportunityPicsBySol(100)
    }
}