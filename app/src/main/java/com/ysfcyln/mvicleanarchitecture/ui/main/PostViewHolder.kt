package com.ysfcyln.mvicleanarchitecture.ui.main

import com.ysfcyln.base.BaseViewHolder
import com.ysfcyln.mvicleanarchitecture.databinding.RowPostItemLayoutBinding
import com.ysfcyln.presentation.model.PostUiModel

/**
 * ViewHolder class for Post
 */
class PostViewHolder constructor(
    private val binding : RowPostItemLayoutBinding,
    private val click : ((PostUiModel?) -> Unit)? = null
) : BaseViewHolder<PostUiModel, RowPostItemLayoutBinding>(binding) {


    init {
        binding.root.setOnClickListener {
            click?.invoke(getRowItem())
        }
    }

    override fun bind() {
        getRowItem()?.let {
            binding.post = it
            binding.executePendingBindings()
        }
    }
}