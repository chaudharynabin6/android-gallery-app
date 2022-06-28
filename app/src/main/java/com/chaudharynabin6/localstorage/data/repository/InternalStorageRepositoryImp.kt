package com.chaudharynabin6.localstorage.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.chaudharynabin6.localstorage.data.internal_storage.InternalStoragePhoto
import com.chaudharynabin6.localstorage.domain.repository.InternalStorageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.time.LocalDateTime
import javax.inject.Inject

//https://www.codexpedia.com/android/android-dagger-hilt-injecting-application-context-in-a-povides-function/
//https://developer.android.com/jetpack/compose/libraries
class InternalStorageRepositoryImp @Inject constructor(
    @ApplicationContext private val context: Context,
) : InternalStorageRepository, ComponentActivity() {
    private val componentActivity: ComponentActivity = ComponentActivity()
    override fun savePhotoToInternalStorage(filename: String, bitmap: Bitmap): Boolean {

        return try {
//            open output stream
            context.openFileOutput("$filename.jpeg", Context.MODE_PRIVATE).use {
                val isPhotoSavedSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 95, it)
                if (!isPhotoSavedSuccess) {
                    throw IOException("cannot save the bitmap")
                }
            }

            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun loadPhotosFromInternalStorage(): List<InternalStoragePhoto> {

        return withContext(Dispatchers.IO) {
            val files = context.filesDir.listFiles()
            files?.filter { it.canRead() && it.name.endsWith(".jpeg") && it.isFile }?.map {
                val byteSteam = it.readBytes()
                val bitmap = BitmapFactory.decodeByteArray(byteSteam, 0, byteSteam.size)
                InternalStoragePhoto(
                    fileName = it.name,
                    bitmap = bitmap,
                )
            } ?: emptyList()

        }
    }

    override fun deletePhotoFromInternalStorage(filename: String): Boolean {

        return try {
            context.deleteFile(filename)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    override fun takePhoto(isPrivate: Boolean, bitmap: Bitmap) {

        if (isPrivate) {
            val isPhotoSavedSuccessFully = savePhotoToInternalStorage(
                filename = LocalDateTime.now().toString() + ".jpeg",
                bitmap = bitmap
            )
            if (isPhotoSavedSuccessFully) {
                Toast.makeText(context, "photo saved successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "photo not saved successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(context, "photo not saved successfully", Toast.LENGTH_SHORT).show()
        }

    }
}