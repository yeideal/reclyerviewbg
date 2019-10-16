package com.leaf.reclyerviewbg;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * 随着背景滚动
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    private BgDecoration bgDecoration;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        bgDecoration = new BgDecoration(this, R.drawable.map_default_bg);

        recyclerView.addItemDecoration(bgDecoration);

        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add(i + "");
        }
        adapter = new SimpleAdapter(data);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                setBg(this, "http://imgsrc.baidu.com/imgad/pic/item/7af40ad162d9f2d32ce4b0f6a3ec8a136327cc36.jpg");
                break;
            case R.id.button2:
                bgDecoration.restoreDefaultBm();
                bgDecoration.clearMap();
                adapter.notifyDataSetChanged();
                break;
            case R.id.button3:
                startActivity(new Intent(this, MainActivity2.class));
                break;
        }
    }

    private void setBg(final Context context, final String url) {
        Glide.with(context).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                        Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                           DataSource dataSource, boolean isFirstResource) {
                Bitmap bitmap = BitmapUtils.drawableToBitmap(resource);
                bgDecoration.setBmp(bitmap);
                bgDecoration.clearMap();
                adapter.notifyDataSetChanged();
                return false;
            }
        }).preload();
    }

}
