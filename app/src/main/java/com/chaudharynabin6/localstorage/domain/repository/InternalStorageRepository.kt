package com.chaudharynabin6.localstorage.domain.repository

import android.graphics.Bitmap
import androidx.activity.result.ActivityResultLauncher
import com.chaudharynabin6.localstorage.data.internal_storage.InternalStoragePhoto

interface InternalStorageRepository {

    fun savePhotoToInternalStorage(filename : String, bitmap: Bitmap) : Boolean

    suspend fun loadPhotosFromInternalStorage() : List<InternalStoragePhoto>

    fun deletePhotoFromInternalStorage(filename: String) : Boolean

    fun takePhoto(isPrivate : Boolean,bitmap: Bitmap, writePermission: Boolean)

    fun savePhotoToExternalStorage(displayName : String,bitmap: Bitmap) : Boolean

}