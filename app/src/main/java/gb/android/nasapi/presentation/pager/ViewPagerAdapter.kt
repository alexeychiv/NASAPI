package gb.android.nasapi.presentation.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import gb.android.nasapi.presentation.pager.earth.EarthFragment
import gb.android.nasapi.presentation.pager.mars.MarsFragment
import gb.android.nasapi.presentation.pager.weather.WeatherFragment


class ViewPagerAdapter(private val fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    private val EARTH_FRAGMENT = 0
    private val MARS_FRAGMENT = 1
    private val WEATHER_FRAGMENT = 2

    private val fragments = arrayOf(EarthFragment(), MarsFragment(), WeatherFragment())

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> fragments[EARTH_FRAGMENT]
            1 -> fragments[MARS_FRAGMENT]
            2 -> fragments[WEATHER_FRAGMENT]
            else -> fragments[EARTH_FRAGMENT]
        }
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }
}