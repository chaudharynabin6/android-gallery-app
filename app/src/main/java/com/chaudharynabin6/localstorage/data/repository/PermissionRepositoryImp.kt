package com.chaudharynabin6.localstorage.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.chaudharynabin6.localstorage.data.mapper.toPermission
import com.chaudharynabin6.localstorage.data.permission.PermissionData
import com.chaudharynabin6.localstorage.domain.model.Permission
import com.chaudharynabin6.localstorage.domain.repository.PermissionRepository
import dagger.hilt.android.qualifiers.ApplicationContext

import javax.inject.Inject

class PermissionRepositoryImp @Inject constructor(
   @ApplicationContext private val context: Context
) : PermissionRepository {
    override fun getInitialPermission() : Permission {

        val hasReadPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val hasWritePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val isMinSdk29orGreater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q


        val permissionData =  PermissionData(
            readPermission = hasReadPermission,
            writePermission = hasWritePermission || isMinSdk29orGreater
        )

        return  permissionData.toPermission()
    }

    override fun updatePermission(permission: Permission): Array<String>? {
       val permissionToRequest = mutableListOf<String>()
// if read permission not granted
        if(!permission.readPermission){
           permissionToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if(!permission.writePermission){
            permissionToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        return if(permissionToRequest.isNotEmpty()){
           permissionToRequest.toTypedArray()
        } else null

    }
}