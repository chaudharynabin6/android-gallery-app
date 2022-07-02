package com.chaudharynabin6.localstorage.presentation.external_storage

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chaudharynabin6.localstorage.domain.model.ExternalImageData
import com.chaudharynabin6.localstorage.presentation.internalstorage_part1.InternalStoragePart1Events
import com.chaudharynabin6.localstorage.presentation.internalstorage_part1.InternalStoragePart1ViewModel
import com.chaudharynabin6.localstorage.R


@Composable
fun ImageRowView(
    imageData: List<ExternalImageData>,
    modifier: Modifier,
    viewModel: InternalStoragePart1ViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val gridStateIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
    ) {

        items(
            count = imageData.size,
        ) { index ->
            LaunchedEffect(key1 = gridStateIndex) {
                viewModel.onEvent(event = InternalStoragePart1Events.GetIndex(gridStateIndex))
                Log.e("index ", "$gridStateIndex")
            }



        if(index >= gridStateIndex && index <= gridStateIndex + 5 ){
            if(viewModel.state.bitmap.isNotEmpty()){
                Image(
                    bitmap = viewModel.state.bitmap[0].asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
//                        https://developer.android.com/jetpack/compose/gestures
                    ,
                    contentScale = ContentScale.Crop
                )
            }

        }
            else{
            Image(
                painter = painterResource(id = R.drawable.image_0),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
//                        https://developer.android.com/jetpack/compose/gestures
                ,
                contentScale = ContentScale.Crop
            )
        }




            Image(
                painter = painterResource(id = R.drawable.image_0),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
//                        https://developer.android.com/jetpack/compose/gestures
                ,
                contentScale = ContentScale.Crop
            )
        }


    }
}
