package com.chaudharynabin6.localstorage.data.mapper

import com.chaudharynabin6.localstorage.data.internal_storage.InternalStoragePhoto
import com.chaudharynabin6.localstorage.domain.model.ImageData

fun InternalStoragePhoto.toImageData() : ImageData{
    return  ImageData(
        bitmap = bitmap,
        fileName = fileName
    )
}