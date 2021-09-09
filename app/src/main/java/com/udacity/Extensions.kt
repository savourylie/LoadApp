package com.udacity

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect


fun Context.dpToPx(dp: Float) = this.getResources().getDisplayMetrics().density * dp


fun Paint.getTextWidth(string: String): Float {
    val rect = Rect()
    this.getTextBounds(string, 0, string.length, rect)
    return rect.width().toFloat()
}