package com.aries.template.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.xuexiang.xui.widget.flowlayout.BaseTagAdapter;


/**
 * @author xuexiang
 * @date 2017/11/21 上午10:44
 */
public class FlowTagAdapter extends BaseTagAdapter<String, TextView> {

    public FlowTagAdapter(Context context) {
        super(context);
    }

    @Override
    protected TextView newViewHolder(View convertView) {
        return (TextView) convertView.findViewById(R.id.tv_tag);
    }

    @Override
    protected int getLayoutId() {
        if(GlobalConfig.thirdFactory.equals("3")||GlobalConfig.thirdFactory.equals("2")){
            return R.layout.adapter_item_tag_l;
        }else {
            return R.layout.adapter_item_tag;
        }
//        return R.layout.adapter_item_tag;
    }

    @Override
    protected void convert(TextView textView, String item, int position) {
        textView.setText(item);
    }
}
