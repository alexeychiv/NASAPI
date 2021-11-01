package gb.android.nasapi.presentation.apod

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
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

        setBottomSheetBehavior(binding.bottomSheet.bottomSheetContainer)

        viewModel.requestApod()
        viewModel.liveDataToObserve.observe(requireActivity()) {
            render(it)
        }

        binding.layoutSearchWiki.layoutSearchWiki.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.layoutSearchWiki.etSearchWiki.text}")
            })
        }

        binding.chipToggleHd.setOnCheckedChangeListener { compoundButton, isHdToggled ->
            if (viewModel.liveDataToObserve.value is ApodState.SuccessImage) {
                val apodState = viewModel.liveDataToObserve.value as ApodState.SuccessImage

                if (isHdToggled)
                    loadPicture(apodState.apodDomainDataModel.hdurl)
                else
                    loadPicture(apodState.apodDomainDataModel.url)
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
    // RENDER

    private fun render(apodState: ApodState) {
        when (apodState) {
            is ApodState.Error -> {
                binding.bottomSheet.bottomSheetContainer.visibility = View.GONE
                binding.youtubePlayerView.visibility = View.GONE
                Snackbar
                    .make(
                        binding.main,
                        "${apodState.error.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
            }
            ApodState.Loading -> {
                binding.imageView.visibility = View.VISIBLE
                binding.youtubePlayerView.visibility = View.GONE

                binding.bottomSheet.bottomSheetContainer.visibility = View.GONE
                Snackbar
                    .make(
                        binding.main,
                        "LOADING....",
                        Snackbar.LENGTH_SHORT
                    ).show()
            }
            is ApodState.SuccessImage -> {
                binding.imageView.visibility = View.VISIBLE
                binding.youtubePlayerView.visibility = View.GONE


                showBottomSheet(
                    apodState.apodDomainDataModel.title,
                    apodState.apodDomainDataModel.explanation
                )

                binding.bottomSheet.bottomSheetContainer.visibility = View.VISIBLE
                binding.bottomSheet.bottomSheetDescriptionHeader.text =
                    apodState.apodDomainDataModel.title
                binding.bottomSheet.bottomSheetExplanation.text =
                    apodState.apodDomainDataModel.explanation

                if (!apodState.apodDomainDataModel.url.isNullOrBlank()) {
                    if (binding.chipToggleHd.isChecked)
                        loadPicture(apodState.apodDomainDataModel.hdurl)
                    else
                        loadPicture(apodState.apodDomainDataModel.url)
                }
            }
            is ApodState.SuccessVideo -> {
                binding.imageView.visibility = View.GONE
                binding.youtubePlayerView.visibility = View.VISIBLE
                showNasaVideo(getVideoIDFromUrl(apodState.apodDomainDataModel.url ?: ""))
                showBottomSheet(
                    apodState.apodDomainDataModel.title,
                    apodState.apodDomainDataModel.explanation
                )
            }
        }
    }

    private fun loadPicture(url: String?) {
        binding.imageView.load(url) {
            lifecycle(this@ApodFragment)
            placeholder(R.drawable.ic_apod_image_loading)
            error(R.drawable.ic_broken_image)
        }
    }

    private fun showBottomSheet(title: String?, explanation: String?) {
        if (title != null)
            if (explanation != null) {
                binding.bottomSheet.bottomSheetContainer.visibility = View.VISIBLE
                binding.bottomSheet.bottomSheetDescriptionHeader.text = title
                binding.bottomSheet.bottomSheetExplanation.text = explanation
            }
    }

    private fun getVideoIDFromUrl(url: String): String {
        var id = url
        while (id.indexOf('/') > -1) {
            id = id.subSequence(id.indexOf('/') + 1, id.length - 1) as String
        }

        if (id.indexOf('?') > -1)
            id = id.subSequence(0, id.indexOf('?')) as String

        return id
    }

    private fun showNasaVideo(videoId: String) {
        lifecycle.addObserver(binding.youtubePlayerView)
        binding.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {

                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
    }

    //===========================================================================================
    // BOTTOM SHEET

    private lateinit var bottomSheetBehaviour: BottomSheetBehavior<LinearLayout>

    private fun setBottomSheetBehavior(bottomSheet: LinearLayout) {


        bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehaviour.setPeekHeight(140);
    }
}