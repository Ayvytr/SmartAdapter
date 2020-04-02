package androidx.ayvytr.adapter

import android.content.Context
import androidx.ayvytr.adapter.Internals.NO_GETTER
import androidx.fragment.app.Fragment

/**
 * @author ayvytr
 */

fun <T> Fragment.smart(init: SmartAdapterBuilder<T>.() -> Unit): SmartAdapter<T> =
        SmartAdapterBuilderImpl<T>(context!!).apply { init() }.build()

fun <T> Context.smart(init: SmartAdapterBuilder<T>.() -> Unit): SmartAdapter<T> =
        SmartAdapterBuilderImpl<T>(this).apply { init() }.build()

interface SmartAdapterBuilder<T> {
    var items: List<T>
    var bind: SmartContainer<T>
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get
    var typeItems: List<SmartContainer<T>>
    var type: (it: T) -> Int
    var typePosition: (it: T, position: Int) -> Int
    fun build(): SmartAdapter<T>
}