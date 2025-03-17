package com.my.mvistudymultimodule

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityOptionsCompat
import com.my.mvistudymultimodule.core.base.ActivityNavigator
import com.my.mvistudymultimodule.feature.xml.view.activity.XmlHomeActivity

/**
 * Activity 전환
 */
class ActivityNavigatorImpl: ActivityNavigator {


    override fun navigateActivityToXmlActivity(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        options: ActivityOptionsCompat?,
        dataBundle: Bundle?
    ) {
        val intent = Intent(context, XmlHomeActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        dataBundle?.let { intent.putExtras(dataBundle) }
        launcher.launch(intent, options)
    }

    override fun navigateActivityToComposeActivity(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        options: ActivityOptionsCompat?,
        dataBundle: Bundle?
    ) {
        TODO("Not yet implemented")
    }

}