package com.ayvytr.adapter.internal

import android.support.annotation.LayoutRes
import android.view.View
import com.ayvytr.adapter.SmartAdapter
import com.ayvytr.adapter.SmartContainer
import com.ayvytr.adapter.SmartDiffCallback
import com.ayvytr.adapter.internal.Internals.NO_GETTER

/**
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.3.0
 */
internal class SmartAdapterBuilderImpl<T>(@LayoutRes layoutId: Int = -1,
                                          bind: View.(item: T, position:Int) -> Unit = {_,_->}
) : SmartAdapterBuilder<T> {
    private val smartAdapter: SmartAdapter<T> = SmartAdapter()

    init {
        smartAdapter.map(SmartContainer(layoutId, 0, bind))
    }

    override fun build(): SmartAdapter<T> {
        return smartAdapter
    }

    override var items: List<T>
        get() = smartAdapter.list
        set(value) {
            smartAdapter.list.clear()
            smartAdapter.addAll(value)
        }

    override var itemViewOf: SmartContainer<T>
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
        set(value) {
            smartAdapter.map(value)
        }

    override var multiItemViewOf: List<SmartContainer<T>>
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
        set(value) {
            value.forEach {
                smartAdapter.map(it)
            }
        }

    override var type: (it: T) -> Int
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
        set(value) {
            smartAdapter.type { it, _ ->
                value(it)
            }
        }
    override var typePosition: (it: T, position: Int) -> Int
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
        set(value) {
            smartAdapter.type { it, position ->
                value(it, position)
            }
        }

    override var itemClick: (item: T, position: Int) -> Unit
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
        set(value) {
            smartAdapter.itemClickListener = value
        }

    override var itemLongClick: (item: T, position: Int) -> Unit
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
        set(value) {
            smartAdapter.itemLongClickListener = value
        }

    override fun diff(smartDiffCallback: SmartDiffCallback<T>, detectMovies: Boolean) {
        smartAdapter.diff(smartDiffCallback, detectMovies)
    }

}