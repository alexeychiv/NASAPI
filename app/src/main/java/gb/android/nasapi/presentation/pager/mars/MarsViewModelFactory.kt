package gb.android.nasapi.presentation.pager.mars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gb.android.nasapi.data.MarsRepositoryImpl
import gb.android.nasapi.data.retrofit.RetrofitBuilder
import gb.android.nasapi.domain.mars.GetCuriosityPicsUseCase
import gb.android.nasapi.domain.mars.GetOpportunityPicsUseCase
import gb.android.nasapi.domain.mars.GetSpiritPicsUseCase

class MarsViewModelFactory : ViewModelProvider.Factory {

    private val marsAPI by lazy(LazyThreadSafetyMode.NONE) { RetrofitBuilder.buildMarsAPI() }
    private val marsRepository by lazy(LazyThreadSafetyMode.NONE) { MarsRepositoryImpl(marsAPI) }
    private val getCuriosityPicsUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetCuriosityPicsUseCase(
            marsRepository = marsRepository
        )
    }
    private val getOpportunityPicsUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetOpportunityPicsUseCase(
            marsRepository = marsRepository
        )
    }
    private val getSpiritPicsUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetSpiritPicsUseCase(
            marsRepository = marsRepository
        )
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MarsViewModel(
            getCuriosityPicsUseCase = getCuriosityPicsUseCase,
            getOpportunityPicsUseCase = getOpportunityPicsUseCase,
            getSpiritPicsUseCase = getSpiritPicsUseCase
        ) as T
    }
}