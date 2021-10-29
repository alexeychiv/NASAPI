package gb.android.nasapi.domain.apod

import getDateDaysBefore

class GetApodUseCase(
    private val apodRepository: ApodRepository
) {
    suspend fun invoke(daysBefore: Long = 0): ApodDomainDataModel {
        if (daysBefore.equals(0)) {
            return apodRepository.getCurrentApod()
        } else {
            return apodRepository.getApodByDate(date = getDateDaysBefore(daysBefore))
        }
    }

}
