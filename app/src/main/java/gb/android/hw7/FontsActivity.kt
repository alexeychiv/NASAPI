package gb.android.hw7

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import gb.android.nasapi.R
import gb.android.nasapi.databinding.ActivityFontsBinding

class FontsActivity : AppCompatActivity() {

    private var _binding: ActivityFontsBinding? = null
    private val binding: ActivityFontsBinding
        get() = _binding!!

    val northeadTypeface by lazy { Typeface.createFromAsset(this.assets, "Northead-0WgdX.ttf") }
    val smytheTypeface by lazy { ResourcesCompat.getFont(this, R.font.smythe) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityFontsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //FONTS
        binding.tvLongText.typeface = Typeface.DEFAULT

        binding.chipgroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_default -> {
                    binding.tvLongText.typeface = Typeface.DEFAULT
                }
                R.id.chip_northead -> {
                    binding.tvLongText.typeface = northeadTypeface
                }
                R.id.chip_smythe -> {
                    binding.tvLongText.typeface = smytheTypeface
                }
            }
        }

        //SPANS - HTML

        binding.tvHtmlTags.text = Html.fromHtml("Plain - <i>Italic</i> - <b>Bold</b>")

        //SPANS - SPANS

        val spannable_style = SpannableString("Plain - Italic - Bold")

        spannable_style.setSpan(
            StyleSpan(Typeface.ITALIC),
            8, 14,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        spannable_style.setSpan(
            StyleSpan(Typeface.BOLD),
            17, 21,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )

        binding.tvSpansTextStyle.text = spannable_style

        val spannable_color = SpannableString("Red - Green - Blue")


        spannable_color.setSpan(
            ForegroundColorSpan(Color.RED),
            0, 3,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        spannable_color.setSpan(
            ForegroundColorSpan(Color.GREEN),
            6, 11,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        spannable_color.setSpan(
            ForegroundColorSpan(Color.BLUE),
            14, 18,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )

        binding.tvSpansTextColor.text = spannable_color
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}