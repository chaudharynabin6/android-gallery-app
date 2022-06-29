package com.chaudharynabin6.localstorage.data.mapper

import com.chaudharynabin6.localstorage.data.permission.PermissionData
import com.chaudharynabin6.localstorage.domain.model.Permission

fun PermissionData.toPermission() : Permission {

    return Permission(
        readPermission = readPermission,
        writePermission = writePermission
    )
}

fun Permission.toPermissionData() : PermissionData{

    return  PermissionData(
        readPermission = readPermission,
        writePermission = writePermission
    )
}