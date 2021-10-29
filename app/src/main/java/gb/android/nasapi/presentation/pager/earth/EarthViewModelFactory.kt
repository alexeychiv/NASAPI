package gb.android.nasapi.presentation.pager.earth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gb.android.nasapi.data.EarthRepositoryImpl
import gb.android.nasapi.data.retrofit.RetrofitBuilder
import gb.android.nasapi.domain.Earth.GetEarthUseCase

class EarthViewModelFactory : ViewModelProvider.Factory {

    private val earthAPI by lazy(LazyThreadSafetyMode.NONE) { RetrofitBuilder.buildEarthAPI() }
    private val earthRepository by lazy(LazyThreadSafetyMode.NONE) { EarthRepositoryImpl(earthAPI) }
    private val getEarthUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetEarthUseCase(
            earthRepository = earthRepository
        )
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EarthViewModel(
            getEarthUseCase = getEarthUseCase
        ) as T
    }
}