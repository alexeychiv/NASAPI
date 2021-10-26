package gb.android.nasapi.domain.mars

interface MarsUseCase {
    suspend fun execute(): List<MarsDomainDataModel>
}