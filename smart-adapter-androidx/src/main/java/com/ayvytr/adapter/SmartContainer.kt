package com.ayvytr.adapter

import android.view.View
import androidx.annotation.LayoutRes

/**
 * Container for [SmartAdapter], include [layoutId], [viewType] and [bind], bind for init view.
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.1.0
 */
open class SmartContainer<T>(@LayoutRes val layoutId: Int, val viewType: Int,
                             val bind: View.(item: T, position: Int) -> Unit) {
    override fun toString(): String {
        return "SmartContainer(layoutId=$layoutId, viewType=$viewType, bind=$bind)"
    }
}