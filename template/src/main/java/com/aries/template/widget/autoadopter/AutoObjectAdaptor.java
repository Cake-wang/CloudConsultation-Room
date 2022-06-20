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

/******
 *
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/17 11:18 AM
 *
 */
public class AutoObjectAdaptor extends RecyclerView.Adapter<AutoObjectAdaptor.ViewHolder> {

    /** 创建一个OBJECT太麻烦了，直接用 MAP 吧 */
    public ArrayList<Object> data;
    /** 资源 layout id */
    public int res;
    /** item 监听对象 */
    public IItemListener listener;

    public AutoObjectAdaptor(RecyclerView recyclerView, @LayoutRes int res, int spanCount , ArrayList<Object> data, Context context) {
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

    @Override
    public int getItemCount() {
        return data.size();
    }

    // setter && getter
    public void setListener(IItemListener listener) {
        this.listener = listener;
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
        void onItemClick(ViewHolder holder, int position, Object  itemData);
        // 当单个对象开始绘制时
        void onItemViewDraw(ViewHolder holder, int position, Object  itemData);
    }

}
