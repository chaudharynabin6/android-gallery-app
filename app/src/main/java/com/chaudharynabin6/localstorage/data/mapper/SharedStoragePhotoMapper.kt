package com.chaudharynabin6.localstorage.data.mapper

import com.chaudharynabin6.localstorage.data.internal_storage.SharedStoragePhoto
import com.chaudharynabin6.localstorage.domain.model.ExternalImageData

 fun SharedStoragePhoto.toExternalImageData() : ExternalImageData {


    return  ExternalImageData(
        id = id,
        name = name,
        width = width,
        height = height,
        contentUri = contentUri,
        bitmap = null
    )
}