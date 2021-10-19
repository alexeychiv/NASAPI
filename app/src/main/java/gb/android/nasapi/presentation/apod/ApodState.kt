package gb.android.nasapi.presentation.apod

import gb.android.nasapi.data.ApodDTO

sealed class ApodState {
    object Loading : ApodState()
    data class Success(val apodDTO: ApodDTO) : ApodState()
    data class Error(val error: Throwable) : ApodState()
}