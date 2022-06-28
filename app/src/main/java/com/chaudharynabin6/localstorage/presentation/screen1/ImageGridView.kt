package com.chaudharynabin6.localstorage.presentation.screen1

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.chaudharynabin6.localstorage.domain.model.ImageData


@Composable
fun ImageGridView(
    imageData: List<ImageData>,
    modifier: Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        modifier = modifier
    ) {
        items(imageData) { item: ImageData ->

            Image(
                bitmap = item.bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.height(128.dp),
                contentScale = ContentScale.FillBounds
            )

        }
    }
}