package com.chaudharynabin6.localstorage.presentation.internalstorage_part1

import android.graphics.Bitmap
import android.net.Uri
import com.chaudharynabin6.localstorage.domain.model.Permission


sealed class InternalStoragePart1Events {
    data class TakePhoto(val bitmap: Bitmap) : InternalStoragePart1Events()
    object TogglePrivate : InternalStoragePart1Events()
    data class DeletePhoto(val fileName: String) : InternalStoragePart1Events()
    object GetPermissionToRequest : InternalStoragePart1Events()
    data class ChangePermission(val permission: Permission) : InternalStoragePart1Events()
    data class GetIndex(val index: Int) : InternalStoragePart1Events()
    data class DeleteExternalStoragePhoto(val uri: Uri) : InternalStoragePart1Events()
    object PhotoToDeleteAfterPermissionSuccessful : InternalStoragePart1Events()
    object PhotoToDeleteAfterPermissionUnsuccessful : InternalStoragePart1Events()
}