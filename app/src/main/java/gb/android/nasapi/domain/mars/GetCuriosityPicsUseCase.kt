package gb.android.nasapi.domain.mars

import getDateDaysBefore

class GetCuriosityPicsUseCase(
    private val marsRepository: MarsRepository
) : MarsUseCase {
    override suspend fun invoke(): List<MarsDomainDataModel> {
        return marsRepository.getCuriosityPics(getDateDaysBefore(1))
    }
}