package androidx.ayvytr.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.1.0
 */

/**
 * 快速创建[SmartAdapter]的入口.
 *
 * 用法:
 * ```
 *     rv = YourRecyclerView()
 *     rv.bind(list, R.layout.your_layout){
 *          //view binding
 *     }
 *       .build()
 * ```
 *
 */
fun <T> RecyclerView.bind(items: List<T>,
                          @LayoutRes layoutId: Int,
                          bind: (View.(item: T) -> Unit)): SmartAdapter<T> {
    this.layoutManager = LinearLayoutManager(context)
    return SmartAdapter(context, items.toMutableList())
        .map(layoutId, 0, bind)
}

/**
 * 快速创建[SmartAdapter]的入口.
 *
 * 用法:
 * ```
 *     rv = YourRecyclerView()
 *     rv.bind(list, itemViewType, R.layout.your_layout){
 *          //view binding
 *     }
 *       .build()
 * ```
 *
 */
fun <T> RecyclerView.bind(items: List<T>,
                          @LayoutRes layoutId: Int,
                          type: Int = 0,
                          bind: (View.(item: T) -> Unit)): SmartAdapter<T> {
    this.layoutManager = LinearLayoutManager(context)
    return SmartAdapter(context, items.toMutableList())
        .map(layoutId, type, bind)
}

/**
 * 真实Adapter类，是[bind]的返回类型，你可以继承[SmartAdapter], 但是大多数情况下，调用[bind]足够了.
 */
