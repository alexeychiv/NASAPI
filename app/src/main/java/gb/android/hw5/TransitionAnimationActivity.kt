package gb.android.hw5

import android.graphics.Rect
import android.os.Bundle
import android.transition.*
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.forEach
import gb.android.nasapi.databinding.ActivityTransitionAnimationBinding


class TransitionAnimationActivity : AppCompatActivity() {

    private var _binding: ActivityTransitionAnimationBinding? = null
    private val binding: ActivityTransitionAnimationBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityTransitionAnimationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chipObjectAnimatorFade.setOnCheckedChangeListener { compoundButton, isChecked ->
            transitionFade(isChecked, binding.linearLayoutTransitionFade)
        }

        binding.chipTransitionSlide.setOnCheckedChangeListener { compoundButton, isChecked ->
            transitionSlide(isChecked, binding.linearLayoutTransitionSlide)
        }



        binding.btnReturnExploded.setOnClickListener {
            transitionExplode(binding.gridLayoutTransitionExplode)
            binding.gridLayoutTransitionExplode.visibility = View.VISIBLE
            binding.gridLayoutTransitionExplode.forEach {
                it.visibility = View.VISIBLE
            }
        }

        binding.gridLayoutTransitionExplode.forEach {
            it.setOnClickListener {
                transitionExplode(it)
            }
        }


        binding.chipTransitionSize.setOnCheckedChangeListener { compoundButton, isChecked ->
            transitionSize(isChecked, binding.frameLayoutTransitionSize)
        }

        binding.chipTransitionMove.setOnCheckedChangeListener { compoundButton, isChecked ->
            transitionMoveOnPath(isChecked, binding.frameLayoutTransitionMove)
        }


        val titles: MutableList<String> = ArrayList()
        for (i in 0..4) {
            titles.add(String.format("Item %d Very Long Text", i + 1))
        }
        createViews(binding.LinearLayoutTransitionShuffle, titles)

        binding.btnTransitionShuffle.setOnClickListener {
            TransitionManager.beginDelayedTransition(
                binding.LinearLayoutTransitionShuffle,
                ChangeBounds()
            )
            titles.shuffle()
            createViews(binding.LinearLayoutTransitionShuffle, titles)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun transitionFade(doFadeIn: Boolean, viewGroup: ViewGroup) {
        if (doFadeIn) {
            TransitionManager.beginDelayedTransition(viewGroup)
            viewGroup.visibility = View.VISIBLE
        } else {
            TransitionManager.beginDelayedTransition(viewGroup)
            viewGroup.visibility = View.GONE
        }
    }

    private fun transitionSlide(doSlideIn: Boolean, viewGroup: ViewGroup) {
        if (doSlideIn) {
            TransitionManager.beginDelayedTransition(viewGroup, Slide(Gravity.END))
            viewGroup.visibility = View.VISIBLE
        } else {
            TransitionManager.beginDelayedTransition(viewGroup, Slide(Gravity.END))
            viewGroup.visibility = View.GONE
        }
    }

    private fun transitionExplode(clickedView: View) {
        val viewRect = Rect()
        clickedView.getGlobalVisibleRect(viewRect)
        val explode = Explode()
        explode.epicenterCallback = object : Transition.EpicenterCallback() {
            override fun onGetEpicenter(transition: Transition): Rect {
                return viewRect
            }
        }
        explode.duration = 1000
        TransitionManager.beginDelayedTransition(binding.gridLayoutTransitionExplode, explode)
        clickedView.visibility = View.GONE
        //binding.gridLayoutTransitionExplode.visibility = View.GONE
    }

    private fun transitionSize(doEnlarge: Boolean, viewGroup: ViewGroup) {
        TransitionManager.beginDelayedTransition(
            viewGroup, TransitionSet()
                .addTransition(ChangeBounds())
                .addTransition(ChangeImageTransform())
        )

        val params: ViewGroup.LayoutParams = viewGroup.layoutParams

        if (doEnlarge) {
            params.height = 0
            params.width = 0
        } else {
            params.height = 20
            params.width = 20
        }

        viewGroup.layoutParams = params
    }

    private fun transitionMoveOnPath(isToRight: Boolean, viewGroup: ViewGroup) {
        val changeBounds = ChangeBounds()
        changeBounds.setPathMotion(ArcMotion())
        changeBounds.duration = 500
        TransitionManager.beginDelayedTransition(viewGroup, changeBounds)

        val params = viewGroup.getChildAt(0).layoutParams as FrameLayout.LayoutParams

        params.gravity =
            if (isToRight)
                Gravity.START or Gravity.TOP
            else
                Gravity.BOTTOM or Gravity.END

        viewGroup.getChildAt(0).layoutParams = params
    }


    private fun createViews(layout: ViewGroup, titles: List<String>) {
        layout.removeAllViews()
        for (title in titles) {
            val textView = TextView(this)
            textView.text = title
            textView.gravity = Gravity.CENTER_HORIZONTAL
            ViewCompat.setTransitionName(textView, title)
            layout.addView(textView)
        }
    }
}

