package gb.android.nasapi.presentation.apod

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.*
import android.view.animation.DecelerateInterpolator
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
import gb.android.nasapi.presentation.utils.getColorFromAttr

class ApodFragment : Fragment() {

    //===========================================================================================
    // BINDING

    private var _binding: FragmentApodBinding? = null
    private val binding: FragmentApodBinding
        get() = _binding!!

    //===========================================================================================
    // VIEW MODEL

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, ApodViewModelFactory(requireActivity().applicationContext))
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
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.layoutSearchWiki.etSearchWiki.text}")
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
                animationFadeIn(binding.imageView)
                animationFadeOut(binding.youtubePlayerView)
                animationFadeOut(binding.bottomSheet.bottomSheetContainer)

                Snackbar
                    .make(
                        binding.main,
                        "${apodState.error.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
            }
            ApodState.Loading -> {
                animationFadeOut(binding.imageView)
                animationFadeOut(binding.youtubePlayerView)
                animationFadeOut(binding.bottomSheet.bottomSheetContainer)

                binding.bottomSheet.bottomSheetContainer.visibility = View.GONE
                /*Snackbar.make(binding.main,"LOADING....",Snackbar.LENGTH_SHORT).show()*/
            }
            is ApodState.SuccessImage -> {
                animationFadeIn(binding.imageView)
                animationFadeOut(binding.youtubePlayerView)

                animationFadeIn(binding.bottomSheet.bottomSheetContainer)
                showBottomSheet(
                    apodState.apodDomainDataModel.title,
                    apodState.apodDomainDataModel.explanation
                )

                if (!apodState.apodDomainDataModel.url.isNullOrBlank()) {
                    if (binding.chipToggleHd.isChecked)
                        loadPicture(apodState.apodDomainDataModel.hdurl)
                    else
                        loadPicture(apodState.apodDomainDataModel.url)
                }
            }
            is ApodState.SuccessVideo -> {
                animationFadeOut(binding.imageView)
                animationFadeIn(binding.youtubePlayerView)

                showNasaVideo(getVideoIDFromUrl(apodState.apodDomainDataModel.url ?: ""))

                animationFadeIn(binding.bottomSheet.bottomSheetContainer)
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

                val spannableTitle = SpannableString(title)

                spannableTitle.setSpan(
                    ForegroundColorSpan(requireContext().getColorFromAttr(R.attr.colorPrimary)),
                    0, title.length,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
                spannableTitle.setSpan(
                    StyleSpan(Typeface.BOLD),
                    0, title.length,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )

                binding.bottomSheet.bottomSheetDescriptionHeader.text = spannableTitle

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
    // ANIMATIONS

    private fun animationFadeIn(view: View) {
        val animator = ObjectAnimator.ofFloat(view, View.ALPHA, view.alpha, 1f)
        animator.apply {
            duration = 800
            interpolator = DecelerateInterpolator()
            start()
        }
    }

    private fun animationFadeOut(view: View) {
        val animator = ObjectAnimator.ofFloat(view, View.ALPHA, view.alpha, 0f)
        animator.apply {
            duration = 800
            interpolator = DecelerateInterpolator()
            start()
        }
    }


    //===========================================================================================
    // BOTTOM SHEET


    private lateinit var bottomSheetBehaviour: BottomSheetBehavior<LinearLayout>

    private fun setBottomSheetBehavior(bottomSheet: LinearLayout) {

        bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehaviour.peekHeight = 140
    }
}