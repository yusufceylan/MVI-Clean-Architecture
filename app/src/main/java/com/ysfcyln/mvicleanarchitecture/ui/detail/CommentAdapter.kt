package com.ysfcyln.mvicleanarchitecture.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ysfcyln.base.BaseRecyclerAdapter
import com.ysfcyln.mvicleanarchitecture.databinding.RowCommentItemLayoutBinding
import com.ysfcyln.presentation.model.CommentUiModel

/**
 * Adapter class for Comments RecyclerView
 */
class CommentAdapter : BaseRecyclerAdapter<CommentUiModel, RowCommentItemLayoutBinding, CommentViewHolder>(CommentItemDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = RowCommentItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentViewHolder(binding = binding)
    }
}

class CommentItemDiffUtil : DiffUtil.ItemCallback<CommentUiModel>() {
    override fun areItemsTheSame(oldItem: CommentUiModel, newItem: CommentUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CommentUiModel, newItem: CommentUiModel): Boolean {
        return oldItem == newItem
    }

}