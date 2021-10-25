package gb.android.nasapi.presentation.apod

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import gb.android.nasapi.R
import gb.android.nasapi.databinding.FragmentApodBinding
import gb.android.nasapi.presentation.MainActivity
import gb.android.nasapi.presentation.themes.ThemesFragment

class ApodFragment : Fragment() {

    //===========================================================================================
    // BINDING

    private var _binding: FragmentApodBinding? = null
    private val binding: FragmentApodBinding
        get() = _binding!!

    //===========================================================================================
    // VIEW MODEL

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, ApodViewModelFactory())
            .get(ApodViewModel::class.java)
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

        setupBottomAppBar(view)

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

        binding.chipToggleHd.setOnCheckedChangeListener { compoundButton, isHdToggled ->
            if (viewModel.liveDataToObserve.value is ApodState.SuccessImage) {
                val apodState = viewModel.liveDataToObserve.value as ApodState.SuccessImage

                if (isHdToggled)
                    loadApod(apodState.apodDomainDataModel.hdurl)
                else
                    loadApod(apodState.apodDomainDataModel.url)
            }
        }

        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_show_today_apod -> {
                    viewModel.requestApod()
                }
                R.id.chip_show_yesterday_apod -> {
                    viewModel.requestApod(1)
                }
                R.id.chip_show_before_yesterday_apod -> {
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
    // BOTTOM APP BAR

    private fun setupBottomAppBar(view: View) {
        (activity as MainActivity).setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)
    }

    //===========================================================================================
    // BOTTOM APP BAR MENU

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open_fragment_themes -> {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, ThemesFragment.newInstance())
                    ?.addToBackStack("")
                    ?.commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    //===========================================================================================
    // RENDER

    private fun render(apodState: ApodState) {
        when (apodState) {
            is ApodState.Error -> {
                binding.bottomSheet.bottomSheetContainer.visibility = View.GONE
                Snackbar
                    .make(
                        binding.main,
                        "${apodState.error.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
            }
            ApodState.Loading -> {
                binding.bottomSheet.bottomSheetContainer.visibility = View.GONE
                Snackbar
                    .make(
                        binding.main,
                        "LOADING....",
                        Snackbar.LENGTH_LONG
                    ).show()
            }
            is ApodState.SuccessImage -> {
                binding.bottomSheet.bottomSheetContainer.visibility = View.VISIBLE
                binding.bottomSheet.bottomSheetDescriptionHeader.text =
                    apodState.apodDomainDataModel.title
                binding.bottomSheet.bottomSheetExplanation.text =
                    apodState.apodDomainDataModel.explanation

                if (!apodState.apodDomainDataModel.url.isNullOrBlank()) {
                    if (binding.chipToggleHd.isChecked)
                        loadApod(apodState.apodDomainDataModel.hdurl)
                    else
                        loadApod(apodState.apodDomainDataModel.url)
                }
            }
            is ApodState.SuccessVideo -> {
                // TODO: VideoPlayer
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

    private lateinit var bottomSheetBehaviour: BottomSheetBehavior<LinearLayout>

    private fun setBottomSheetBehavior(bottomSheet: LinearLayout) {
        bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehaviour.setPeekHeight(binding.bottomAppBar.getHeight() + 140);
    }
}