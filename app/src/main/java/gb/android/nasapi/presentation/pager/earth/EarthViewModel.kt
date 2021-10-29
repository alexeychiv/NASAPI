package gb.android.nasapi.presentation.pager.earth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gb.android.nasapi.BuildConfig
import gb.android.nasapi.domain.Earth.GetEarthUseCase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class EarthViewModel(
    private val getEarthUseCase: GetEarthUseCase
) : ViewModel(), CoroutineScope {

    private val liveDataToObserveMutable: MutableLiveData<EarthState> = MutableLiveData()
    val liveDataToObserve: LiveData<EarthState>
        get() = liveDataToObserveMutable

    //=============================================================================================
    //COROUTINES

    //COROUTINE EXCEPTION HANDLER
    private val handler = CoroutineExceptionHandler { _, exception ->
        liveDataToObserveMutable.value = EarthState.Error(Throwable(exception.message))
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

    fun requestEarth(lat: Float, lon: Float, daysBefore: Long) {
        liveDataToObserveMutable.value = EarthState.Loading

        if (BuildConfig.NASA_API_KEY.isNullOrBlank()) {
            liveDataToObserveMutable.value =
                EarthState.Error(Throwable("ERROR: API KEY REQUIRED!"))
        } else launch {
            val earthDomainDataModel =
                getEarthUseCase.invoke(lon = lon, lat = lat, daysBefore = daysBefore)

            liveDataToObserveMutable.value = EarthState.Success(earthDomainDataModel)
        }
    }

}