package gb.android.nasapi.presentation.apod

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gb.android.nasapi.BuildConfig
import gb.android.nasapi.data.ApodDTO
import gb.android.nasapi.data.ApodRetrofitImpl
import getDateDaysBefore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApodViewModel : ViewModel() {

    private val apodSource: ApodRetrofitImpl = ApodRetrofitImpl()

    private val liveDataToObserveMutable: MutableLiveData<ApodState> = MutableLiveData()
    val liveDataToObserve: LiveData<ApodState>
        get() = liveDataToObserveMutable

    public fun requestApod(daysBefore: Long = 0) {
        liveDataToObserveMutable
            .postValue(ApodState.Loading)

        if (BuildConfig.NASA_API_KEY.isNullOrBlank())
            ApodState.Error(Throwable("ERROR: API KEY REQUIRED!"))
        else {
            apodSource
                .getRetrofitImpl()
                .getApodByDate(getDateDaysBefore(daysBefore), BuildConfig.NASA_API_KEY)
                .enqueue(object : Callback<ApodDTO> {
                    override fun onResponse(call: Call<ApodDTO>, response: Response<ApodDTO>) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataToObserveMutable.value =
                                ApodState.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                liveDataToObserveMutable.value =
                                    ApodState.Error(Throwable("Unidentified error"))
                            } else {
                                liveDataToObserveMutable.value =
                                    ApodState.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<ApodDTO>, t: Throwable) {
                        liveDataToObserveMutable.value =
                            ApodState.Error(Throwable(t))
                    }
                }
                )
        }
    }


}