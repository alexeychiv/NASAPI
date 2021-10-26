package gb.android.nasapi.data

import gb.android.nasapi.BuildConfig
import gb.android.nasapi.data.repository.ApodAPI
import gb.android.nasapi.data.repository.ApodData
import gb.android.nasapi.domain.apod.ApodDomainDataModel
import gb.android.nasapi.domain.apod.ApodRepository

class ApodRepositoryImpl(
    private val apodAPI: ApodAPI
) : ApodRepository {

    override suspend fun getCurrentApod(): ApodDomainDataModel {
        val apodDataResult = apodAPI.getTodayApod(BuildConfig.NASA_API_KEY)

        if (!apodDataResult.isSuccessful || apodDataResult.body() == null)
            throw(Exception("ERROR: Unable to load picture of the day!"))

        return mapApodDataToApodDomainDataModel(apodDataResult.body()!!)
    }

    override suspend fun getApodByDate(date: String): ApodDomainDataModel {
        val apodDataResult = apodAPI.getApodByDate(date, BuildConfig.NASA_API_KEY)

        if (!apodDataResult.isSuccessful || apodDataResult.body() == null)
            throw(Exception("ERROR: Unable to load picture of the day!"))

        return mapApodDataToApodDomainDataModel(apodDataResult.body()!!)
    }

    private fun mapApodDataToApodDomainDataModel(apodData: ApodData): ApodDomainDataModel {
        return ApodDomainDataModel(
            date = apodData.date,
            explanation = apodData.explanation,
            mediaType = apodData.mediaType,
            title = apodData.title,
            url = apodData.url,
            hdurl = apodData.hdurl
        )
    }
}