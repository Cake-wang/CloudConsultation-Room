package com.aries.template.widget.autoadopter;

import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/******
 * 快速RV适配器
 * 用法：
 * AutoAdaptor adaptor =  new AutoAdaptor(recyclerView,R.layout.item_doctor,2,newDatas,getContext());创建代理
 * adaptor.setListener(new AutoAdaptor.IItemListener() 创建对应的事件任务
 * adaptor.notifyDataSetChanged(); 强制RV刷新，用于第二次以上输入数据后，刷新界面
 *
 * Map<String ,Object> map = new HashMap<>();
 * map.put(KEY,value) //将数据封装在Map里，注意自己规定一个KEY来存和取，这样能让数据脱离范型
 *
 * public void onItemViewDraw(AutoAdaptor.ViewHolder holder, int position, Map itemData) {
 *  // 根据ID 处理界面数据，这些ID，都些在 R.layout.item_doctor 单一展示layout里面的，必须一一对应
 *  GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail item = ((GetConsultsAndRecipesResultEntity.QueryArrearsSummary.Recipes.RecipeDetail) itemData.get(KEY_ITEM_CURRENT_RECIPES));
 *       ((TextView)holder.itemView.findViewById(R.id.jtjk_doc_item_name_tv)).setText(item.getName());
 *       ((TextView)holder.itemView.findViewById(R.id.jtjk_doc_item_introduce_tv)).setText(item.getIntroduce());
 * }
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/10 11:04 AM
 *
 */
public class AutoAdaptorProxy<T>{

    /** RV 数据代理对象, 这里不需要管理数据，数据统一由 adaptor管理，这里只规定输入数据的类型 */
    private AutoObjectAdaptor adaptor;
    /** item 监听对象 */
    private IItemListener<T> listener;

    public AutoAdaptorProxy(RecyclerView recyclerView, @LayoutRes int res, int spanCount , List<T> data, Context context){
        this(recyclerView,res,spanCount,new ArrayList<>(data),context);
    }

    /**
     * 将 adaptor 和 RV 链接在一起
     * @param recyclerView 代理对象
     * @param res 代理对象要显示的最小单位 显示界面
     * @param spanCount 显示多少列
     * @param data 显示注入的数据
     * @param context recyclerView显示中的上下文数据
     */
    public AutoAdaptorProxy(RecyclerView recyclerView, @LayoutRes int res, int spanCount , ArrayList<T> data, Context context) {
        if (spanCount<=0)
            return;
        if (context ==null)
            return;
        if (recyclerView==null)
            return;
        if (data==null)
            return;
        this.adaptor = new AutoObjectAdaptor(recyclerView,res,spanCount, (ArrayList<Object>) data,context);
        recyclerView.setAdapter(adaptor);

        adaptor.setListener(new AutoObjectAdaptor.IItemListener() {
            @Override
            public void onItemClick(AutoObjectAdaptor.ViewHolder holder, int position, Object itemData) {
                listener.onItemClick(holder,position, ((T) itemData));
            }

            @Override
            public void onItemViewDraw(AutoObjectAdaptor.ViewHolder holder, int position, Object itemData) {
                listener.onItemViewDraw(holder,position, ((T) itemData));
            }
        });
    }

    // setter && getter
    public void setListener(IItemListener<T> listener) {
        this.listener = listener;
    }

    /**
     * 强制刷新RV的界面
     */
    public void notifyDataSetChanged() {
        adaptor.notifyDataSetChanged();
    }

    /**
     * 对外监听类
     */
    public interface IItemListener<T>{
        // 当单个对象被点击时
        void onItemClick(AutoObjectAdaptor.ViewHolder holder, int position, T  itemData);
        // 当单个对象开始绘制时
        void onItemViewDraw(AutoObjectAdaptor.ViewHolder holder, int position, T  itemData);
    }


}
