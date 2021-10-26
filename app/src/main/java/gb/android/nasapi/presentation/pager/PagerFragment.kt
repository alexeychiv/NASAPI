package gb.android.nasapi.presentation.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import gb.android.nasapi.R
import gb.android.nasapi.databinding.FragmentPagerBinding

class PagerFragment : Fragment() {

    private val EARTH = 0
    private val MARS = 1
    private val WEATHER = 2

    //===========================================================================================
    // BINDING

    private var _binding: FragmentPagerBinding? = null
    private val binding: FragmentPagerBinding
        get() = _binding!!

    //===========================================================================================
    // COMPANION

    companion object {
        fun newInstance() = PagerFragment()
    }

    //===========================================================================================
    // LIFECYCLE EVENTS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPager()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    //===========================================================================================
    // PAGER SETUP

    private fun setupPager() {

        binding.viewPager.adapter = ViewPagerAdapter(parentFragmentManager)

        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.tabLayout.setSelectedTabIndicatorColor(R.attr.colorAccent)

        setHighlightedTab(EARTH)

        setPagerChangeListener()
    }

    private fun setPagerChangeListener() {
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                setHighlightedTab(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
        })
    }

    // ===========================================================================================
    // TABS MANAGEMENT

    private fun setHighlightedTab(position: Int) {
        val layoutInflater = LayoutInflater.from(activity)

        binding.tabLayout.getTabAt(EARTH)?.customView = null
        binding.tabLayout.getTabAt(MARS)?.customView = null
        binding.tabLayout.getTabAt(WEATHER)?.customView = null

        when (position) {
            EARTH -> {
                setEarthTabHighlighted(layoutInflater)
            }
            MARS -> {
                setMarsTabHighlighted(layoutInflater)
            }
            WEATHER -> {
                setWeatherTabHighlighted(layoutInflater)
            }
            else -> {
                setEarthTabHighlighted(layoutInflater)
            }
        }
    }

    private fun setEarthTabHighlighted(layoutInflater: LayoutInflater) {

        val earth = layoutInflater.inflate(R.layout.activity_api_custom_tab_earth, null)

        earth.findViewById<AppCompatTextView>(R.id.tab_image_textview)
            .setTextColor(R.attr.colorAccent)

        binding.tabLayout.getTabAt(EARTH)?.customView = earth
        binding.tabLayout.getTabAt(MARS)?.customView =
            layoutInflater.inflate(R.layout.activity_api_custom_tab_mars, null)
        binding.tabLayout.getTabAt(WEATHER)?.customView =
            layoutInflater.inflate(R.layout.activity_api_custom_tab_weather, null)

        binding.ivSwipeIndicatorEarth.setImageResource(R.drawable.swipe_indicator_active)
        binding.ivSwipeIndicatorMars.setImageResource(R.drawable.swipe_indicator_passive)
        binding.ivSwipeIndicatorWeather.setImageResource(R.drawable.swipe_indicator_passive)
    }

    private fun setMarsTabHighlighted(layoutInflater: LayoutInflater) {

        val mars = layoutInflater.inflate(R.layout.activity_api_custom_tab_mars, null)

        mars.findViewById<AppCompatTextView>(R.id.tab_image_textview)
            .setTextColor(R.attr.colorAccent)

        binding.tabLayout.getTabAt(EARTH)?.customView =
            layoutInflater.inflate(R.layout.activity_api_custom_tab_earth, null)
        binding.tabLayout.getTabAt(MARS)?.customView = mars
        binding.tabLayout.getTabAt(WEATHER)?.customView =
            layoutInflater.inflate(R.layout.activity_api_custom_tab_weather, null)

        binding.ivSwipeIndicatorEarth.setImageResource(R.drawable.swipe_indicator_passive)
        binding.ivSwipeIndicatorMars.setImageResource(R.drawable.swipe_indicator_active)
        binding.ivSwipeIndicatorWeather.setImageResource(R.drawable.swipe_indicator_passive)
    }

    private fun setWeatherTabHighlighted(layoutInflater: LayoutInflater) {

        val weather = layoutInflater.inflate(R.layout.activity_api_custom_tab_weather, null)

        weather.findViewById<AppCompatTextView>(R.id.tab_image_textview)
            .setTextColor(R.attr.colorAccent)

        binding.tabLayout.getTabAt(EARTH)?.customView =
            layoutInflater.inflate(R.layout.activity_api_custom_tab_earth, null)
        binding.tabLayout.getTabAt(MARS)?.customView =
            layoutInflater.inflate(R.layout.activity_api_custom_tab_mars, null)
        binding.tabLayout.getTabAt(WEATHER)?.customView = weather

        binding.ivSwipeIndicatorEarth.setImageResource(R.drawable.swipe_indicator_passive)
        binding.ivSwipeIndicatorMars.setImageResource(R.drawable.swipe_indicator_passive)
        binding.ivSwipeIndicatorWeather.setImageResource(R.drawable.swipe_indicator_active)
    }
}