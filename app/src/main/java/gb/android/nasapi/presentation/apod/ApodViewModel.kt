package gb.android.nasapi.presentation.apod

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gb.android.nasapi.BuildConfig
import gb.android.nasapi.domain.apod.ApodDomainDataModel
import gb.android.nasapi.domain.apod.GetApodUseCase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ApodViewModel(
    private val getApodUseCase: GetApodUseCase
) : ViewModel(), CoroutineScope {

    private val liveDataToObserveMutable: MutableLiveData<ApodState> = MutableLiveData()
    val liveDataToObserve: LiveData<ApodState>
        get() = liveDataToObserveMutable

    //=============================================================================================
    //COROUTINES

    //COROUTINE EXCEPTION HANDLER
    private val handler = CoroutineExceptionHandler { _, exception ->
        liveDataToObserveMutable.value = ApodState.Error(Throwable(exception.message))
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

    public fun requestApod(daysBefore: Long = 0) {
        liveDataToObserveMutable.value = ApodState.Loading

        if (BuildConfig.NASA_API_KEY.isNullOrBlank()) {
            liveDataToObserveMutable.value = ApodState.Error(Throwable("ERROR: API KEY REQUIRED!"))
        } else launch {
            val apodDomainDataModel: ApodDomainDataModel = getApodUseCase.execute(daysBefore)

            if (apodDomainDataModel.mediaType == "image")
                liveDataToObserveMutable.value =
                    ApodState.SuccessImage(apodDomainDataModel = apodDomainDataModel)
            else
                liveDataToObserveMutable.value =
                    ApodState.SuccessVideo(apodDomainDataModel = apodDomainDataModel)
        }
    }


}