package com.ayvytr.adapter

/**
 * @author admin
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
