package com.myk.numa.otoasobi.ui.timeline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.myk.numa.otoasobi.R
import com.myk.numa.otoasobi.data.Voice
import com.myk.numa.otoasobi.ui.core.BaseViewHolderBinding

class TimeLineAdapter(
    private val onClickItemTimeLineListener: ((voice: Voice) -> Unit)? = null
): RecyclerView.Adapter<TimeLineAdapter.ItemTimeLineViewHolder>() {

    val data: MutableList<Voice> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTimeLineViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater,
            R.layout.item_list,
            parent,
            false
        )
        return ItemTimeLineViewHolder((viewDataBinding))
    }

    override fun onBindViewHolder(holder: ItemTimeLineViewHolder, position: Int) {
        holder.onBindView(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun addData(newData: Voice, clear: Boolean) {
        if (clear) data.clear()
        data.add(newData)

        notifyDataSetChanged()
    }

    inner class ItemTimeLineViewHolder(
        private val viewBinding: ViewDataBinding
    ) : BaseViewHolderBinding<Voice>(viewBinding) {
        override fun onBindView(data: Voice) {
            viewBinding.root.setOnClickListener {
            }
            viewBinding.executePendingBindings()
        }
    }

}