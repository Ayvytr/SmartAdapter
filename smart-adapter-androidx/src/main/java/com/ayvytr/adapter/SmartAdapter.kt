package com.ayvytr.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * [RecyclerView.Adapter]的继承类，可以继承重写open方法，可以使用[smart]满足大多数需求.
 * @param context 按照目前的写法，context未用到，迭代几个小版本，如果还没有用到，可以删除
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.1.0
 */
open class SmartAdapter<T>(val context: Context,
                           val list: MutableList<T> = mutableListOf(),
                           bind: SmartContainer<T>? = null
) : RecyclerView.Adapter<SmartViewHolder<T>>() {
    init {
        bind?.let {
            map(it)
        }
    }
    /**
     * @see [type]
     */
    private var viewTypePredicate: (item: T, position: Int) -> Int = { _, _ -> 0 }

    private val map = mutableMapOf<Int, SmartContainer<T>>()

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

    var itemClickListener: (T, Int) -> Unit = { _, _ -> }
    var itemLongClickListener: (T, Int) -> Unit = { _, _ -> }

    /**
     * 设置 view type 获取方法.
     * @see [viewTypePredicate]
     */
    fun type(predicate: (item: T, position: Int) -> Int): SmartAdapter<T> {
        this.viewTypePredicate = predicate
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
                itemClickListener(list[position], position)
            }

            holder.itemView.setOnLongClickListener {
                itemLongClickListener(list[position], position)
                true
            }
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int): Int {
        return viewTypePredicate(list[position], position)
    }


    open fun map(@LayoutRes layoutId: Int, type: Int,
                 bind: View.(item: T) -> Unit): SmartAdapter<T> {
        return map(SmartContainer(layoutId, type, bind))
    }

    /**
     * 设置[RecyclerView]多类型item view的方法.
     * @see [smartContainer]
     */
    open fun map(smartContainer: SmartContainer<T>): SmartAdapter<T> {
        map[smartContainer.viewType] = smartContainer
        return this
    }

    /**
     * 设置diff callback(实际使用了[smartDiffCallback]包装了[DiffUtil.Callback]).
     * @param smartDiffCallback `null` :取消diff，
     * @param detectMovies `true` :尝试检测移动的item，参见[DiffUtil.calculateDiff].
     */
    open fun diff(smartDiffCallback: SmartDiffCallback<T>? = SmartDiffCallback(),
                  detectMovies: Boolean = true): SmartAdapter<T> {
        this.detectMovies = detectMovies

        if (smartDiffCallback == null) {
            removeDiff()
        } else {
            createDiffCallback(smartDiffCallback)
        }

        return this
    }

    /**
     * 移除diff callback.
     */
    fun removeDiff() {
        if (hasDiff()) {
            diffCallback = null
        }
    }

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


    protected open fun dispatchUpdate() {
        val diffResult = DiffUtil.calculateDiff(diffCallback!!, detectMovies)
        diffResult.dispatchUpdatesTo(this)
        list.clear()
        list.addAll(tempNewList)
    }

    /**
     * 添加[item]到[list]的[index],默认是追加到末尾.
     */
    fun add(item: T, index: Int = list.size) {
        if (diffCallback != null) {
            getTempList().also {
                it.add(index, item)
            }.also {
                dispatchUpdate()
            }
        } else {
            list.add(index, item)
            notifyDataSetChanged()
        }
    }

    /**
     * [isAppend]=false: 更新[list]为[newList];
     * [isAppend]=true: [isAppendToHead]=true:追加到[list]开头;[isAppendToHead]=false:追加到[list]末尾.
     *
     * @since 0.3.1 新增参数[isAppend],[isAppendToHead],支持下拉刷新和加载更多共用一个方法.
     *
     * note:设置了diffCallback之后，增减item如果没在视野里，看不到增减动画，需要手动调用
     * [RecyclerView.scrollToPosition]。这个应该算是[RecyclerView]本身的问题.
     */
    fun update(newList: List<T>, isAppend: Boolean = false, isAppendToHead: Boolean = false) {
        if (isAppend) {
            addAll(newList, if (isAppendToHead) 0 else list.size)
        } else {
            if (diffCallback != null) {
                tempNewList = newList.toMutableList()
                dispatchUpdate()
            } else {
                list.clear()
                list.addAll(newList)
                notifyDataSetChanged()
            }
        }
    }

    /**
     * 删除[list]中的[item].
     */
    fun remove(item: T): Boolean {
        var succeed = false
        if (diffCallback != null) {
            getTempList().also {
                succeed = it.remove(item)
            }.also {
                dispatchUpdate()
            }
            return succeed
        } else {
            succeed = list.remove(item)
            notifyDataSetChanged()
        }
        return succeed
    }

    /**
     * 如果存在，删除[list]中[index]所在的item.
     */
    fun removeAt(index: Int) {
        if (index < 0 || index >= list.size) {
            return
        }

        if (diffCallback != null) {
            getTempList().also {
                it.removeAt(index)
            }.also {
                dispatchUpdate()
            }
        } else {
            list.removeAt(index)
            notifyDataSetChanged()
        }
    }

    /**
     * 添加[items]到[list]的[index]位置，默认追加到末尾.
     */
    fun addAll(items: List<T>, index: Int = list.size) {
        if (diffCallback != null) {
            getTempList().also {
                it.addAll(index, items)
            }.also {
                dispatchUpdate()
            }
        } else {
            list.addAll(index, items)
            notifyDataSetChanged()
        }
    }

    /**
     * 删除[list]中[items].
     */
    fun removeAll(items: List<T>) {
        if (diffCallback != null) {
            getTempList().also {
                it.removeAll(items)
            }.also {
                dispatchUpdate()
            }
        } else {
            list.removeAll(items)
            notifyDataSetChanged()
        }
    }

    /**
     * 清空[list]中所有元素
     */
    fun clear() {
        if (list.isEmpty()) {
            return
        }

        if (diffCallback != null) {
            mutableListOf<T>().also {
                tempNewList.clear()
                dispatchUpdate()
            }
        } else {
            list.clear()
            notifyDataSetChanged()
        }
    }

    /**
     * [list]是否为空.
     */
    fun isEmpty(): Boolean {
        return list.isEmpty()
    }


    /**
     * [SmartAdapter]是否设置了[diffCallback].
     */
    fun hasDiff(): Boolean {
        return diffCallback != null
    }


    fun click(onItemClickListener: (item: T, position: Int) -> Unit): SmartAdapter<T> {
        this.itemClickListener = onItemClickListener
        return this
    }

    fun longClick(onItemLongClickListener: (item: T, position: Int) -> Unit): SmartAdapter<T> {
        this.itemLongClickListener = onItemLongClickListener
        return this
    }
}


