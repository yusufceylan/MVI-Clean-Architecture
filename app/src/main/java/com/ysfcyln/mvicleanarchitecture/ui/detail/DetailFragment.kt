package com.ysfcyln.mvicleanarchitecture.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ysfcyln.base.BaseFragment
import com.ysfcyln.mvicleanarchitecture.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Detail Fragment
 */
@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    override val bindLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDetailBinding
        get() = FragmentDetailBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {
        // do something
    }

}