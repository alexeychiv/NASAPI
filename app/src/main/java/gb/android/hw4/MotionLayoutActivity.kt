package gb.android.hw4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import gb.android.nasapi.databinding.ActivityMotionLayoutBinding

class MotionLayoutActivity : AppCompatActivity() {

    private var _binding: ActivityMotionLayoutBinding? = null
    private val binding: ActivityMotionLayoutBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMotionLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}