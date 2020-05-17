package com.loguito.brainmove.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.loguito.brainmove.R
import kotlinx.android.synthetic.main.overlay_image_widget.view.*

class OverlayImageWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var gifUrl: String = String()
        set(value) {
            field = value
            Glide.with(this).load(value).into(workoutImageView)
            this.visibility = View.VISIBLE
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.overlay_image_widget, this, true)
        setBackgroundColor(ContextCompat.getColor(context, R.color.widget_overlay_black))
        this.setOnClickListener {
            this.visibility = View.GONE
        }
    }
}