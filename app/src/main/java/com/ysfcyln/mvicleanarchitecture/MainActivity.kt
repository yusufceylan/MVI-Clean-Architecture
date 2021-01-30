package com.ysfcyln.mvicleanarchitecture

import android.os.Bundle
import android.view.LayoutInflater
import com.ysfcyln.base.BaseActivity
import com.ysfcyln.mvicleanarchitecture.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val bindLayout: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {
        // init views
    }
}