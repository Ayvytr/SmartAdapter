package com.ayvytr.adapter

/**
 * 为外部提供的略简便的Diff Callback，从0.1.0较为繁琐的函数变量改为直接提供方法，注意：实际场景中，区分Item是不是相等是用id或者
 * 其他能确定唯一性的字段来区分的，所以，一定要重写 [areItemsTheSame]；区分Item内容是不是相等需要判断每个字段是不是相等，所以
 * 需要重写 [areContentsTheSame]；数据更新Item有闪烁，是因为全量刷新时，数据Bean更新了但是未重写[areItemsTheSame], 或者重写了
 * 但是返回null，请一定要注意这几点.
 *
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.2.0
 */
open class SmartDiffCallback<T> {
    open fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem === newItem
    }

    open fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    /**
     * 这里默认是返回null的，为了避免白光闪烁现象，提供默认实现，返回newItem，必须和[onBindPayloads]一起重写.
     */
    open fun getChangePayload(oldItem: T, newItem: T): Any? {
        return newItem
    }

    /**
     * 必须和[getChangePayload]一起重写.
     */
    open fun onBindPayloads(holder: SmartViewHolder<T>, item: T,
                            position: Int, payloads: List<Any>) {
        holder.bind(item, position)
    }
}
