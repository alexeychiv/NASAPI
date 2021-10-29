package gb.android.nasapi.presentation.pager.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gb.android.nasapi.data.DonkiRepositoryImpl
import gb.android.nasapi.data.retrofit.RetrofitBuilder
import gb.android.nasapi.domain.weather.GetDonkiUseCase

class WeatherViewModelFactory : ViewModelProvider.Factory {

    private val donkiAPI by lazy(LazyThreadSafetyMode.NONE) { RetrofitBuilder.buildDonkiAPI() }
    private val donkiRepository by lazy(LazyThreadSafetyMode.NONE) { DonkiRepositoryImpl(donkiAPI) }
    private val getDonkiUseCase by lazy(LazyThreadSafetyMode.NONE) { GetDonkiUseCase(donkiRepository = donkiRepository) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(
            getDonkiUseCase = getDonkiUseCase
        ) as T
    }
}