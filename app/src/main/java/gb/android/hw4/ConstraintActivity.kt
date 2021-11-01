package gb.android.hw4

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import gb.android.nasapi.databinding.ActivityConstraintBinding

class ConstraintActivity : AppCompatActivity() {

    private var _binding: ActivityConstraintBinding? = null
    private val binding: ActivityConstraintBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityConstraintBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chipToggleGroupVisibility.setOnCheckedChangeListener { compoundButton, isToggled ->
            if (isToggled)
                binding.group.visibility = View.VISIBLE
            else
                binding.group.visibility = View.GONE
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}