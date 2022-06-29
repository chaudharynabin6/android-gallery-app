package com.chaudharynabin6.localstorage.util

import android.os.Build

inline fun <T> isSdk29orUp(
    block: () -> T,
): T? {
// fill build version is greater or equal to 29 then will execute passed lambda and return it's result
//    else we return null
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        block()
    } else null
}