package gb.android.nasapi.data

import gb.android.nasapi.BuildConfig
import gb.android.nasapi.data.repository.DonkiAPI
import gb.android.nasapi.data.repository.DonkiData
import gb.android.nasapi.domain.weather.DonkiDomainDataModel
import gb.android.nasapi.domain.weather.DonkiRepository

class DonkiRepositoryImpl(
    private val donkiAPI: DonkiAPI
) : DonkiRepository {
    override suspend fun getDonki(): List<DonkiDomainDataModel> {
        val donkiDataListResult =
            donkiAPI.getDonkiRecentNotifications(apikey = BuildConfig.NASA_API_KEY)

        if (!donkiDataListResult.isSuccessful
            || donkiDataListResult.body() == null
            || (donkiDataListResult.body() as List<DonkiData>).size == 0
        )
            throw(Exception("ERROR: Unable to load weather notifications of the day!"))

        val donkiDomainDataModelList = mutableListOf<DonkiDomainDataModel>()

        (donkiDataListResult.body() as List<DonkiData>).forEach {
            donkiDomainDataModelList.add(mapDonkiDataToDonkiDomainDataModel(it))
        }

        return donkiDomainDataModelList
    }

    private fun mapDonkiDataToDonkiDomainDataModel(donkiData: DonkiData): DonkiDomainDataModel {
        return DonkiDomainDataModel(
            messageType = donkiData.messageType,
            messageID = donkiData.messageID,
            messageURL = donkiData.messageURL,
            messageIssueTime = donkiData.messageIssueTime,
            messageBody = donkiData.messageBody
        )
    }
}