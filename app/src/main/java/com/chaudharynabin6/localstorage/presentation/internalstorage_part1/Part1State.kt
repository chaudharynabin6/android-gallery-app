package com.chaudharynabin6.localstorage.presentation.internalstorage_part1

import com.chaudharynabin6.localstorage.domain.model.ImageData

data class Part1State(
    val photos : List<ImageData> = emptyList(),
    val isPrivate : Boolean = true,
)
