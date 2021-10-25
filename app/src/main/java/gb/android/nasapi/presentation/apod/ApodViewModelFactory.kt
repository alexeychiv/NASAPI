package gb.android.nasapi.presentation.apod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gb.android.nasapi.data.ApodRepositoryImpl
import gb.android.nasapi.data.repository.RetrofitBuilder
import gb.android.nasapi.domain.ApodFragment.GetApodUseCase

class ApodViewModelFactory : ViewModelProvider.Factory {

    private val apodAPI by lazy(LazyThreadSafetyMode.NONE) { RetrofitBuilder().buildApodApi() }
    private val apodRepository by lazy(LazyThreadSafetyMode.NONE) { ApodRepositoryImpl(apodAPI) }
    private val getApodUseCase by lazy(LazyThreadSafetyMode.NONE) { GetApodUseCase(apodRepository = apodRepository) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ApodViewModel(
            getApodUseCase = getApodUseCase
        ) as T
    }
}