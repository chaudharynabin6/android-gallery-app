package com.chaudharynabin6.localstorage.presentation.internalstorage_part1

import android.content.IntentSender
import android.graphics.Bitmap
import android.net.Uri
import com.chaudharynabin6.localstorage.domain.model.ExternalImageData
import com.chaudharynabin6.localstorage.domain.model.ImageData
import com.chaudharynabin6.localstorage.domain.model.Permission

data class Part1State(
    val photos : List<ImageData> = emptyList(),
    val isPrivate : Boolean = true,
    val permissionToRequest: List<String> = emptyList(),
    val permission: Permission = Permission(
        readPermission = false,
        writePermission = false
    ),
    val externalImageDataList: List<ExternalImageData> = emptyList(),
    val bitmap: List<Bitmap> = emptyList(),
    val intentSender: IntentSender? = null,
    val photoToDeleteUri : Uri? = null
)
