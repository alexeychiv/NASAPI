package gb.android.nasapi.presentation.pager.earth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.google.android.material.snackbar.Snackbar
import gb.android.nasapi.R
import gb.android.nasapi.databinding.FragmentEarthBinding
import gb.android.nasapi.presentation.pager.mars.*

class EarthFragment : Fragment() {

    //===========================================================================================
    // BINDING

    private var _binding: FragmentEarthBinding? = null
    private val binding: FragmentEarthBinding
        get() = _binding!!

    //===========================================================================================
    // VIEW MODEL

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, EarthViewModelFactory())
            .get(EarthViewModel::class.java)
    }

    //===========================================================================================
    // COMPANION

    companion object {
        fun newInstance() = EarthFragment()
    }

    //===========================================================================================
    // LIFECYCLE EVENTS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEarthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.requestEarth(1.5f, 100.75f, 100)
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

    private fun render(earthState: EarthState) {
        when (earthState) {
            is EarthState.Error -> {
                Snackbar
                    .make(
                        binding.root,
                        "${earthState.error.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
            }
            EarthState.Loading -> {
                Snackbar
                    .make(
                        binding.root,
                        "LOADING....",
                        Snackbar.LENGTH_SHORT
                    ).show()
            }
            is EarthState.Success -> {
                binding.ivEarth.load(earthState.earthDomainDataModel.url) {
                    lifecycle(this@EarthFragment)
                    placeholder(R.drawable.ic_apod_image_loading)
                    error(R.drawable.ic_broken_image)
                }
            }
        }
    }
}