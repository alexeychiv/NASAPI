package gb.android.nasapi.data

import gb.android.nasapi.BuildConfig
import gb.android.nasapi.data.repository.MarsAPI
import gb.android.nasapi.data.repository.MarsData
import gb.android.nasapi.data.repository.PicData
import gb.android.nasapi.domain.mars.MarsDomainDataModel
import gb.android.nasapi.domain.mars.MarsRepository
import retrofit2.Response

class MarsRepositoryImpl(
    private val marsAPI: MarsAPI
) : MarsRepository {

    override suspend fun getCuriosityPics(date: String): List<MarsDomainDataModel> {

        val marsDataResult =
            marsAPI.getCuriosityPics(earthDate = date, apikey = BuildConfig.NASA_API_KEY)

        return processResult(marsDataResult)
    }

    override suspend fun getOpportunityPicsBySol(sol: Int): List<MarsDomainDataModel> {
        val marsDataListResult =
            marsAPI.getOpportunityPicsBySol(sol = sol, apikey = BuildConfig.NASA_API_KEY)

        return processResult(marsDataListResult)
    }

    override suspend fun getSpiritPicsBySol(sol: Int): List<MarsDomainDataModel> {
        val marsDataListResult =
            marsAPI.getSpiritPicsBySol(sol = sol, apikey = BuildConfig.NASA_API_KEY)

        return processResult(marsDataListResult)
    }


    //==================================================================================================
    // UTILS

    private fun processResult(marsDataListResult: Response<MarsData>): List<MarsDomainDataModel> {
        if (!marsDataListResult.isSuccessful
            || marsDataListResult.body() == null
            || (marsDataListResult.body() as MarsData).photos.size == 0
        )
            throw(Exception("ERROR: Unable to load mars pictures!"))

        val marsDomainDataModelList = mutableListOf<MarsDomainDataModel>()

        (marsDataListResult.body() as MarsData).photos.forEach {
            marsDomainDataModelList.add(mapPicDataToMarsDomainDataModel(it))
        }

        return marsDomainDataModelList
    }


    private fun mapPicDataToMarsDomainDataModel(picData: PicData): MarsDomainDataModel {
        return MarsDomainDataModel(
            img_src = picData.img_src,
            earth_date = picData.earth_date
        )
    }
}