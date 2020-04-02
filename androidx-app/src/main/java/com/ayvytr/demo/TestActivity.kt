package com.ayvytr.demo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.ayvytr.adapter.SmartAdapter
import androidx.ayvytr.adapter.SmartContainer
import androidx.ayvytr.adapter.smart
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayvytr.ktx.ui.getContext
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.item_type3.view.*

class TestActivity : AppCompatActivity() {
    lateinit var smartAdapter: SmartAdapter<Item>
    val list = listOf(Item("first", 1), Item("second", 2), Item("third", 1), Item("fourth", 0), Item("fifth", 1),
                      Item("third", 1), Item("fourth", 0), Item("fifth", 2))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        smartAdapter = smart<Item> {
            items = list
            bind = SmartContainer(R.layout.item, 0) {
                item_text.text = it.value
            }
            typeItems = listOf(SmartContainer(R.layout.item_type2, 1) {
                val tv = findViewById<TextView>(R.id.tv2)
                tv.text = it.value
//                item_text.text = it.value
            }, SmartContainer(R.layout.item_type3, 2) {
                tv3.text = it.value
//                item_text.text = it.value
            })
            type = {  it.type }
            typePosition = { it, _ -> it.type }
        }
        recycler_view.layoutManager = LinearLayoutManager(getContext())
        recycler_view.adapter = smartAdapter
    }
}
