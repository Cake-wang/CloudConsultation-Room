package com.aries.template.widget.autoadopter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/******
 * 这个适配器，只做样式处理
 * - RV 点击对象的样式
 * - RV 点击对象后，其他对象的样式
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/21 10:30 AM
 *
 */
public class AutoThemeAdaptor extends AutoObjectAdaptor{

    public AutoThemeAdaptor(RecyclerView recyclerView, int res, int spanCount, ArrayList<Object> data, Context context) {
        super(recyclerView, res, spanCount, data, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        // 设置样式事件触发
        if (themeListener!=null){
            if (position==currentClickPosition){
                themeListener.onClickTheme(holder,position,data.get(position));
            } else{
                themeListener.unClickTheme(holder,position,data.get(position));
            }
        }
        super.onBindViewHolder(holder, position, payloads);
    }

    /** item 监听对象 */
    public IItemThemeListener themeListener;

    // setter && getter
    public void setThemeListener(IItemThemeListener themeListener) {
        this.themeListener = themeListener;
    }

    /**
     * 对外监听类
     */
    public interface IItemThemeListener{
        // 有点击样式的规定
        void onClickTheme(ViewHolder holder, int position, Object  itemData);
        // 没有点击的样式规定
        void unClickTheme(ViewHolder holder, int position, Object  itemData);
    }


}
