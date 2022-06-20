package com.aries.template.widget.autoadopter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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
 *  因为这个类的数据处理太复杂，所以启用新的高级处理范型 AutoAdaptorProxy 来处理自动RV代理
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/10 11:04 AM
 *
 */
@Deprecated
public class AutoAdaptor extends RecyclerView.Adapter<AutoAdaptor.ViewHolder>{

    /** 创建一个OBJECT太麻烦了，直接用 MAP 吧 */
    public ArrayList<Map> data;
    /** 资源 layout id */
    public int res;
    /** item 监听对象 */
    private IItemListener listener;

    /**
     * 将 adaptor 和 RV 链接在一起
     * @param recyclerView 代理对象
     * @param res 代理对象要显示的最小单位 显示界面
     * @param spanCount 显示多少列
     * @param data 显示注入的数据
     * @param context recyclerView显示中的上下文数据
     */
    public AutoAdaptor(RecyclerView recyclerView, @LayoutRes int res,int spanCount , ArrayList<Map> data, Context context) {
        if (spanCount<=0)
            return;
        if (context ==null)
            return;
        if (recyclerView==null)
            return;
        if (data==null)
            return;

        this.res = res;
        this.data = data;

        GridLayoutManager manager = new GridLayoutManager(context,spanCount){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(this);
    }

    /**
     * viewHolder 构造器
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setIsRecyclable(false);
        }
    }

    /**
     * 对外监听类
     */
    public interface IItemListener{
        // 当单个对象被点击时
        void onItemClick(ViewHolder holder, int position, Map  itemData);
        // 当单个对象开始绘制时
        void onItemViewDraw(ViewHolder holder, int position, Map  itemData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(res,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        listener.onItemViewDraw(holder,position,data.get(position));
        holder.itemView.setOnClickListener(v->listener.onItemClick(holder,position,data.get(position)));
    }


    /**
     * 返回数据
     */
    @Override
    public int getItemCount() {
        return data.size();
    }


    // setter && getter
    public void setListener(IItemListener listener) {
        this.listener = listener;
    }


}
