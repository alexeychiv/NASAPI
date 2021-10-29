package gb.android.nasapi.presentation.pager.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gb.android.nasapi.BuildConfig
import gb.android.nasapi.domain.weather.GetDonkiUseCase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class WeatherViewModel(
    private val getDonkiUseCase: GetDonkiUseCase
) : ViewModel(), CoroutineScope {
    private val liveDataToObserveMutable: MutableLiveData<WeatherState> = MutableLiveData()
    val liveDataToObserve: LiveData<WeatherState>
        get() = liveDataToObserveMutable

    //=============================================================================================
    //COROUTINES

    //COROUTINE EXCEPTION HANDLER
    private val handler = CoroutineExceptionHandler { _, exception ->
        liveDataToObserveMutable.value = WeatherState.Error(Throwable(exception.message))
    }

    //COROUTINE SCOPE
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + handler

    //=============================================================================================
    //LIFECYCLE EVENTS

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }

    //=============================================================================================

    public fun requestWeather() {
        liveDataToObserveMutable.value = WeatherState.Loading

        if (BuildConfig.NASA_API_KEY.isNullOrBlank()) {
            liveDataToObserveMutable.value =
                WeatherState.Error(Throwable("ERROR: API KEY REQUIRED!"))
        } else launch {
            val weatherDomainDataModelList = getDonkiUseCase.invoke()

            liveDataToObserveMutable.value = WeatherState.Success(weatherDomainDataModelList)
        }
    }
}