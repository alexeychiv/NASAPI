package gb.android.nasapi.presentation.pager.earth

import gb.android.nasapi.domain.earth.EarthDomainDataModel

sealed class EarthState {
    object Loading : EarthState()
    data class Success(val earthDomainDataModel: EarthDomainDataModel) : EarthState()
    data class Error(val error: Throwable) : EarthState()
}