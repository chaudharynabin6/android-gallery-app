package com.chaudharynabin6.localstorage.domain.repository

import android.content.IntentSender
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import com.chaudharynabin6.localstorage.data.internal_storage.InternalStoragePhoto
import com.chaudharynabin6.localstorage.data.internal_storage.SharedStoragePhoto

interface InternalStorageRepository {

    fun savePhotoToInternalStorage(filename : String, bitmap: Bitmap) : Boolean

    suspend fun loadPhotosFromInternalStorage() : List<InternalStoragePhoto>

    fun deletePhotoFromInternalStorage(filename: String) : Boolean

    fun takePhoto(isPrivate : Boolean,bitmap: Bitmap, writePermission: Boolean)

    fun savePhotoToExternalStorage(displayName : String,bitmap: Bitmap) : Boolean

    suspend fun loadPhotosFromExternalStorage() : List<SharedStoragePhoto>

    suspend fun deletePhotoFromExternalStorage(photoUri: Uri): IntentSender?

}