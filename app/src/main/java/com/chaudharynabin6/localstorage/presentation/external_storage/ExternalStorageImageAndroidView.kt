package com.chaudharynabin6.localstorage.presentation.external_storage


import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.chaudharynabin6.localstorage.R
import com.chaudharynabin6.localstorage.domain.model.ExternalImageData
import com.chaudharynabin6.localstorage.presentation.internalstorage_part1.InternalStoragePart1Events
import com.chaudharynabin6.localstorage.presentation.internalstorage_part1.InternalStoragePart1ViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    val gridState = rememberLazyGridState()
    val gridIndex by remember { derivedStateOf { gridState.firstVisibleItemIndex } }
    var toShow by remember {
        mutableStateOf(false)
    }
var job : Job? = null
    DisposableEffect(key1 = gridIndex){
        scope.launch {
            job = scope.launch {
                delay(300)
                toShow = true
            }

        }

        onDispose {
            scope.launch {
                job?.cancel()
                toShow = false
            }
        }
    }
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Adaptive(minSize = 128.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        modifier = modifier

    ) {

        itemsIndexed(
            items = imageData,
            key = { _: Int, item: ExternalImageData ->
                item.id
            }
        ) { index: Int, item: ExternalImageData ->


            if(index >=  gridIndex- 2 && index <= gridIndex + 2 && toShow){
                ImageItem(uri = item.contentUri)
            }
            else{
                Image(painter = painterResource(id = R.drawable.image_1), contentDescription = null)
//                Box() {
//
//                }
            }
//            GlideImage(
//                imageModel = item.contentUri,
//                // Crop, Fit, Inside, FillHeight, FillWidth, None
//                contentScale = ContentScale.Crop,
//                // shows an image with a circular revealed animation.
//                circularRevealedEnabled = true,
//            )

//                AsyncImage(
//                    model = item.contentUri,
//                    contentDescription = null,
//                    modifier = Modifier.pointerInput(key1 = item.id) {
//                        detectTapGestures(
//                            onDoubleTap = {
//                                viewModel.onEvent(InternalStoragePart1Events.DeleteExternalStoragePhoto(
//                                    item.contentUri))
//                                val intentSender =
//                                    viewModel.deletePhotoFromExternalStorage(item.contentUri)
//                                scope.launch {
//                                    intentSender.collect {
//                                        it?.also {
//                                            intentSenderLauncher.launch(
//                                                IntentSenderRequest.Builder(it).build()
//                                            )
//                                        }
//                                    }
//                                }
//
//                            }
//                        )
//                    },
//                )


        }
    }
}

@Composable
fun loadPicture(url: String, placeholder: Painter? = null): Painter? {

    var state by remember {
        mutableStateOf(placeholder)
    }

    val options: RequestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)
    val context = LocalContext.current
    val result = object : CustomTarget<Bitmap>() {
        override fun onLoadCleared(p: Drawable?) {
            state = placeholder
        }

        override fun onResourceReady(
            resource: Bitmap,
            transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?,
        ) {
            state = BitmapPainter(resource.asImageBitmap())
        }
    }
    try {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .apply(options)
            .into(result)
    } catch (e: Exception) {
        // Can't use LocalContext in Compose Preview
    }
    return state
}

@Composable
fun ImageItem(
    uri: Uri,
) {
    val painter = loadPicture(
        url = uri.toString(),
        placeholder = painterResource(id = R.drawable.image_1)
    )
    painter?.let {
        Image(painter = it, contentDescription = null)
    }
}