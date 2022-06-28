package com.chaudharynabin6.localstorage.presentation.internalstorage_part1

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chaudharynabin6.localstorage.R


@Composable
fun InternalStoragePart1Screen(
    viewModel: InternalStoragePart1ViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    Column(

    ) {
        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()){
            it?.also {
                viewModel.onEvent(InternalStoragePart1Events.TakePhoto(bitmap = it))
            }
        }
        ImageGridViewPart1(imageData = state.photos, modifier = Modifier.fillMaxSize().weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Switch(checked = state.isPrivate, onCheckedChange = {
                viewModel.onEvent(InternalStoragePart1Events.TogglePrivate)
            })

            IconButton(onClick = {
               launcher.launch()
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_camera_alt_24),
                    contentDescription = null,
                    tint = Color.Gray
                    ,
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp))
            }
        }
    }


}