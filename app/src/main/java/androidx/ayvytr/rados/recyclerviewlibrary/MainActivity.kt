package androidx.ayvytr.rados.recyclerviewlibrary

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.ayvytr.adapter.SmartAdapter
import androidx.ayvytr.adapter.SmartContainer
import androidx.ayvytr.adapter.SmartDiffCallback
import androidx.ayvytr.adapter.bind
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayvytr.rados.recyclerviewlibrary.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.item_4.view.*
import kotlinx.android.synthetic.main.item_custom.view.*
import kotlinx.android.synthetic.main.item_second.view.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    val list = mutableListOf(Item("first", 2), Item("second", 2), Item("third", 1), Item("fourth", 1), Item("fifth", 1))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    lateinit var baseAdapter: SmartAdapter<Item>

    private fun initView() {
        baseAdapter = recycler_view.bind(list, R.layout.item, 1) { item: Item ->
            item_text.text = item.value
            //            container.setOnClickListener {
            //                toast(item.value)
            //            }
        }

            //        recycler_view.bind(list, R.layout.item) {
            //            item_text.text = it.value
            //            container.setOnClickListener {
            //
            //            }
            //        }

            .map(R.layout.item_second, 2) { item: Item ->
                item_second_text.text = item.value
                //                container_second.setOnClickListener {
                //                    toast(item.value)
                //                }
            }
            .map(R.layout.item_custom, 3) { item: Item ->
                item_custom_text.text = item.value
                //                container_custom.setOnClickListener {
                //                    toast(item.value)
                //                }
            }
            .map(BindMap4())
            //            .diff({ oldItem, newItem -> oldItem.type == newItem.type },
            //                  { oldItem, newItem -> oldItem.value == newItem.value },
            //                  { oldItem, newItem ->
            //                      if (oldItem.value != newItem.value) {
            //                          newItem
            //                      } else null
            //                  },
            //                  { holder, item, payloads -> holder.bind(item) })
            .diff(Diff())
            .type { it.type }
            .click { item: Item, i: Int ->
                toast("clicked $i $item")
            }
            .longClick { item: Item, i: Int ->
                toast("long clicked $i $item")
            }
            .build()
        //                .map(predicate = {it:Item, _ ->  it.type == 3 }) { item: Item ->
        //                    item_custom_text.text = item.value
        //                    container_custom.setOnClickListener {
        //                        toast(item.value)
        //                    }
        //                }
        //            .layoutManager(LinearLayoutManager(this))
        btn_add.setOnClickListener {
            val index = if (baseAdapter.isEmpty()) 0 else Random.nextInt(baseAdapter.itemCount)
            baseAdapter.add(itemCreator(), index)
        }

        btn_remove_at.setOnClickListener {
            if (baseAdapter.isEmpty()) {
                return@setOnClickListener
            }

            if (baseAdapter.itemCount == 0) {
                baseAdapter.removeAt(0)
            } else {
                baseAdapter.removeAt(Random.nextInt(baseAdapter.itemCount))
            }
        }

        btn_remove.setOnClickListener {
            if (baseAdapter.isEmpty()) {
                return@setOnClickListener
            }

            val item = baseAdapter.list.firstOrNull { it.value == "Creator" }
            if (item != null) {
                baseAdapter.remove(item)
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
            if (baseAdapter.isEmpty()) {
                baseAdapter.addAll(list, 0)
            } else {
                baseAdapter.addAll(list, Random.nextInt(baseAdapter.itemCount))
            }
        }

        btn_clear.setOnClickListener {
            baseAdapter.clear()
        }

        btn_remove_3.setOnClickListener {
            if (baseAdapter.itemCount < 3) {
                toast("itemCount < 3 !")
                return@setOnClickListener
            }

            var index = 0
            if (baseAdapter.itemCount != 3) {
                index = Random.nextInt(baseAdapter.itemCount - 3)
            }
            val subList = baseAdapter.list.subList(index, index + 3)
            baseAdapter.removeAll(subList)
            toast("删除成功")
        }

        btn_update.setOnClickListener {
            if (baseAdapter.isEmpty()) {
                return@setOnClickListener
            }
            val copyList = mutableListOf<Item>()
            baseAdapter.list.forEachIndexed { index, item ->
                copyList.add(Item("$index Update", item.type))
            }
            copyList.forEachIndexed { index, item ->
                item.value = "$index Update"
            }

            baseAdapter.update(copyList)
            toast("更新成功")
        }

        btn_reset.setOnClickListener {
            baseAdapter.update(list)
        }
        //        delay(2000) {
        //            recycler_view.update(list2)
        //            baseAdapter.update(list2)
        //        }
    }

    private fun itemCreator() = Item("Creator", Random.nextInt(1, 5))

    private fun toast(value: String) {
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mid_add_diff       -> {
                if (baseAdapter.hasDiff()) {
                    toast("Has diff!")
                } else {
                    baseAdapter.diff(Diff())
                }
                true
            }
            R.id.mid_remove_diff    -> {
                if (baseAdapter.hasDiff().not()) {
                    toast("No diff!")
                } else {
                    baseAdapter.removeDiff()
                }
                true
            }
            R.id.mid_grid           -> {
                baseAdapter.layoutManager(GridLayoutManager(applicationContext, 3))
                true
            }
            R.id.mid_linear         -> {
                baseAdapter.layoutManager(LinearLayoutManager(applicationContext))
                true
            }
            R.id.mid_java_activiity -> {
                startActivity(Intent(this, JavaActivity::class.java))
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

class Diff : SmartDiffCallback<Item>({ oldItem, newItem -> oldItem === newItem },
                                     { oldItem, newItem -> oldItem === newItem && oldItem.value == newItem.value },
                                     { item: Item, item1: Item -> },
                                     { param: Any, item: Item, mutableList: MutableList<Any> -> }
)

class BindMap4 : SmartContainer<Item>(R.layout.item_4, 4, { item_text_4.text = it.value }) {
}

//class LocalFactory(val activity: AppCompatActivity) : LayoutFactory {
//    override fun createView(parent: ViewGroup, type: Int): View {
//        return LayoutInflater.from(activity).inflate(R.layout.item_custom,
//                parent, false)
//    }
//}

fun delay(delay: Long, func: () -> Unit) {
    val handler = Handler()
    handler.postDelayed({
                            try {
                                func()
                            } catch (e: Exception) {
                                println(e.toString())
                            }
                        }, delay)
}