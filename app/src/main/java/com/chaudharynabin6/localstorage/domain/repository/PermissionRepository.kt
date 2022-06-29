package com.chaudharynabin6.localstorage.domain.repository

import androidx.activity.result.ActivityResultLauncher
import com.chaudharynabin6.localstorage.domain.model.Permission

interface PermissionRepository {

    fun getInitialPermission() : Permission

    fun updatePermission(permission: Permission) : Array<String>?
}