package com.chaudharynabin6.localstorage.presentation.internalstorage_part1

import android.content.Context
import android.database.ContentObserver
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaudharynabin6.localstorage.data.mapper.toExternalImageData
import com.chaudharynabin6.localstorage.data.mapper.toImageData
import com.chaudharynabin6.localstorage.domain.repository.InternalStorageRepository
import com.chaudharynabin6.localstorage.domain.repository.PermissionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InternalStoragePart1ViewModel @Inject constructor(
    private val internalStorageRepository: InternalStorageRepository,
    private val permissionRepository: PermissionRepository,
    @ApplicationContext private val context : Context
) : ViewModel() {
    private lateinit var contentObserver: ContentObserver
    var state by mutableStateOf(Part1State())

    init {
        loadPhotosFromInternalStorage()
        val getInitialPermission = permissionRepository.getInitialPermission()
        state = state.copy(
            permission = getInitialPermission
        )
        loadPhotosFromExternalStorage()
        initContentObserver()
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
                if(state.permission.readPermission){
                    loadPhotosFromExternalStorage()
                }
            }
            is InternalStoragePart1Events.ChangePermission -> {
                state = state.copy(
                    permission = event.permission
                )
            }
            is InternalStoragePart1Events.GetIndex -> {
//                loadPhotosFromGivenIndex(index = event.index)
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


    private fun loadPhotosFromExternalStorage() {
        viewModelScope.launch {
            val photos = internalStorageRepository.loadPhotosFromExternalStorage()

            val photosExternalStorage = photos.map { it.toExternalImageData() }


            state = state.copy(
                externalImageDataList = photosExternalStorage,
                )
        }
    }

    private fun initContentObserver(){

        contentObserver = object  : ContentObserver(null){
            override fun onChange(selfChange: Boolean) {
                if(state.permission.readPermission){
                    loadPhotosFromExternalStorage()
                }
            }
        }
        context.contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true,
            contentObserver
        )


    }

    override fun onCleared() {
        context.contentResolver.unregisterContentObserver(
            contentObserver
        )
        super.onCleared()
    }
}