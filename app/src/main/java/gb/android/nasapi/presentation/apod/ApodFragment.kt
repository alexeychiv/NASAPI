package gb.android.nasapi.presentation.apod

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import getDateDaysBefore
import getTodayDate

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

        viewModel.requestApod()
        viewModel.liveDataToObserve.observe(requireActivity()) {
            render(it)
        }

        binding.layoutSearchWiki.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.etSearchWiki.text}")
            })
        }
        
        binding.chipToggleHd.setOnCheckedChangeListener { compoundButton, b ->
            if (viewModel.liveDataToObserve.value is ApodState.Success) {
                val apodState = viewModel.liveDataToObserve.value as ApodState.Success

                if (b)
                    loadApod(apodState.apodDTO.hdurl)
                else
                    loadApod(apodState.apodDTO.url)
            }
        }

        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId){
                R.id.chip_show_today_apod -> {
                    Log.d("BLAH", "CHECK CHANGE >>>>> TODAY ${getTodayDate()}")
                    viewModel.requestApod()
                }
                R.id.chip_show_yesterday_apod -> {
                    Log.d("BLAH", "CHECK CHANGE >>>>> YESTERDAY ${getDateDaysBefore(1)}")
                    viewModel.requestApod(1)
                }
                R.id.chip_show_before_yesterday_apod -> {
                    Log.d("BLAH", "CHECK CHANGE >>>>> BEFORE YESTERDAY ${getDateDaysBefore(2)}")
                    viewModel.requestApod(2)
                }
            }
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
                    if (binding.chipToggleHd.isChecked)
                        loadApod(apodState.apodDTO.hdurl)
                    else
                        loadApod(apodState.apodDTO.url)
                }
            }
        }
    }

    private fun loadApod(url: String?) {
        binding.imageView.load(url) {
            lifecycle(this@ApodFragment)
            placeholder(R.drawable.ic_apod_image_loading)
            error(R.drawable.ic_apod_image_loading_error)
        }

    }

    //===========================================================================================
    // BOTTOM SHEET

    private lateinit var bottomSheetBehaviour: BottomSheetBehavior<ConstraintLayout>

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehaviour.setPeekHeight(binding.bottomAppBar.getHeight() + 90);
    }
}