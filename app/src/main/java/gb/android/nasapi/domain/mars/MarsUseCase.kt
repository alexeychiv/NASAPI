package gb.android.nasapi.domain.mars

interface MarsUseCase {
    suspend fun invoke(): List<MarsDomainDataModel>
}