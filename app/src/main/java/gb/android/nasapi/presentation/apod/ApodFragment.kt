package gb.android.nasapi.presentation.apod

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import gb.android.nasapi.R
import gb.android.nasapi.databinding.FragmentApodBinding

class ApodFragment : Fragment() {

    //===========================================================================================
    // BINDING

    private var _binding: FragmentApodBinding? = null
    private val binding: FragmentApodBinding
        get() = _binding!!

    //===========================================================================================
    // VIEW MODEL

    private val viewModel: ApodViewModel by lazy {
        ViewModelProvider(this).get(ApodViewModel::class.java)
    }

    //===========================================================================================
    // COMPANION

    companion object {
        fun newInstance() = ApodFragment()
    }

    //===========================================================================================
    // LIFECYCLE EVENTS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomSheetBehavior(binding.bottomSheet.bottomSheetContainer)

        viewModel.liveDataToObserve.observe(requireActivity()) {
            render(it)
        }

        binding.layoutSearchWiki.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.etSearchWiki.text}")
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //===========================================================================================
    // RENDER

    private fun render(apodState: ApodState) {
        when (apodState) {
            is ApodState.Error -> {
                Snackbar
                    .make(
                        binding.root,
                        "Error loading picture of the day!",
                        Snackbar.LENGTH_LONG
                    )
            }
            ApodState.Loading -> {
                Snackbar
                    .make(
                        binding.root,
                        "LOADING....",
                        Snackbar.LENGTH_LONG
                    )
            }
            is ApodState.Success -> {
                binding.bottomSheet.bottomSheetDescriptionHeader.text = apodState.apodDTO.title
                binding.bottomSheet.bottomSheetDescription.text = apodState.apodDTO.explanation

                if (!apodState.apodDTO.url.isNullOrBlank()) {
                    binding.imageView.load(apodState.apodDTO.url) {
                        lifecycle(this@ApodFragment)
                        placeholder(R.drawable.ic_apod_image_loading)
                        error(R.drawable.ic_apod_image_loading_error)
                    }
                }


            }
        }
    }

    //===========================================================================================
    // BOTTOM SHEET

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
}