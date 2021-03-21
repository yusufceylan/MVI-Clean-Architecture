package com.ysfcyln.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Base ViewHolder class for RecyclerView
 */
abstract class BaseViewHolder<M, WB : ViewBinding> constructor(viewBinding: WB) :
    RecyclerView.ViewHolder(viewBinding.root) {

    private var item: M? = null

    fun doBindings(data: M?) {
        this.item = data
    }

    abstract fun bind()

    fun getRowItem(): M? {
        return item
    }
}