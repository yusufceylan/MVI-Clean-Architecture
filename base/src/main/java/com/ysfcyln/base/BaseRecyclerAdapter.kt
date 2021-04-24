package com.ysfcyln.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding

/**
 * Base Adapter class for RecyclerView
 */
abstract class BaseRecyclerAdapter<M : Any, WB : ViewBinding, VH : BaseViewHolder<M, WB>>(callback: DiffUtil.ItemCallback<M>) :
    ListAdapter<M, VH>(callback) {

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.doBindings((getItem(position)))
        holder.bind()
    }

    override fun submitList(items: List<M>?) {
        super.submitList(items ?: emptyList())
    }
}