package com.chaudharynabin6.localstorage.presentation.internalstorage_part1

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chaudharynabin6.localstorage.domain.model.ImageData


@Composable
fun ImageGridViewPart1(
    imageData: List<ImageData>,
    modifier: Modifier,
    viewModel: InternalStoragePart1ViewModel = hiltViewModel()
) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        modifier = modifier
    ) {
        items(
            items = imageData,
            key = {
                it.fileName
            }
        ) { item: ImageData ->

            Image(
                bitmap = item.bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .height(128.dp)
//                        https://developer.android.com/jetpack/compose/gestures
                    .pointerInput(key1 = item.fileName) {
                        detectTapGestures(
                            onLongPress = {
                                viewModel.onEvent(InternalStoragePart1Events.DeletePhoto(item.fileName))
                            }
                        )
                    },
                contentScale = ContentScale.FillBounds
            )

        }
    }
}