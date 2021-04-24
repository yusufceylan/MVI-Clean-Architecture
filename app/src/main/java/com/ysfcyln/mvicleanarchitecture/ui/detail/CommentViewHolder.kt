package com.ysfcyln.mvicleanarchitecture.ui.detail

import com.ysfcyln.base.BaseViewHolder
import com.ysfcyln.mvicleanarchitecture.databinding.RowCommentItemLayoutBinding
import com.ysfcyln.presentation.model.CommentUiModel

/**
 * ViewHolder class for Comment
 */
class CommentViewHolder constructor(
    private val binding : RowCommentItemLayoutBinding
) : BaseViewHolder<CommentUiModel, RowCommentItemLayoutBinding>(binding){

    override fun bind() {
        getRowItem()?.let {
            binding.comment = it
            binding.executePendingBindings()
        }
    }
}