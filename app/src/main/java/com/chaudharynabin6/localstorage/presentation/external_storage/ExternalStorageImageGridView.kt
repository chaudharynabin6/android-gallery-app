package com.chaudharynabin6.localstorage.presentation.external_storage

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chaudharynabin6.localstorage.R
import com.chaudharynabin6.localstorage.domain.model.ExternalImageData
import com.chaudharynabin6.localstorage.presentation.internalstorage_part1.InternalStoragePart1ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ExternalStorageImageGridView(
    imageData: List<ExternalImageData>,
    modifier: Modifier,
    viewModel: InternalStoragePart1ViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val gridState = rememberLazyGridState()
    val gridStateIndex by remember { derivedStateOf { gridState.firstVisibleItemIndex } }
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Adaptive(minSize = 128.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
    ) {

        itemsIndexed(
            items = imageData,
        ) { index: Int, item: ExternalImageData ->
            LaunchedEffect(key1 = gridStateIndex) {
                Log.e("grid  index", gridStateIndex.toString())
            }



            if (index >= gridStateIndex && index <= gridStateIndex + 5) {
//

                val bitmap by remember {
                    derivedStateOf { mutableStateOf<Bitmap?>(null) }
                }

                var job: Job? = null
                LaunchedEffect(gridStateIndex) {
                    delay(3000)
                    job?.cancel()
                    job = scope.launch(Dispatchers.IO) {
                        delay(1)
                        try {
//                            val source =
//                                ImageDecoder.createSource(viewModel.context.contentResolver,
//                                    item.contentUri)
//                            bitmap.value = ImageDecoder.decodeBitmap(source)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }


                }

                bitmap.value?.let {

                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .height(128.dp)
//                        https://developer.android.com/jetpack/compose/gestures
                        ,
                        contentScale = ContentScale.FillBounds
                    )
                }
            } else {
                Image(
                    painter = painterResource(R.drawable.image_0),
                    contentDescription = null,
                    modifier = Modifier
                        .height(128.dp)
//                        https://developer.android.com/jetpack/compose/gestures
                    ,
                    contentScale = ContentScale.FillBounds
                )
            }

        }
    }
}