open class SmartAdapter<T>(val context: Context, val list: MutableList<T> = mutableListOf(),
                           bind: SmartContainer<T>? = null)
    : RecyclerView.Adapter<SmartViewHolder<T>>() {
    /**
     * @see [type]
     */
    private var viewTypePredicate: (item: T, position: Int) -> Int = { _, _ -> 0 }

    val map = mutableMapOf<Int, SmartContainer<T>>()

    /**
     * [diffCallback] 使用的临时List，充当oldList.
     */
    private var tempNewList: MutableList<T> = arrayListOf()

    /**
     * 为外部提供的较为简便的Diff Callback类.
     * @see [SmartDiffCallback]
     */
    private var smartDiffCallback: SmartDiffCallback<T> = SmartDiffCallback()

    /**
     * 内部真实创建的Diff Callback.
     */
    var diffCallback: DiffUtil.Callback? = null

    var detectMovies = true

    private var onItemClickListener: (T, Int) -> Unit = { _, _ -> }
    private var onItemLongClickListener: (T, Int) -> Unit = { _, _ -> }

//    var type:(item: T) -> Int = {0}
    /**
     * Function to get view type.
     * @see [viewTypePredicate]
     */
    fun type(predicate: (item: T, position: Int) -> Int): SmartAdapter<T> {
        this.viewTypePredicate = predicate
        return this
    }

    /**
     * Real function to create [SmartAdapter], you must to call it last step.
     */
    //TODO:删除
    @Deprecated(message = "删除")
    fun build(): SmartAdapter<T> {
//        rv.adapter = this
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartViewHolder<T> {
        map[viewType]?.let {
            return SmartViewHolder(LayoutInflater.from(parent.context).inflate(it.layoutId, parent, false), viewType, it)
        } ?: throw IllegalStateException("ViewType: $viewType not found, cannot create ViewHolder!")
    }

    override fun onBindViewHolder(holder: SmartViewHolder<T>, position: Int) {
        map[holder.viewType]?.let {
            holder.bind(list[position])
            holder.itemView.setOnClickListener {
                onItemClickListener(list[position], position)
            }

            holder.itemView.setOnLongClickListener {
                onItemLongClickListener(list[position], position)
                true
            }
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int): Int {
        return viewTypePredicate(list[position], position)
    }


    fun map(@LayoutRes layoutId: Int, type: Int, bind: View.(item: T) -> Unit): SmartAdapter<T> {
        return map(SmartContainer(layoutId, type, bind))
    }

    /**
     * Multiple item view type creator, if you call this explicit, you must to call [type] to describe how to get item
     * view type.
     */
    fun map(smartContainer: SmartContainer<T>): SmartAdapter<T> {
        map[smartContainer.viewType] = smartContainer
        return this
    }

//    /**
//     * Sets up a layout manager for the recycler view.
//     */
//    fun layoutManager(manager: RecyclerView.LayoutManager): SmartAdapter<T> {
//        rv.layoutManager = manager
//        return this
//    }


    /**
     * Create different callback.
     */
    fun diff(smartDiffCallback: SmartDiffCallback<T> = SmartDiffCallback(),
             detectMovies: Boolean = true): SmartAdapter<T> {
        this.detectMovies = detectMovies
        createDiffCallback(smartDiffCallback)
        return this
    }

//    /**
//     * Different remover.
//     */
//    fun removeDiff() {
//        if (hasDiff()) {
//            diffCallback = null
//        }
//    }

    /**
     * Real different creator.
     */
    private fun createDiffCallback(smartDiffCallback: SmartDiffCallback<T>) {
        this.diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return smartDiffCallback.areItemsTheSame(list[oldItemPosition], tempNewList[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return smartDiffCallback.areContentsTheSame(list[oldItemPosition], tempNewList[newItemPosition])
            }

            override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                return smartDiffCallback.getChangePayload(list[oldItemPosition], tempNewList[newItemPosition])
            }

            override fun getOldListSize(): Int {
                return list.size
            }

            override fun getNewListSize(): Int {
                return tempNewList.size
            }
        }
    }

    override fun onBindViewHolder(holder: SmartViewHolder<T>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            smartDiffCallback.onBindPayloads(holder, list[position], payloads)
        }
    }

    private fun getTempList(): MutableList<T> {
        tempNewList = mutableListOf<T>().apply {
            list.forEach { add(it) }
        }
        return tempNewList
    }


    /**
     * Calculate difference.
     */
    private fun diffUpdate() {
        val diffResult = DiffUtil.calculateDiff(diffCallback!!, detectMovies)
        diffResult.dispatchUpdatesTo(this)
        list.clear()
        list.addAll(tempNewList)
    }

    /**
     * Add [item] to [index] of [list], default append.
     */
    fun add(item: T, index: Int = list.size) {
        if (diffCallback != null) {
            getTempList().also {
                it.add(index, item)
            }.also {
                diffUpdate()
            }
        } else {
            list.add(index, item)
            notifyDataSetChanged()
        }
    }

    /**
     * Set [list] to [newList].
     */
    fun update(newList: List<T>) {
        if (diffCallback != null) {
            tempNewList = newList.toMutableList()
            diffUpdate()
        } else {
            list.clear()
            list.addAll(newList)
            notifyDataSetChanged()
        }
    }

    /**
     * Remove [item] from [list].
     */
    fun remove(item: T): Boolean {
        var succeed = false
        if (diffCallback != null) {
            getTempList().also {
                succeed = it.remove(item)
            }.also {
                diffUpdate()
            }
            return succeed
        } else {
            succeed = list.remove(item)
            notifyDataSetChanged()
        }
        return succeed
    }

    /**
     * Remove item at [index], if exists.
     */
    fun removeAt(index: Int) {
        if (index < 0 || index >= list.size) {
            return
        }

        if (diffCallback != null) {
            getTempList().also {
                it.removeAt(index)
            }.also {
                diffUpdate()
            }
        } else {
            list.removeAt(index)
            notifyDataSetChanged()
        }
    }

    /**
     * Add [items] to [index] of [list], default append.
     */
    fun addAll(items: List<T>, index: Int = list.size) {
        if (diffCallback != null) {
            getTempList().also {
                it.addAll(index, items)
            }.also {
                diffUpdate()
            }
        } else {
            list.addAll(index, items)
            notifyDataSetChanged()
        }
    }

    /**
     * Remove [items] from [list].
     */
    fun removeAll(items: List<T>) {
        if (diffCallback != null) {
            getTempList().also {
                it.removeAll(items)
            }.also {
                diffUpdate()
            }
        } else {
            list.removeAll(items)
            notifyDataSetChanged()
        }
    }

    /**
     * Remove all items from [list].
     */
    fun clear() {
        if (list.isEmpty()) {
            return
        }

        if (diffCallback != null) {
            mutableListOf<T>().also {
                tempNewList.clear()
                diffUpdate()
            }
        } else {
            list.clear()
            notifyDataSetChanged()
        }
    }

    /**
     * Returns `true` if [list] is empty.
     */
    fun isEmpty(): Boolean {
        return list.isEmpty()
    }


    /**
     * Returns `true` if [SmartAdapter] has different callback.
     */
    fun hasDiff(): Boolean {
        return diffCallback != null
    }


    fun click(onItemClickListener: (item: T, position: Int) -> Unit): SmartAdapter<T> {
        this.onItemClickListener = onItemClickListener
        return this
    }

    fun longClick(onItemLongClickListener: (item: T, position: Int) -> Unit): SmartAdapter<T> {
        this.onItemLongClickListener = onItemLongClickListener
        return this
    }
}

/**
 * Container for [SmartAdapter], include [layoutId], [viewType] and [bind], bind for init view.
 */
open class SmartContainer<T>(@LayoutRes val layoutId: Int, val viewType: Int, val bind: View.(item: T) -> Unit) {
    override fun toString(): String {
        return "SmartContainer(layoutId=$layoutId, viewType=$viewType, bind=$bind)"
    }
}


/**
 * View holder for [SmartAdapter]
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