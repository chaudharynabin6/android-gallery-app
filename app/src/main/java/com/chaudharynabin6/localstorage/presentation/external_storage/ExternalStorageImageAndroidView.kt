package com.chaudharynabin6.localstorage.presentation.external_storage


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.chaudharynabin6.localstorage.domain.model.ExternalImageData
import com.chaudharynabin6.localstorage.presentation.internalstorage_part1.InternalStoragePart1ViewModel

@Composable
fun ExternalStorageImageAndroidView(
    imageData: List<ExternalImageData>,
    modifier: Modifier,
    viewModel: InternalStoragePart1ViewModel = hiltViewModel(),
) {
    val gridState = rememberLazyGridState()
    val gridIndex by remember {
        derivedStateOf { gridState.firstVisibleItemIndex }
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
            key = { index: Int, item: ExternalImageData ->
                item.id
            }
        ) { index: Int, item: ExternalImageData ->

//            if (index >= gridIndex && index < gridIndex + 6) {

//                AndroidView(factory = {
//                    ImageView(it)
//
//                }, modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp)
//                ) { imageView ->
//
//                    imageView.apply {
//                        scaleType = ImageView.ScaleType.CENTER_CROP
//                        setImageURI(item.contentUri)
//                    }
//
//                }


//                Box(modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(text = "${item.name}")
//                }

                AsyncImage(
                    model = item.contentUri,
                    contentDescription = null,
//                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )

//            } else {
//                Box(modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp))
//            }

        }
    }
}