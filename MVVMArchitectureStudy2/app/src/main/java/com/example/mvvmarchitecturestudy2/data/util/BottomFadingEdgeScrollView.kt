package com.example.mvvmarchitecturestudy2.data.util

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

class BottomFadingEdgeScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {

    override fun getTopFadingEdgeStrength() = 0f
}