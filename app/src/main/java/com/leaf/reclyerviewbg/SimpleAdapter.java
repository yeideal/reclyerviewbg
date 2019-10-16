package com.leaf.reclyerviewbg;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


/**
 * Created by ye on 2018/3/14.
 */

public class SimpleAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SimpleAdapter(List<String> data) {
        super(R.layout.item_simple, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.textView, item);
    }
}
