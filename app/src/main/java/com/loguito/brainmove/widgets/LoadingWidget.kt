package com.loguito.brainmove.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.loguito.brainmove.R
import kotlinx.android.synthetic.main.widget_loading.view.*

class LoadingWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.widget_loading, this, true)
        setBackgroundColor(ContextCompat.getColor(context, R.color.widget_overlay_black))
        attrs?.let {
            val typedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.LoadingWidget,
                defStyleAttr,
                defStyleRes
            )

            loadingMessageTextView.text =
                typedArray.getString(R.styleable.LoadingWidget_loadingMessage)

            typedArray.recycle()
        }
    }

    fun setLoadingMessage(message: String) {
        loadingMessageTextView.text = message
    }
}