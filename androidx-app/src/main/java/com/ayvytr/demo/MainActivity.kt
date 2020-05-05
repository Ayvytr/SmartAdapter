package com.ayvytr.demo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ayvytr.adapter.SmartAdapter
import com.ayvytr.adapter.SmartContainer
import com.ayvytr.adapter.SmartDiffCallback
import com.ayvytr.adapter.smart
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayvytr.ktx.context.toast
import com.ayvytr.ktx.ui.getContext
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.item_type3.view.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var smartAdapter: SmartAdapter<Item>
    val list = listOf(Item("first", 1), Item("second", 2), Item("third", 1), Item("fourth", 0), Item("fifth", 1),
                      Item("third", 1), Item("fourth", 0), Item("fifth", 2))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
//        initView2()
        initClick()
        initAdapter()
    }

    private fun initView2() {
        smartAdapter = smart(list, R.layout.item, {
            item_text.text = it.value
        }) {
            multiItemViewOf = listOf(SmartContainer(R.layout.item_type2, 1) {
                val tv = findViewById<TextView>(R.id.tv2)
                tv.text = it.value
//                item_text.text = it.value
            }, SmartContainer(R.layout.item_type3, 2) {
                tv3.text = it.value
//                item_text.text = it.value
            })
            type = { it.type }
//            typePosition = { it, _ -> it.type }
            itemClick = { it, position ->
                toast("clicked: $it $position")
            }
            itemLongClick = { it, position ->
                toast("long clicked: $it $position")
            }
        }
    }

    private fun initView() {
        smartAdapter = smart<Item> {
            items = list
            itemViewOf = SmartContainer(R.layout.item, 0) {
                item_text.text = it.value
            }
            multiItemViewOf = listOf(SmartContainer(R.layout.item_type2, 1) {
                val tv = findViewById<TextView>(R.id.tv2)
                tv.text = it.value
//                item_text.text = it.value
            }, SmartContainer(R.layout.item_type3, 2) {
                tv3.text = it.value
//                item_text.text = it.value
            })
            type = { it.type }
//            typePosition = { it, _ -> it.type }
            itemClick = { it, position ->
                toast("clicked: $it $position")
            }
            itemLongClick = { it, position ->
                toast("long clicked: $it $position")
            }
            diff(SmartDiffCallback())
        }
    }

    private fun initAdapter() {
        recycler_view.layoutManager = LinearLayoutManager(getContext())
        recycler_view.adapter = smartAdapter
    }

    private fun initClick() {
        btn_add.setOnClickListener {
            val index = if (smartAdapter.isEmpty()) 0 else Random.nextInt(smartAdapter.itemCount)
            smartAdapter.add(itemCreator(), index)
        }

        btn_remove_at.setOnClickListener {
            if (smartAdapter.isEmpty()) {
                return@setOnClickListener
            }

            if (smartAdapter.itemCount == 0) {
                smartAdapter.removeAt(0)
            } else {
                smartAdapter.removeAt(Random.nextInt(smartAdapter.itemCount))
            }
        }

        btn_remove.setOnClickListener {
            if (smartAdapter.isEmpty()) {
                return@setOnClickListener
            }

            val item = smartAdapter.list.firstOrNull { it.value == "Creator" }
            if (item != null) {
                smartAdapter.remove(item)
                toast("删除${item}成功")
            } else {
                toast("未找到!")
            }
        }

        btn_addAll.setOnClickListener {
            val list = mutableListOf<Item>()
            repeat(3) {
                list.add(itemCreator())
            }
            if (smartAdapter.isEmpty()) {
                smartAdapter.addAll(list, 0)
            } else {
                smartAdapter.addAll(list, Random.nextInt(smartAdapter.itemCount))
            }
        }

        btn_clear.setOnClickListener {
            smartAdapter.clear()
        }

        btn_remove_3.setOnClickListener {
            if (smartAdapter.itemCount < 3) {
                toast("itemCount < 3 !")
                return@setOnClickListener
            }

            var index = 0
            if (smartAdapter.itemCount != 3) {
                index = Random.nextInt(smartAdapter.itemCount - 3)
            }
            val subList = smartAdapter.list.subList(index, index + 3)
            smartAdapter.removeAll(subList)
            toast("删除成功")
        }

        btn_update.setOnClickListener {
            if (smartAdapter.isEmpty()) {
                return@setOnClickListener
            }
            val copyList = mutableListOf<Item>()
            smartAdapter.list.forEachIndexed { index, item ->
                copyList.add(Item("$index Update", item.type))
            }
            copyList.forEachIndexed { index, item ->
                item.value = "$index Update"
            }

            smartAdapter.update(copyList)
            toast("更新成功")
        }

        btn_reset.setOnClickListener {
            smartAdapter.update(list)
        }

        btn_update_for_append.setOnClickListener {
            val isAppendToHead = true
            smartAdapter.update(listOf(randomItem()), true, isAppendToHead)
            recycler_view.scrollToPosition(if(isAppendToHead) 0 else smartAdapter.itemCount)
        }
    }

    private fun itemCreator() = Item("Creator", Random.nextInt(0, 3))

    private fun randomItem(): Item {
        val value = Random.nextInt()
        return Item("Random" + value, Random.nextInt(0, 3))
    }
}
