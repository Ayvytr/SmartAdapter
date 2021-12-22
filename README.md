# SmartAdapter 

[![](https://img.shields.io/badge/jCenter-0.3.2-red.svg)](https://bintray.com/ayvytr/maven/smart-adapter/_latestVersion)



为RecyclerView提供的泛型Adapter，使用Kotlin编写，充分利用了Kotlin的特性，可直接使用smart{}创建Adapter实例，使用简便，功能很赞，内部封装了DiffUtil的处理逻辑，只需设置diff callback，无需操心调用DiffUtil.calculateDiff。



| 有DiffCallback，item增减有动画 | 没有DiffCallback，item增减没有动画 |
| ------------------------------ | ---------------------------------- |
| ![](media\with_diff.gif)       | ![](media\without_diff.gif)        |





## Import

mavenCentral（0.3.3+）

```
implementation "io.github.ayvytr:smart-adapter-androidx:0.3.3"
```



jcenter

```
Android:
	implementation "com.ayvytr:smart-adapter:0.3.2"
Androidx:
	implementation "com.ayvytr:smart-adapter-androidx:0.3.2"
```



Android:

​	implementation "com.ayvytr:smart-adapter:0.3.2"

Androidx:

​	implementation "com.ayvytr:smart-adapter-androidx:0.3.2"





## [Javadoc](https://ayvytr.github.io/projects/smartadapter/javadoc/index.html)（暂时还是旧版本的。因为还没解决dokka跨域问题）



## 说明

0.3.3开始只更新androidx的库

## ChangeLog

* 0.3.3 支持mavenCentral
* 0.3.2 增加bind方法position参数，方便点击监听获取position
* 0.3.1
    SmartAdapter.update()新增参数isAppend,isAppendToHead,支持下拉刷新和加载更多共用一个方法
* 0.3.0
  1. 废弃了RecyclerView.bind方法，因为只适用于RecyclerView。ViewPager2也使用了RecyclerView.Adapter
  2. 增加smart方法创建SmartAdapter，调用简单，不再只适用RecyclerView
* 0.2.2
    增加SmartDiffCallback默认实现



## 用法:

### 0.3.0+

直接使用smart方法创建SmartAdapter或者继承SmartAdapter重写。

```kotlin
//直接使用smart方法创建adapter

//单个item：
val smartAdapter = smart(list, R.layout.item, {it,_->
            item_text.text = it.value
        }) {}

//多个item以及更多参数：
val smartAdapter = smart{
    			//设置items
            items = list
    			//设置一个item view，包括layout id, item view type, view数据绑定方法
            itemViewOf = SmartContainer(R.layout.item, 0) {it,_->
                item_text.text = it.value
            }
    			//设置多个item view，和itemViewOf不冲突，但是要注意item view type不能重复
            multiItemViewOf = listOf(SmartContainer(R.layout.item_type2, 1) {it,_->
                val tv = findViewById<TextView>(R.id.tv2)
                tv.text = it.value
//                item_text.text = it.value
            }, SmartContainer(R.layout.item_type3, 2) {it,_->
                tv3.text = it.value
//                item_text.text = it.value
            })
    			//获取当前item的view type
            type = { it.type }
    			//通过item和position,获取当前item的view type，和type二选一即可
//            typePosition = { it, _ -> it.type }
    			//点击事件
            itemClick = { it, position ->
                toast("clicked: $it $position")
            }
    			//长按事件
            itemLongClick = { it, position ->
                toast("long clicked: $it $position")
            }
    			//设置Diff callback，内部包装了DiffUtil.DiffCallback，设置了之后不用再操心调用DiffUtil.calculateDiff了
            diff(SmartDiffCallback())
}
recycler_view.adapter = smartAdapter


```





### 0.3.0之前的用法（不建议再使用）

```kotlin
//单个item
recycler_view.bind(list, R.layout.item, 1) { item: Item ->    
	item_text.text = item.value    
}}.build()


```



```kotlin
//多种item
recycler_view.bind(list, R.layout.item, 1) { item: Item ->
            item_text.text = item.value
        }
        	//anther item view
            .map(R.layout.item_second, 2) { item: Item ->
                item_second_text.text = item.value
            }
            .map(R.layout.item_custom, 3) { item: Item ->
                item_custom_text.text = item.value
            }
            .map(BindMap4())
            //add custom diff util
            //            .diff({ oldItem, newItem -> oldItem.type == newItem.type },
            //                  { oldItem, newItem -> oldItem.value == newItem.value },
            //                  { oldItem, newItem ->
            //                      if (oldItem.value != newItem.value) {
            //                          newItem
            //                      } else null
            //                  },
            //                  { holder, item, payloads -> holder.bind(item) })
            //another way to add custom diff util
            .diff(Diff())
            //how to get item type from your item.
            .type { it.type }
            //item click listener
            .click { item: Item, i: Int ->
                toast("clicked $i $item")
            }
            //item long click listener
            .longClick { item: Item, i: Int ->
                toast("long clicked $i $item")
            }
            //real method to create adapter
            .build()
            
class Diff : SmartDiffCallback<Item>({ oldItem, newItem -> oldItem === newItem },
                                     { oldItem, newItem -> oldItem === newItem && oldItem.value == newItem.value },
                                     { item: Item, item1: Item -> },
                                     { param: Any, item: Item, mutableList: MutableList<Any> -> }
)

class BindMap4 : SmartContainer<Item>(R.layout.item_4, 4, { item_text_4.text = it.value }) {
}
```















