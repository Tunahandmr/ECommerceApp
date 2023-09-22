package com.tunahan.ecommerceapp.domain.model

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher

data class Image(
    val uri: Uri,
    val bitmap: Bitmap,
    var activityResultLauncher: ActivityResultLauncher<Intent>,
    var permissionLauncher: ActivityResultLauncher<String>
)
