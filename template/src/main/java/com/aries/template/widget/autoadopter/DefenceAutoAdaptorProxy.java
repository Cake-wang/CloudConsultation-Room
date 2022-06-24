package com.aries.template.widget.autoadopter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.aries.template.utils.DefenceUtil;

import java.util.ArrayList;
import java.util.List;

/******
 * 防止按钮重复提交
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/24 10:39 AM
 *
 */
public class DefenceAutoAdaptorProxy<T> extends AutoAdaptorProxy<T>{

    public DefenceAutoAdaptorProxy(RecyclerView recyclerView, int res, int spanCount, ArrayList<T> data, Context context) {
        super(recyclerView, res, spanCount, data, context);
        // 重新设置事件触发，添加反重复提交
        adaptor.setListener(new AutoObjectAdaptor.IItemListener() {
            @Override
            public void onItemClick(AutoObjectAdaptor.ViewHolder holder, int position, Object itemData) {
                if (listener!=null)
                    if (DefenceUtil.checkReSubmit("DefenceAutoAdaptorProxy.onItemClick"))
                        listener.onItemClick(holder,position, ((T) itemData));
            }

            @Override
            public void onItemViewDraw(AutoObjectAdaptor.ViewHolder holder, int position, Object itemData) {
                if (listener!=null)
                    listener.onItemViewDraw(holder,position, ((T) itemData));
            }
        });
    }
}
