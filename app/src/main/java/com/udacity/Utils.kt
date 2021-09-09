package com.udacity

fun makePositiveAngle(angleInDeg: Float): Float {
    var newAngleInDeg = angleInDeg

    while (newAngleInDeg < 0) {
        newAngleInDeg += 360
    }

    return newAngleInDeg
}