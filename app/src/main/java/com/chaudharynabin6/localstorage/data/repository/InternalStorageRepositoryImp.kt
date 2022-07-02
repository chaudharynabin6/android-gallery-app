package com.chaudharynabin6.localstorage.data.repository

import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.chaudharynabin6.localstorage.data.internal_storage.InternalStoragePhoto
import com.chaudharynabin6.localstorage.data.internal_storage.SharedStoragePhoto
import com.chaudharynabin6.localstorage.domain.repository.InternalStorageRepository
import com.chaudharynabin6.localstorage.util.isSdk29orUp
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

    override fun takePhoto(isPrivate: Boolean, bitmap: Bitmap, writePermission: Boolean) {
        val isPhotoSavedSuccessfully = when {
            isPrivate -> savePhotoToInternalStorage(
                filename = LocalDateTime.now().toString() + ".jpeg",
                bitmap = bitmap
            )

            writePermission -> savePhotoToExternalStorage(
                displayName = LocalDateTime.now().toString(),
                bitmap = bitmap
            )

            else -> false

        }

        if (isPhotoSavedSuccessfully) {
            Toast.makeText(context, "photo saved successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "photo not saved successfully", Toast.LENGTH_SHORT)
                .show()
        }


    }

    override fun savePhotoToExternalStorage(displayName: String, bitmap: Bitmap): Boolean {
//        media store uri
        val imageCollection = isSdk29orUp {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, bitmap.width)
            put(MediaStore.Images.Media.HEIGHT, bitmap.height)

        }

        return try {
//            for inserting meta data and this will return uri where we can insert actual photo
            context.contentResolver.insert(
                imageCollection, // uri
                contentValues // contentValues
            )?.also { uri ->
                context.contentResolver.openOutputStream(uri).use { outputStream ->
                    val isBitmapSaved = bitmap.compress(
                        Bitmap.CompressFormat.JPEG, 95, outputStream
                    )
                    if (!isBitmapSaved) {
                        throw IOException("cannot save bitmap")
                    }
                }
            } ?: throw  IOException("could not create MediaStore entry")
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun loadPhotosFromExternalStorage(): List<SharedStoragePhoto> {

        return withContext(
            Dispatchers.IO
        ) {
            val externalImageCollection = isSdk29orUp {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT
            )
            val photos = mutableListOf<SharedStoragePhoto>()
            context.contentResolver.query(
                externalImageCollection,
                projection,
                """ 
                    ${MediaStore.Images.Media.DISPLAY_NAME} LIKE '%.jpg'
                  
                 """.trimIndent(),
                null,
                "${MediaStore.Images.Media.DATE_MODIFIED} DESC"
            )?.use { cursor ->

                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
                val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)


                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val width = cursor.getInt(widthColumn)
                    val height = cursor.getInt(heightColumn)

                    val contentUri = ContentUris.withAppendedId(
                        externalImageCollection,
                        id
                    )

//                    val source = ImageDecoder.createSource(context.contentResolver, contentUri)
//
//
//                    val bitmap = ImageDecoder.decodeBitmap(source)



                    photos.add(
                        SharedStoragePhoto(
                            id = id,
                            name = displayName,
                            width = width,
                            height = height,
                            contentUri = contentUri,
//                            bitmap = bitmap
                        )
                    )


                }
                photos.toList()
            } ?: emptyList()

        }
    }

    override suspend fun deletePhotoFromExternalStorage(photoUri: Uri): IntentSender? {

        return withContext(Dispatchers.IO) {
            var intentSender: IntentSender? = null
            try {
//                for 28 and less
                context.contentResolver.delete(photoUri, null, null)

            } catch (e: SecurityException) {
                intentSender = when {
//                  30 and greater
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                        MediaStore.createDeleteRequest(context.contentResolver,
                            listOf(photoUri)).intentSender
                    }
//                  29 and greater
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                        val recoverableSecurityException = e as? RecoverableSecurityException
                        val intent =
                            recoverableSecurityException?.userAction?.actionIntent?.intentSender
                        intent
                    }
                    else -> null
                }
            }
            intentSender
        }

    }
}