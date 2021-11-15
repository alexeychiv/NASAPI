package gb.android.nasapi.presentation.apod

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gb.android.nasapi.data.ApodRepositoryImpl
import gb.android.nasapi.data.retrofit.RetrofitBuilder
import gb.android.nasapi.domain.apod.GetApodUseCase

class ApodViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    private val apodAPI by lazy(LazyThreadSafetyMode.NONE) { RetrofitBuilder.buildApodAPI() }
    private val apodRepository by lazy(LazyThreadSafetyMode.NONE) { ApodRepositoryImpl(context, apodAPI) }
    private val getApodUseCase by lazy(LazyThreadSafetyMode.NONE) { GetApodUseCase(apodRepository = apodRepository) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ApodViewModel(
            getApodUseCase = getApodUseCase
        ) as T
    }
}