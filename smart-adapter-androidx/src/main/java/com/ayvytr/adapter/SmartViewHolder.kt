package com.ayvytr.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * View holder for [SmartAdapter]
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.1.0
 */
class SmartViewHolder<T>(containerView: View, val viewType: Int,
                         private val smartContainer: SmartContainer<T>) :
    RecyclerView.ViewHolder(containerView) {
    fun bind(t: T,
             position: Int,
             bind: View.(item: T, position: Int) -> Unit = smartContainer.bind) {
        itemView.apply {
            bind(t, position)
        }
    }
}