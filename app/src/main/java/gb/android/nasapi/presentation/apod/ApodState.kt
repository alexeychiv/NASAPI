package gb.android.nasapi.presentation.apod

import gb.android.nasapi.domain.ApodFragment.ApodDomainDataModel

sealed class ApodState {
    object Loading : ApodState()
    data class SuccessImage(val apodDomainDataModel: ApodDomainDataModel) : ApodState()
    data class SuccessVideo(val apodDomainDataModel: ApodDomainDataModel) : ApodState()
    data class Error(val error: Throwable) : ApodState()
}