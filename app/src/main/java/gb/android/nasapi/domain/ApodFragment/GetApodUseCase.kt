package gb.android.nasapi.domain.ApodFragment

import getDateDaysBefore

class GetApodUseCase(
    private val apodRepository: ApodRepository
) {
    suspend fun execute(daysBefore: Long = 0): ApodDomainDataModel {
        if (daysBefore.equals(0)) {
            return apodRepository.getCurrentApod()
        } else {
            return apodRepository.getApodByDate(date = getDateDaysBefore(daysBefore))
        }
    }

}
