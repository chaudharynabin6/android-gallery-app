package com.chaudharynabin6.localstorage.domain.model

import android.graphics.Bitmap
import android.net.Uri

data class ExternalImageData(
    val id : Long,
    val name : String,
    val width : Int,
    val height : Int,
    val contentUri :Uri,
    val bitmap: Bitmap?
)
