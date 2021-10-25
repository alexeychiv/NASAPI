package gb.android.nasapi.presentation.themes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import gb.android.nasapi.App
import gb.android.nasapi.R
import gb.android.nasapi.databinding.FragmentThemesBinding

class ThemesFragment : Fragment() {

    //===========================================================================================
    // BINDING

    private var _binding: FragmentThemesBinding? = null
    private val binding: FragmentThemesBinding
        get() = _binding!!

    //===========================================================================================
    // COMPANION

    companion object {
        fun newInstance() = ThemesFragment()
    }

    //===========================================================================================
    // LIFECYCLE EVENTS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThemesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (App.settings.getTheme()) {
            1 -> binding.chipThemePurple.isChecked = true
            2 -> binding.chipThemeRed.isChecked = true
        }


        binding.chipGroupThemes.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_theme_purple -> {
                    App.settings.setTheme(1)
                    requireActivity().recreate()
                }
                R.id.chip_theme_red -> {
                    App.settings.setTheme(2)
                    requireActivity().recreate()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}