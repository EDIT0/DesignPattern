package com.my.mvistudymultimodule.feature.home.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.my.mvistudymultimodule.core.base.BaseDataBindingActivity
import com.my.mvistudymultimodule.core.util.ClickUtil.onSingleClickWithDebounce
import com.my.mvistudymultimodule.core.util.ClickUtil.onSingleClickWithCancel
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.feature.home.R
import com.my.mvistudymultimodule.feature.home.databinding.ActivityHomeBinding
import com.my.mvistudymultimodule.feature.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn

@AndroidEntryPoint
class HomeActivity : BaseDataBindingActivity<ActivityHomeBinding>(R.layout.activity_home) {

    private val homeVM: HomeViewModel by viewModels()

    val launchActivity: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        LogUtil.d_dev("launchActivity ${result.resultCode} / ${result.data}")
        LogUtil.d_dev("${result.data?.getStringExtra("Data")}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnXML.onSingleClickWithDebounce(
            onClicked = {
                LogUtil.d_dev("MYTAG ${javaClass.simpleName} btnXML clicked")

                val animList: List<androidx.core.util.Pair<View, String>> = listOf(
                    androidx.core.util.Pair<View, String>(binding.btnXML, "btnXMLToXmlHome"),
                )
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *animList.toTypedArray())
                val dataBundle = Bundle().apply {
                    putString("key1", "value1")
                    putInt("key2", 123)
                }
                activityNavigator.navigateActivityToXmlActivity(this, launchActivity, options, dataBundle)

//                activityScope.launch {
//                    for(i in 1 .. 10) {
//                        LogUtil.d_dev("MYTAG ${javaClass.simpleName} btnXML clicked ${i}")
//                        delay(1000L)
//                        binding.btnXML.text = i.toString()
//                    }
//                }
            },
            debounceTime = 1000L
        ).launchIn(activityScope)

        binding.btnCompose.onSingleClickWithCancel(
            onClicked = {
                LogUtil.d_dev("MYTAG ${javaClass.simpleName} btnCompose clicked")

                val animList: List<androidx.core.util.Pair<View, String>> = listOf(
                    androidx.core.util.Pair<View, String>(binding.btnXML, "btnComposeToZHome"),
                )
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *animList.toTypedArray())
                val dataBundle = Bundle().apply {
                    putString("key1", "value1")
                    putInt("key2", 123)
                }
                activityNavigator.navigateActivityToComposeActivity(this, launchActivity, options, dataBundle)

//                for(i in 1 .. 10) {
//                    LogUtil.d_dev("MYTAG ${javaClass.simpleName} btnCompose clicked ${i}")
//                    delay(1000L)
//                    binding.btnCompose.text = i.toString()
//                }
            },
            scope = lifecycleScope
        ).launchIn(activityScope)

    }
}