package com.ayvytr.adapter.internal

import com.ayvytr.adapter.SmartAdapter
import com.ayvytr.adapter.SmartContainer
import com.ayvytr.adapter.SmartDiffCallback
import com.ayvytr.adapter.internal.Internals.NO_GETTER


/**
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.3.0
 */
interface SmartAdapterBuilder<T> {
    /**
     * 设置[SmartAdapter.list].
     */
    var items: List<T>

    /**
     * 设置一个类型的item view，等于设置一个[SmartContainer]，包含layout id, view type, 初始化view的[SmartContainer.bind]方法，
     * 不管是不是同时设置了[itemViewOf],[multiItemViewOf]，只要view type不重复，有多少个不同的view type就有
     * 多少个item view type.
     */
    var itemViewOf: SmartContainer<T>
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get

    /**
     * 设置多个类型的item view，等于设置多个[SmartContainer].
     * 不管是不是同时设置了[itemViewOf],[multiItemViewOf]，只要view type不重复，有多少个不同的view type就有
     * 多少个item view type.
     */
    var multiItemViewOf: List<SmartContainer<T>>
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get

    /**
     * 通过当前item获取view type，和[typePosition]功能相同，但会相互覆盖.
     */
    var type: (it: T) -> Int
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get

    /**
     * 通过当前item和position获取view type，和[type]功能相同，但会相互覆盖.
     */
    var typePosition: (it: T, position: Int) -> Int
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get

    /**
     * 点击事件.
     */
    var itemClick: (item: T, position: Int) -> Unit
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get

    /**
     * 长按点击事件.
     */
    var itemLongClick: (item: T, position: Int) -> Unit
        @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get

    /**
     * 设置diff callback，[SmartDiffCallback]做了包装，[SmartAdapter]内部还是使用了[DiffUtil.Callback].
     *
     */
    fun diff(smartDiffCallback: SmartDiffCallback<T> = SmartDiffCallback(),
             detectMovies: Boolean = true)

    fun build(): SmartAdapter<T>
}