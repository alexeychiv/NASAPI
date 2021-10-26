package gb.android.nasapi.presentation.pager.mars

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gb.android.nasapi.BuildConfig
import gb.android.nasapi.domain.mars.GetCuriosityPicsUseCase
import gb.android.nasapi.domain.mars.GetOpportunityPicsUseCase
import gb.android.nasapi.domain.mars.GetSpiritPicsUseCase
import gb.android.nasapi.domain.mars.MarsUseCase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MarsViewModel(
    private val getCuriosityPicsUseCase: GetCuriosityPicsUseCase,
    private val getOpportunityPicsUseCase: GetOpportunityPicsUseCase,
    private val getSpiritPicsUseCase: GetSpiritPicsUseCase
) : ViewModel(), CoroutineScope {

    private val liveDataToObserveMutable: MutableLiveData<MarsState> = MutableLiveData()
    val liveDataToObserve: LiveData<MarsState>
        get() = liveDataToObserveMutable

    //=============================================================================================
    //COROUTINES

    //COROUTINE EXCEPTION HANDLER
    private val handler = CoroutineExceptionHandler { _, exception ->
        liveDataToObserveMutable.value = MarsState.Error(Throwable(exception.message))
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

    private fun requestPics(marsUseCase: MarsUseCase) {
        liveDataToObserveMutable.value = MarsState.Loading

        if (BuildConfig.NASA_API_KEY.isNullOrBlank()) {
            liveDataToObserveMutable.value =
                MarsState.Error(Throwable("ERROR: API KEY REQUIRED!"))
        } else launch {
            val apodDomainDataModelList = marsUseCase.execute()

            liveDataToObserveMutable.value = MarsState.Success(apodDomainDataModelList)
        }
    }

    public fun requestCuriosityPics() {
        requestPics(getCuriosityPicsUseCase)
    }

    public fun requestOpportunityPics() {
        requestPics(getOpportunityPicsUseCase)
    }

    public fun requestSpiritPics() {
        requestPics(getSpiritPicsUseCase)
    }
}