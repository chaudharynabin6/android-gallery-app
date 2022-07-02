package com.chaudharynabin6.localstorage

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.chaudharynabin6.localstorage.presentation.external_storage.ExternalStorageImageGridView
import com.chaudharynabin6.localstorage.presentation.external_storage.ExternalStorageRowScrollView
import com.chaudharynabin6.localstorage.presentation.internalstorage_part1.InternalStoragePart1Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InternalStoragePart1Screen()
        }
    }
//
//    override fun takePhoto(isPrivate: Boolean): ActivityResultLauncher<Void?> {
//        return registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
//
//            if (isPrivate) {
//                val isPhotoSavedSuccessFully = internalStorageRepository.savePhotoToInternalStorage(
//                    filename = LocalDateTime.now().toString() + ".jpeg",
//                    bitmap = it!!
//                )
//                if (isPhotoSavedSuccessFully) {
//                    Toast.makeText(this, "photo saved successfully", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "photo not saved successfully", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            } else {
//                Toast.makeText(this, "photo not saved successfully", Toast.LENGTH_SHORT).show()
//            }
//
//        }
//    }
}