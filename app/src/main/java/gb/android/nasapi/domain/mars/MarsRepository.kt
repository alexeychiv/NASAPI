package gb.android.nasapi.domain.mars

interface MarsRepository {

    suspend fun getCuriosityPics(date: String): List<MarsDomainDataModel>
    suspend fun getOpportunityPicsBySol(sol: Int): List<MarsDomainDataModel>
    suspend fun getSpiritPicsBySol(sol: Int): List<MarsDomainDataModel>
}