package gb.android.nasapi.presentation.pager.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import gb.android.nasapi.databinding.FragmentWeatherBinding

class WeatherFragment : Fragment() {

    //===========================================================================================
    // BINDING

    private var _binding: FragmentWeatherBinding? = null
    private val binding: FragmentWeatherBinding
        get() = _binding!!

    //===========================================================================================
    // VIEW MODEL

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, WeatherViewModelFactory())
            .get(WeatherViewModel::class.java)
    }

    //===========================================================================================
    // RECYCLER VIEW ADAPTER

    private val adapter: WeatherAdapter by lazy {
        WeatherAdapter()
    }

    //===========================================================================================
    // COMPANION

    companion object {
        fun newInstance() = WeatherFragment()
    }

    //===========================================================================================
    // LIFECYCLE EVENTS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvWeather.adapter = adapter

        viewModel.requestWeather()
        viewModel.liveDataToObserve.observe(requireActivity()) {
            render(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //===========================================================================================
    // RENDER

    private fun render(weatherState: WeatherState) {
        when (weatherState) {
            is WeatherState.Error -> {
                Snackbar
                    .make(
                        binding.root,
                        "${weatherState.error.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
            }
            WeatherState.Loading -> {
                Snackbar
                    .make(
                        binding.root,
                        "LOADING....",
                        Snackbar.LENGTH_SHORT
                    ).show()
            }
            is WeatherState.Success -> {
                adapter.setWeather(weatherState.donkiDomainDataModelList)
            }
        }
    }
}