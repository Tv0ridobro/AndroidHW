package itmo.android.animation

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import kotlin.math.min
import android.animation.Animator
import android.animation.AnimatorSet
import android.content.res.TypedArray
import android.view.animation.PathInterpolator
import androidx.core.animation.doOnEnd


class CustomAnimation @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val desiredWidth: Float
    private val desiredHeight: Float
    private val rectCornerRadius: Float
    private val scale: Float
    private val thick: Float
    private val delay: Long
    private val animDuration: Long
    private val distance: Float
    private val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectLength = dp(22f)
    private val marginLeft = dp(2f)
    private val marginTop = dp(16f)

    private val a: TypedArray = context.obtainStyledAttributes(
        attrs, R.styleable.CustomAnimation, defStyleAttr, 0
    )

    init {
        try {
            rectCornerRadius = a.getFloat(R.styleable.CustomAnimation_rectCornerRadius, dp(2.5f))
            scale = a.getFloat(R.styleable.CustomAnimation_scale, 1.3f)
            thick = a.getFloat(R.styleable.CustomAnimation_thick, dp(6f))
            delay = a.getInt(R.styleable.CustomAnimation_delay, 1000).toLong()
            animDuration = a.getInt(R.styleable.CustomAnimation_duration, 300).toLong()
            distance = a.getFloat(R.styleable.CustomAnimation_distance, dp(16f))
            rectPaint.color = a.getColor(R.styleable.CustomAnimation_color, 0xFFe1e3e6.toInt())
            desiredHeight = marginTop * 2 + rectLength
            desiredWidth = marginLeft * 2 + rectLength * 2 + distance
        } finally {
            a.recycle()
        }
    }


    private val firstRect = RectF(
        marginLeft,
        marginTop + (rectLength - thick) / 2,
        marginLeft + rectLength,
        marginTop + (rectLength - thick) / 2 + thick
    )

    private val secondRect = RectF(
        marginLeft + (rectLength - thick) / 2,
        marginTop,
        marginLeft + (rectLength - thick) / 2 + thick,
        marginTop + rectLength
    )

    private val dot = RectF(
        dp(0f),
        dp(0f),
        thick,
        thick
    )


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            getSize(widthMeasureSpec, desiredWidth.toInt()),
            getSize(heightMeasureSpec, desiredHeight.toInt())
        )
    }

    private var turn = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val save = canvas.save()

        if (turn == 0)
            canvas.rotate(rectRotation, marginLeft + rectLength / 2, marginTop + rectLength / 2)

        canvas.drawRoundRect(firstRect, rectCornerRadius, rectCornerRadius, rectPaint)
        canvas.drawRoundRect(secondRect, rectCornerRadius, rectCornerRadius, rectPaint)

        canvas.restoreToCount(save)

        canvas.save()

        canvas.translate(
            marginLeft + rectLength + distance,
            marginTop + (rectLength - thick) / 2
        )

        if (turn == 4)
            canvas.scale(dotScale, dotScale, thick / 2, thick / 2)
        if (turn == 5)
            canvas.scale(1 + scale - dotScale, 1 + scale - dotScale, thick / 2, thick / 2)
        canvas.drawRoundRect(dot, rectCornerRadius, rectCornerRadius, rectPaint)

        canvas.translate(
            -(marginLeft + rectLength + distance),
            -(marginTop + (rectLength - thick) / 2)
        )

        canvas.restore()

        canvas.save()
        canvas.translate(
            marginLeft + rectLength + distance + (rectLength - thick),
            marginTop + (rectLength - thick) / 2
        )

        if (turn == 2)
            canvas.scale(dotScale, dotScale, thick / 2, thick / 2)
        if (turn == 3)
            canvas.scale(1 + scale - dotScale, 1 + scale - dotScale, thick / 2, thick / 2)
        canvas.drawRoundRect(dot, rectCornerRadius, rectCornerRadius, rectPaint)

        canvas.translate(
            -(marginLeft + rectLength + distance + (rectLength - thick)),
            -(marginTop + (rectLength - thick) / 2)
        )

        canvas.restore()
        canvas.save()

        canvas.translate(
            marginLeft + rectLength + distance + thick + (rectLength - 3 * thick) / 2,
            marginTop
        )

        if (turn == 1)
            canvas.scale(dotScale, dotScale, thick / 2, thick / 2)
        if (turn == 2)
            canvas.scale(1 + scale - dotScale, 1 + scale - dotScale, thick / 2, thick / 2)

        canvas.drawRoundRect(dot, rectCornerRadius, rectCornerRadius, rectPaint)

        canvas.translate(
            -(marginLeft + rectLength + distance + thick + (rectLength - 3 * thick) / 2),
            -(marginTop)
        )

        canvas.restore()
        canvas.save()
        canvas.translate(
            marginLeft + rectLength + distance + thick + (rectLength - 3 * thick) / 2,
            marginTop + thick + (rectLength - 2 * thick)
        )

        if (turn == 3)
            canvas.scale(dotScale, dotScale, thick / 2, thick / 2)
        if (turn == 4)
            canvas.scale(1 + scale - dotScale, 1 + scale - dotScale, thick / 2, thick / 2)

        canvas.drawRoundRect(dot, rectCornerRadius, rectCornerRadius, rectPaint)

        canvas.translate(
            -(marginLeft + rectLength + distance + thick + (rectLength - 3 * thick) / 2),
            -(marginTop + thick + (rectLength - 2 * thick))
        )
        canvas.restore()

        canvas.restoreToCount(save)
    }

    private fun getSize(measureSpec: Int, desired: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        return when (mode) {
            MeasureSpec.AT_MOST -> min(size, desired)
            MeasureSpec.EXACTLY -> size
            MeasureSpec.UNSPECIFIED -> desired
            else -> desired
        }
    }

    private fun dp(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    private var rectRotation: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var dotScale: Float = 1f
        set(value) {
            field = value
            invalidate()
        }

    private val rectRotateAnimator = ValueAnimator.ofFloat(0.0F, 180F).apply {
        addUpdateListener {
            rectRotation = it.animatedValue as Float
        }
    }

    private val dotScaleAnimator = ValueAnimator.ofFloat(1f, scale).apply {
        addUpdateListener {
            dotScale = it.animatedValue as Float
        }
    }

    private var animator: Animator? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator?.cancel()
        animator = AnimatorSet().apply {
            interpolator = PathInterpolator(0.25F, 0.1F, 0.25F, 1F)
            playTogether(rectRotateAnimator, dotScaleAnimator)
            duration = animDuration
            doOnEnd {
                turn = (turn + 1) % 6
                if (turn == 0)
                    postDelayed({ this.start() }, delay)
                else
                    this.start()
            }
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
        animator = null
    }
}