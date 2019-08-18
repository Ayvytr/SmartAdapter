package androidx.ayvytr.adapter

/**
 * 为外部提供的略简便的Diff Callback，从0.1.0较为繁琐的函数变量改为直接提供方法.
 * @author Ayvytr
 * @since 0.2.0
 */
open class SmartDiffCallback<T> {
    open fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem === newItem
    }

    open fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    open fun getChangePayload(oldItem: T, newItem: T): Any? {
        return null
    }

    open fun onBindPayloads(holder: SmartViewHolder<T>, item: T, payloads: List<Any>) {

    }
}
