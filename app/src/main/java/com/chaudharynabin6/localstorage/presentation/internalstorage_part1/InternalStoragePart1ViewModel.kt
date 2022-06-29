package com.chaudharynabin6.localstorage.presentation.internalstorage_part1

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaudharynabin6.localstorage.data.mapper.toImageData
import com.chaudharynabin6.localstorage.domain.repository.InternalStorageRepository
import com.chaudharynabin6.localstorage.domain.repository.PermissionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InternalStoragePart1ViewModel @Inject constructor(
    private val internalStorageRepository: InternalStorageRepository,
    private val permissionRepository: PermissionRepository
) : ViewModel() {

    var state by mutableStateOf(Part1State())

    init {
        loadPhotosFromInternalStorage()
        val getInitialPermission = permissionRepository.getInitialPermission()
        state = state.copy(
            permission = getInitialPermission
        )
    }

    fun onEvent(event: InternalStoragePart1Events) {

        when (event) {
            is InternalStoragePart1Events.TakePhoto -> {
                internalStorageRepository.takePhoto(state.isPrivate, bitmap = event.bitmap, writePermission = state.permission.writePermission)
                loadPhotosFromInternalStorage()
            }
            is InternalStoragePart1Events.TogglePrivate -> {
                state = state.copy(
                    isPrivate = !state.isPrivate
                )
            }
            is InternalStoragePart1Events.DeletePhoto -> {
                internalStorageRepository.deletePhotoFromInternalStorage(event.fileName)
                loadPhotosFromInternalStorage()
            }
            is InternalStoragePart1Events.GetPermissionToRequest -> {
                val getInitialPermission = permissionRepository.getInitialPermission()

                val permissionToRequest = permissionRepository.updatePermission(getInitialPermission)
                state = state.copy(
                    permissionToRequest = permissionToRequest?.toList() ?: emptyList()
                )
            }
            is InternalStoragePart1Events.ChangePermission -> {
                state = state.copy(
                    permission = event.permission
                )
            }
        }
    }

    private fun loadPhotosFromInternalStorage() {
        viewModelScope.launch {
            val photos = internalStorageRepository.loadPhotosFromInternalStorage()
            state = state.copy(
                photos = photos.map { it.toImageData() }
            )
        }
    }
}