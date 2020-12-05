package com.myk.numa.otoasobi.ui.core

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolderBinding<E>(
    private val bindingView: ViewDataBinding
) : RecyclerView.ViewHolder(bindingView.root) {

    val context: Context by lazy { bindingView.root.context }

    /**
     * binding.setVariable(BR.obj, obj);
     * binding.executePendingBindings(); <== important
     */
    abstract fun onBindView(data: E)

    fun setOnItemClick(onItemClick: (data: E, position: Int) -> Unit, data: E, position: Int) {
        bindingView.root.setOnClickListener {
            onItemClick.invoke(data, position)
        }
    }
}