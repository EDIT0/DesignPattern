package com.my.mvistudymultimodule.core.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityOptionsCompat

interface ActivityNavigator {
    fun navigateActivityToXmlActivity(context: Context, launcher: ActivityResultLauncher<Intent>, options: ActivityOptionsCompat?, dataBundle: Bundle?)
    fun navigateActivityToComposeActivity(context: Context, launcher: ActivityResultLauncher<Intent>, options: ActivityOptionsCompat?, dataBundle: Bundle?)
}