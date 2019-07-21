package androidx.ayvytr.rados.recyclerviewlibrary;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ayvytr.rados.recyclerviewlibrary.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.ayvytr.adapter.SmartAdapterKt;
import androidx.ayvytr.adapter.SmartContainer;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

/**
 * @author admin
 */
public class JavaActivity extends AppCompatActivity {

    private RecyclerView rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);
        initView();
    }

    private void initView() {
        rv = findViewById(R.id.recycler_view);
        List<Item> list = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            list.add(new Item("value" + i, i % 4));
        }
        SmartAdapterKt.bind(rv, list, R.layout.item, 0, new Function2<View, Item, Unit>() {
            @Override
            public Unit invoke(View view, Item item) {
                TextView tv = view.findViewById(R.id.item_text);
                tv.setText(item.getValue());
                return null;
            }
        }).map(R.layout.item_second, 1, new Function2<View, Item, Unit>() {
            @Override
            public Unit invoke(View view, Item item) {
                TextView tv = view.findViewById(R.id.item_second_text);
                tv.setText(item.getValue());
                return null;
            }
        }).map(R.layout.item_custom, 2, new Function2<View, Item, Unit>() {
            @Override
            public Unit invoke(View view, Item item) {
                TextView tv = view.findViewById(R.id.item_custom_text);
                tv.setText(item.getValue());
                return null;
            }
        }).map(new Map4())
                      .type(new Function1<Item, Integer>() {

                          @Override
                          public Integer invoke(Item item) {
                              return item.getType();
                          }
                      }).click(new Function2<Item, Integer, Unit>() {
            @Override
            public Unit invoke(Item item, Integer integer) {
                Toast.makeText(JavaActivity.this, "click " + item.toString(), Toast.LENGTH_SHORT).show();
                return null;
            }
        }).longClick(new Function2<Item, Integer, Unit>() {
            @Override
            public Unit invoke(Item item, Integer integer) {
                Toast.makeText(JavaActivity.this, "long click " + item.toString(), Toast.LENGTH_SHORT)
                     .show();
                return null;
            }
        }).build();
    }
}

class Map4 extends SmartContainer<Item> {
    public Map4() {
        super(R.layout.item_4, 3, new Function2<View, Item, Unit>() {
            @Override
            public Unit invoke(View view, Item item) {
                TextView tv = view.findViewById(R.id.item_text_4);
                tv.setText(item.getValue());
                return null;
            }
        });
    }
}