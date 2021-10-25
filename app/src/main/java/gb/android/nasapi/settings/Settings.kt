package gb.android.nasapi.settings

import android.content.Context
import gb.android.nasapi.R

class Settings(
    context: Context
) {

    private val sharedPreferences =
        context.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)

    fun setTheme(themeId: Int) {
        sharedPreferences.edit().putInt("THEME_ID", themeId).commit()
    }

    fun getTheme(): Int = sharedPreferences.getInt("THEME_ID", 1)

    fun getThemeId(): Int {
        return when (getTheme()) {
            1 -> R.style.Theme_NASAPI_Purple
            2 -> R.style.Theme_NASAPI_Red
            else -> R.style.Theme_NASAPI_Purple
        }
    }


}