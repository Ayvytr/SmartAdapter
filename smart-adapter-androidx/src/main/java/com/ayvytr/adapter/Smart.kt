package com.ayvytr.adapter

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.ayvytr.adapter.internal.SmartAdapterBuilder
import com.ayvytr.adapter.internal.SmartAdapterBuilderImpl

/**
 * [smart]: 为[Fragment]和[Context]提供的创建[SmartAdapter]的方法.
 * @author Ayvytr <a href="https://github.com/Ayvytr" target="_blank">'s GitHub</a>
 * @since 0.3.0
 */

fun <T> Fragment.smart(init: SmartAdapterBuilder<T>.() -> Unit): SmartAdapter<T> =
        SmartAdapterBuilderImpl<T>().apply { init() }.build()

fun <T> Fragment.smart(list: List<T>,
                       @LayoutRes layoutId: Int,
                       bind: View.(item: T, position: Int) -> Unit,
                       init: SmartAdapterBuilder<T>.() -> Unit): SmartAdapter<T> =
        SmartAdapterBuilderImpl<T>(layoutId, bind).apply {
            items = list
            init()
        }.build()

fun <T> Context.smart(init: SmartAdapterBuilder<T>.() -> Unit): SmartAdapter<T> =
        SmartAdapterBuilderImpl<T>().apply { init() }.build()

fun <T> Context.smart(list: List<T>,
                      @LayoutRes layoutId: Int,
                      bind: View.(item: T, position: Int) -> Unit,
                      init: SmartAdapterBuilder<T>.() -> Unit): SmartAdapter<T> =
        SmartAdapterBuilderImpl<T>(layoutId, bind).apply {
            items = list
            init()
        }.build()