package gb.android.nasapi.presentation.pager.mars

import gb.android.nasapi.domain.mars.MarsDomainDataModel

sealed class MarsState {
    object Loading : MarsState()
    data class Success(val marsDomainDataModelList: List<MarsDomainDataModel>) : MarsState()
    data class Error(val error: Throwable) : MarsState()
}