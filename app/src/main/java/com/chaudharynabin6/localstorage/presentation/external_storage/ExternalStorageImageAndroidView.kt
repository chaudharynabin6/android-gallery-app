package com.chaudharynabin6.localstorage.presentation.external_storage


import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.chaudharynabin6.localstorage.domain.model.ExternalImageData
import com.chaudharynabin6.localstorage.presentation.internalstorage_part1.InternalStoragePart1Events
import com.chaudharynabin6.localstorage.presentation.internalstorage_part1.InternalStoragePart1ViewModel
import kotlinx.coroutines.launch

@Composable
fun ExternalStorageImageAndroidView(
    imageData: List<ExternalImageData>,
    modifier: Modifier,
    viewModel: InternalStoragePart1ViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val intentSenderLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {

                viewModel.onEvent(InternalStoragePart1Events.PhotoToDeleteAfterPermissionSuccessful)


            } else {
                viewModel.onEvent(InternalStoragePart1Events.PhotoToDeleteAfterPermissionUnsuccessful)
            }
        }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        modifier = modifier

    ) {

        itemsIndexed(
            items = imageData,
            key = { index: Int, item: ExternalImageData ->
                item.id
            }
        ) { index: Int, item: ExternalImageData ->






                AsyncImage(
                    model = item.contentUri,
                    contentDescription = null,
                    modifier = Modifier.pointerInput(key1 = item.id) {
                        detectTapGestures(
                            onDoubleTap = {
                                viewModel.onEvent(InternalStoragePart1Events.DeleteExternalStoragePhoto(
                                    item.contentUri))
                                val intentSender =
                                    viewModel.deletePhotoFromExternalStorage(item.contentUri)
                                scope.launch {
                                    intentSender.collect {
                                        it?.also {
                                            intentSenderLauncher.launch(
                                                IntentSenderRequest.Builder(it).build()
                                            )
                                        }
                                    }
                                }

                            }
                        )
                    },
                )


        }
    }
}