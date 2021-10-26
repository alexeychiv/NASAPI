package gb.android.nasapi.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import gb.android.nasapi.App
import gb.android.nasapi.R
import gb.android.nasapi.presentation.apod.ApodFragment
import gb.android.nasapi.presentation.pager.PagerFragment
import gb.android.nasapi.presentation.themes.ThemesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(App.settings.getThemeId())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.bottom_app_bar))

        if (savedInstanceState == null)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, ApodFragment.newInstance())
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bottom_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_back_to_apod_fragment -> {

                while (supportFragmentManager.backStackEntryCount > 0)
                    supportFragmentManager.popBackStackImmediate()
                true
            }
            R.id.menu_item_open_pager_fragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, PagerFragment.newInstance())
                    .addToBackStack("")
                    .commit()
                true
            }
            R.id.menu_item_open_themes_fragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ThemesFragment.newInstance())
                    .addToBackStack("")
                    .commit()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}