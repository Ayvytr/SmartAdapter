package com.ayvytr.demo

import android.R.attr.text
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ayvytr.adapter.SmartAdapter
import com.ayvytr.adapter.SmartContainer
import com.ayvytr.adapter.SmartDiffCallback
import com.ayvytr.adapter.smart
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayvytr.demo.databinding.ActivityMainBinding
import com.ayvytr.ktx.context.toast
import com.ayvytr.ktx.ui.getContext
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var smartAdapter: SmartAdapter<Item>
    val list = listOf(Item("first", 1), Item("second", 2), Item("third", 1), Item("fourth", 0), Item("fifth", 1),
                      Item("third", 1), Item("fourth", 0), Item("fifth", 2))
    private val binding by viewBinding<ActivityMainBinding>()

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
            findViewById<TextView>(R.id.item_text).text = it.value
        }) {
            multiItemViewOf = listOf(SmartContainer(R.layout.item_type2, 1) {
                val tv = findViewById<TextView>(R.id.tv2)
                tv.text = it.value
//                item_text.text = it.value
            }, SmartContainer(R.layout.item_type3, 2) {
                findViewById<TextView>(R.id.tv3).text = it.value
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
                findViewById<TextView>(R.id.item_text).text = it.value
            }
            multiItemViewOf = listOf(SmartContainer(R.layout.item_type2, 1) {
                val tv = findViewById<TextView>(R.id.tv2)
                tv.text = it.value
//                item_text.text = it.value
            }, SmartContainer(R.layout.item_type3, 2) {
                findViewById<TextView>(R.id.tv3).text = it.value
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
        binding.recyclerView.layoutManager = LinearLayoutManager(getContext())
        binding.recyclerView.adapter = smartAdapter
    }

    private fun initClick() {
        binding.apply {

            btnAdd.setOnClickListener {
                val index =
                    if (smartAdapter.isEmpty()) 0 else Random.nextInt(smartAdapter.itemCount)
                smartAdapter.add(itemCreator(), index)
            }

            btnRemoveAt.setOnClickListener {
                if (smartAdapter.isEmpty()) {
                    return@setOnClickListener
                }

                if (smartAdapter.itemCount == 0) {
                    smartAdapter.removeAt(0)
                } else {
                    smartAdapter.removeAt(Random.nextInt(smartAdapter.itemCount))
                }
            }

            btnRemove.setOnClickListener {
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

            btnAddAll.setOnClickListener {
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

            btnClear.setOnClickListener {
                smartAdapter.clear()
            }

            btnRemove3.setOnClickListener {
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

            btnUpdate.setOnClickListener {
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

            btnReset.setOnClickListener {
                smartAdapter.update(list)
            }

            btnUpdateForAppend.setOnClickListener {
                val isAppendToHead = true
                smartAdapter.update(listOf(randomItem()), true, isAppendToHead)
                recyclerView.scrollToPosition(if (isAppendToHead) 0 else smartAdapter.itemCount)
            }
        }
    }

    private fun itemCreator() = Item("Creator", Random.nextInt(0, 3))

    private fun randomItem(): Item {
        val value = Random.nextInt()
        return Item("Random" + value, Random.nextInt(0, 3))
    }
}
