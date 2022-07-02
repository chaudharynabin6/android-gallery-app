package com.chaudharynabin6.localstorage.presentation.internalstorage_part1

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chaudharynabin6.localstorage.R
import com.chaudharynabin6.localstorage.domain.model.Permission
import com.chaudharynabin6.localstorage.presentation.external_storage.ExternalStorageImageAndroidView


@Composable
fun InternalStoragePart1Screen(
    viewModel: InternalStoragePart1ViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    Column {
        val takePictureLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {
                it?.also {
                    viewModel.onEvent(InternalStoragePart1Events.TakePhoto(bitmap = it))
                }
            }
        val permissionLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) {
                val readPermission =
                    it[Manifest.permission.READ_EXTERNAL_STORAGE] ?: state.permission.readPermission
                val writePermission = it[Manifest.permission.WRITE_EXTERNAL_STORAGE]
                    ?: state.permission.writePermission


                val updatedPermission = Permission(
                    readPermission = readPermission,
                    writePermission = writePermission
                )

                viewModel.onEvent(InternalStoragePart1Events.ChangePermission(updatedPermission))


            }


        LaunchedEffect(state.permissionToRequest) {
            viewModel.onEvent(InternalStoragePart1Events.GetPermissionToRequest)
            permissionLauncher.launch(state.permissionToRequest.toTypedArray())
        }
        Text(
            text = "Internal Photos",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )

        ImageGridViewPart1(
            imageData = state.photos,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        )

//        ExternalStorageImageGridView(imageData = state.externalImageDataList, modifier = Modifier
//            .fillMaxWidth().height(200.dp)
//            )
//        ExternalStorageImageListView(
//            imageData = state.externalImageDataList,
//            modifier = Modifier.fillMaxWidth().weight(1f)
//        )
        Text(
            text = "External Photos",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
        ExternalStorageImageAndroidView(
            imageData = state.externalImageDataList,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Switch(checked = state.isPrivate, onCheckedChange = {
                viewModel.onEvent(InternalStoragePart1Events.TogglePrivate)
            })

            IconButton(onClick = {
               takePictureLauncher.launch()
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