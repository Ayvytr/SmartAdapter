package com.ayvytr.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * View holder for [SmartAdapter]
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.1.0
 */
class SmartViewHolder<T>(containerView: View, val viewType: Int,
                         private val smartContainer: SmartContainer<T>) :
    RecyclerView.ViewHolder(containerView) {
    fun bind(t: T, bind: View.(item: T) -> Unit = smartContainer.bind) {
        itemView.apply {
            bind(t)
        }
    }
}