package gb.android.hw4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import gb.android.nasapi.R
import gb.android.nasapi.databinding.ActivityCollapsingToolBarBinding

class CollapsingToolBarActivity : AppCompatActivity() {

    private var _binding: ActivityCollapsingToolBarBinding? = null
    private val binding: ActivityCollapsingToolBarBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityCollapsingToolBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.collapsingToolbar.title = title

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}