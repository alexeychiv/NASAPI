package gb.android.nasapi.presentation

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import gb.android.hw4.CollapsingToolBarActivity
import gb.android.hw4.ConstraintActivity
import gb.android.hw4.MotionLayoutActivity
import gb.android.hw5.ObjectAnimatorActivity
import gb.android.hw5.TransitionAnimationActivity
import gb.android.hw6.recycler.RecyclerActivity
import gb.android.hw6.todolist.ToDoListActivity
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
            R.id.menu_item_constraint_activity -> {
                val intent = Intent(this, ConstraintActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_item_collapsing_toolbar_activity -> {
                val intent = Intent(this, CollapsingToolBarActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_item_motion_layout_activity -> {
                val intent = Intent(this, MotionLayoutActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_item_transition_animation_activity -> {
                val intent = Intent(this, TransitionAnimationActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_item_object_animator_activity -> {
                val intent = Intent(this, ObjectAnimatorActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_item_recycler_activity -> {
                val intent = Intent(this, RecyclerActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_item_to_do_list_activity -> {
                val intent = Intent(this, ToDoListActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}