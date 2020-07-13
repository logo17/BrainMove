package com.loguito.brainmove.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.loguito.brainmove.R
import kotlinx.android.synthetic.main.main_button_widget.view.*

class MainButtonWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.main_button_widget, this, true)
        attrs?.let {
            val typedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.MainButtonWidget,
                defStyleAttr,
                defStyleRes
            )

            mainButton.text = typedArray.getString(R.styleable.MainButtonWidget_android_text)
            mainButton.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                typedArray.getDimension(R.styleable.MainButtonWidget_android_textSize, 0f)
            )
            isClickable = true
            isEnabled = typedArray.getBoolean(R.styleable.MainButtonWidget_android_enabled, false)
            background = typedArray.getDrawable(R.styleable.MainButtonWidget_android_background)
            typedArray.recycle()
        }
    }
}