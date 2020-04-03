package com.ayvytr.demo

import android.os.Bundle
import android.view.View
import com.ayvytr.adapter.SmartAdapter
import com.ayvytr.adapter.smart
import androidx.fragment.app.Fragment

/**
 * @author EDZ
 */
class MainFragment : Fragment() {
    lateinit var smartAdapter: SmartAdapter<Item>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        smartAdapter = smart {

        }
    }
}