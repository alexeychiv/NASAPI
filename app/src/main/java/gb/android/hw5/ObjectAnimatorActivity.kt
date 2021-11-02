package gb.android.hw5

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import gb.android.nasapi.databinding.ActivityObjectAnimatorBinding


class ObjectAnimatorActivity : AppCompatActivity() {

    private var _binding: ActivityObjectAnimatorBinding? = null
    private val binding: ActivityObjectAnimatorBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityObjectAnimatorBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.chipObjectAnimatorFade.setOnCheckedChangeListener { compoundButton, isChecked ->
            fade(isChecked, binding.viewTransitionFade)
        }

        binding.btnObjectAnimatorMove.setOnClickListener {
            moveRightLeft(binding.viewTransitionMove)
        }

        binding.btnObjectAnimatorRotate.setOnClickListener {
            rotateRightLeft(binding.viewTransitionRotate)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun fade(isFadeIn: Boolean, view: View) {
        if (isFadeIn)
            ObjectAnimator
                .ofFloat(view, View.ALPHA, 0f, 1f)
                .setDuration(1000)
                .start()
        else
            ObjectAnimator
                .ofFloat(view, View.ALPHA, 1f, 0f)
                .setDuration(1000)
                .start()
    }

    private fun moveRightLeft(view: View) {
        val animatorSet = AnimatorSet()

        val moveRightAnimation = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0f, 700f)
        moveRightAnimation.interpolator = DecelerateInterpolator()

        val moveLeftAnimation = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 700f, 0f)
        moveLeftAnimation.interpolator = AccelerateInterpolator()

        animatorSet.playSequentially(moveRightAnimation, moveLeftAnimation)
        animatorSet.setDuration(1000)
        animatorSet.start()
    }

    private fun rotateRightLeft(view: View) {
        val animatorSet = AnimatorSet()

        val rotateXinc = ObjectAnimator.ofFloat(view, View.ROTATION_X, 0f, 720f)
        rotateXinc.interpolator = DecelerateInterpolator()
        val rotateXdec = ObjectAnimator.ofFloat(view, View.ROTATION_X, 720f, 0f)
        rotateXinc.interpolator = AccelerateInterpolator()

        val rotateYinc = ObjectAnimator.ofFloat(view, View.ROTATION_Y, 0f, 720f)
        rotateXinc.interpolator = DecelerateInterpolator()
        val rotateYdec = ObjectAnimator.ofFloat(view, View.ROTATION_Y, 720f, 0f)
        rotateXinc.interpolator = AccelerateInterpolator()

        val rotateInc = ObjectAnimator.ofFloat(view, View.ROTATION, 0f, 720f)
        rotateXinc.interpolator = DecelerateInterpolator()
        val rotateDec = ObjectAnimator.ofFloat(view, View.ROTATION, 720f, 0f)
        rotateXinc.interpolator = AccelerateInterpolator()

        animatorSet.playSequentially(
            rotateXinc,
            rotateXdec,
            rotateYinc,
            rotateYdec,
            rotateInc,
            rotateDec
        )
        animatorSet.setDuration(1000)
        animatorSet.start()
    }

}

