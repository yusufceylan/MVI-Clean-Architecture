package com.ysfcyln.mvicleanarchitecture.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.ysfcyln.base.BaseActivity
import com.ysfcyln.mvicleanarchitecture.databinding.ActivityMainBinding
import com.ysfcyln.presentation.contract.MainContract
import com.ysfcyln.presentation.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val bindLayout: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {
        // do nothing
    }

}