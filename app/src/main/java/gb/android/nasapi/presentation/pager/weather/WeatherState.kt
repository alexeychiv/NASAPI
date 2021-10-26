package gb.android.nasapi.presentation.pager.weather

import gb.android.nasapi.domain.weather.DonkiDomainDataModel

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val donkiDomainDataModelList: List<DonkiDomainDataModel>) : WeatherState()
    data class Error(val error: Throwable) : WeatherState()
}