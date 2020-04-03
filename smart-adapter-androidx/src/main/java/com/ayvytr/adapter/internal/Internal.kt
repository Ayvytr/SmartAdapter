package com.ayvytr.adapter.internal

/**
 * 内部方法.
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.3.0
 */
object Internals {
    const val NO_GETTER: String = "Property does not have a getter"

    fun noGetter(): Nothing = throw Exception("Property does not have a getter")
}
