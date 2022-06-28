package com.chaudharynabin6.localstorage.presentation.internalstorage_part1

import android.graphics.Bitmap


sealed class InternalStoragePart1Events {
    data class TakePhoto(val bitmap: Bitmap) : InternalStoragePart1Events()
    object  TogglePrivate : InternalStoragePart1Events()
    data class DeletePhoto(val fileName : String) : InternalStoragePart1Events()
}