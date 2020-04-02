package androidx.ayvytr.adapter

import android.content.Context
import androidx.ayvytr.adapter.Internals.NO_GETTER
import com.ayvytr.logger.L

/**
 * @author EDZ
 */
class SmartAdapterBuilderImpl<T>(private val context: Context) : SmartAdapterBuilder<T> {
    private val smartAdapter: SmartAdapter<T> = SmartAdapter(context)

    override fun build(): SmartAdapter<T> {
        return smartAdapter
    }

    override var items: List<T>
        get() = smartAdapter.list
        set(value) {
            smartAdapter.list.clear()
            smartAdapter.addAll(value)
        }

    override var bind: SmartContainer<T>
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
        set(value) {
            smartAdapter.map(value)
        }

    override var typeItems: List<SmartContainer<T>>
        get() = smartAdapter.map.values.toList()
        //        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = Internals.noGetter()
        set(value) {
            value.forEach {
                smartAdapter.map(it)
            }

            L.e(typeItems, smartAdapter.map, smartAdapter.map.size)
//            val itemCount = smartAdapter.itemCount
//            if(itemCount > 0) {
//                smartAdapter.notifyItemRangeChanged(0, itemCount)
//            }
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

//    override var bind: SmartContainer<T>
//        get() = smartAdapter
//        set(value) {}
//
}