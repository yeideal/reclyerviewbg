package com.leaf.reclyerviewbg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 固定背景
 */
public class MainActivity2 extends AppCompatActivity {
    RecyclerView recyclerView;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add(i + "");
        }
        adapter = new SimpleAdapter(data);
        recyclerView.setAdapter(adapter);
    }

}
