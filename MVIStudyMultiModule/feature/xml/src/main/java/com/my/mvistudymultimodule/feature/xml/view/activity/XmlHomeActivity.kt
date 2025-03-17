package com.my.mvistudymultimodule.feature.xml.view.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.my.mvistudymultimodule.core.base.BaseDataBindingActivity
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.feature.xml.R
import com.my.mvistudymultimodule.feature.xml.databinding.ActivityXmlHomeBinding
import com.my.mvistudymultimodule.feature.xml.event.home.XmlHomeViewModelEvent
import com.my.mvistudymultimodule.feature.xml.viewmodel.XmlHomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class XmlHomeActivity : BaseDataBindingActivity<ActivityXmlHomeBinding>(R.layout.activity_xml_home) {

    private val xmlHomeVM: XmlHomeViewModel by viewModels()

    private val navChangeListener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
        LogUtil.i_dev("${controller} / ${destination.label} / ${arguments}")
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

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fcv) as NavHostFragment
        val navController = navHostFragment.navController

        requestViewModelEvent(XmlHomeViewModelEvent.SettingNavigation(this@XmlHomeActivity, navController))
    }

    /**
     * Request viewmodel event
     *
     * @param xmlHomeViewModelEvent
     */
    private fun requestViewModelEvent(xmlHomeViewModelEvent: XmlHomeViewModelEvent) {
        when(xmlHomeViewModelEvent) {
            is XmlHomeViewModelEvent.SettingNavigation -> {
                xmlHomeVM.handleViewModelEvent(xmlHomeViewModelEvent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        xmlHomeVM.xmlHomeNavController?.addOnDestinationChangedListener(navChangeListener)
    }

    override fun onPause() {
        super.onPause()
        xmlHomeVM.xmlHomeNavController?.removeOnDestinationChangedListener(navChangeListener)
    }
}