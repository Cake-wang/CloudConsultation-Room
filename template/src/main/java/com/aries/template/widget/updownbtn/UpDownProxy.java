package com.aries.template.widget.updownbtn;

import androidx.annotation.IntRange;

import java.util.ArrayList;

/******
 * 上一页，下一页，刷新 的代理工具
 * 使用方法
 * upDownProxy = new UpDownProxy<>();
 * upDownProxy.setOnEventListener 设置事件处理
 * upDownProxy.setParamMaxNumber(9); 一页最大的显示个数
 * upDownProxy.setTotalDatas(totalDatas); 输入完整的全部数据
 * upDownProxy.doStartReFlash(); 执行第一页开始业务
 * upDownProxy.doNextReFlash(); 执行下一页
 * upDownProxy.doProReFlash(); 执行上一页
 * T 当前处理数据的类型
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/15 11:07 AM
 *
 */
public class UpDownProxy<T> {

    /** 表示当前是上一页返回 */
    public static final int CASE_RV_UP= 0x01;
    /** 表示当前是下一页返回 */
    public static final int CASE_RV_DOWN = 0x02;

    /**  网格数据显示的最大个数 */
    private int param_max_number = 9;

    /** 当前1级机构的page位置 */
    private int currentPageNum = 0;

    /**
     * 可以点击上一页
     */
    private boolean upBtnEnable = true;

    /**
     * 可以点击下一页
     */
    private boolean downBtnEnable = true;

    /** 网络获取的全一级科室数据 */
    private ArrayList<T> totalDatas;

    /** 事件监听器*/
    private EventListener<T> listener;

    /** 事件监听器*/
    public void setOnEventListener(EventListener<T> listener) {
        this.listener = listener;
    }

    /**
     * 创建一个上一页，下一页的管理器
     * @param totalDatas 管理的数据元
     * @param MaxNumber 管理的包含数量
     */
    public UpDownProxy(ArrayList<T> totalDatas, int MaxNumber) {
        this.totalDatas = totalDatas;
        this.param_max_number = MaxNumber;
    }

    public UpDownProxy(){
        this.totalDatas = new ArrayList<>();
        this.param_max_number = 9;
    }

    /**
     * 上一页数据显示
     * 返回数据深度复制
     */
    public void doProReFlash(){
        int startIndex = (currentPageNum -1)* param_max_number;
        if (startIndex<=0){
            startIndex = 0;
            // 上一页 背景设置，可点击设置
            setBtnEnable(CASE_RV_UP,false);
        }
        ArrayList<T> newDatas = new ArrayList<>();
        for (int i = 0; i < param_max_number; i++) {
            if (startIndex+i>totalDatas.size()){
                // 下一页 不可用
                setBtnEnable(CASE_RV_DOWN,false);
                break; // out of boundary
            }
            newDatas.add(totalDatas.get(startIndex+i));
        }
        if (listener!=null)
            listener.reFlashRV(newDatas);
        currentPageNum--;
        // 如果是上一页功能就设置下一页按钮
        if (!downBtnEnable){
            if (startIndex+ param_max_number <=totalDatas.size()){
                setBtnEnable(CASE_RV_DOWN,true);
            }
        }else{
            if (startIndex+ param_max_number >totalDatas.size()){
                setBtnEnable(CASE_RV_DOWN,false);
            }
        }
    }

    /**
     * 下一页数据显示
     * 返回数据深度复制
     */
    public void doNextReFlash(){
        int startIndex = (currentPageNum+1)* param_max_number;
        if (startIndex<=0){
            startIndex = 0;
            // 上一页 背景设置，可点击设置
            setBtnEnable(CASE_RV_UP,false);
        }
        ArrayList<T> newDatas = new ArrayList<>();
        for (int i = 0; i < param_max_number; i++) {
            if (startIndex+i>=totalDatas.size()){
                // 下一页 不可用
                setBtnEnable(CASE_RV_DOWN,false);
                break; // out of boundary
            }
            newDatas.add(totalDatas.get(startIndex+i));
        }
        if (listener!=null)
            listener.reFlashRV(newDatas);
        currentPageNum++;
        // 如果是下一页功能则设置上一页按钮
        if (!upBtnEnable){
            if (startIndex- param_max_number >=0){
                setBtnEnable(CASE_RV_UP,true);
            }
        }else{
            if (startIndex- param_max_number <0){
                setBtnEnable(CASE_RV_UP,false);
            }
        }
    }

    /**
     * 从0开始显示
     * 重置数据，重置上一页下一页按钮
     * 返回数据深度复制
     */
    public void doStartReFlash(){
        int startIndex = 0;
        ArrayList<T> newDatas = new ArrayList<>();
        for (int i = 0; i < param_max_number; i++) {
            if (startIndex+i>=totalDatas.size()){
                break; // out of boundary
            }
            newDatas.add(totalDatas.get(startIndex+i));
        }
        if (listener!=null)
            listener.reFlashRV(newDatas);
        currentPageNum =0;

        // 上一页肯定不能用
        setBtnEnable(CASE_RV_UP,false);

        // 下一页先关闭，防止有些界面可能打开了下一页
        setBtnEnable(CASE_RV_DOWN,false);

        // 判定下一页可以不可以用
        if (startIndex+ param_max_number <totalDatas.size()){
            setBtnEnable(CASE_RV_DOWN,true);
        }
    }

    /**
     * 判断结束了，开始设置上一页和下一页的结果。
     * 将一个button设置可用和不可用,并调整样式
     */
    public void setBtnEnable(int nowCase, boolean b){
        if (nowCase==CASE_RV_UP)
            upBtnEnable=b;
        if (nowCase==CASE_RV_DOWN)
            downBtnEnable=b;
        if (listener!=null)
            listener.setBtnEnable(nowCase,b);
    }

    /**
     * 对外部的监听器
     */
    public interface EventListener<T>{
        /**
         * 当前页面刷新执行，可以写一些界面更新
         * @param newDatas 返回的数据，包含最大 param_max_number 个数据的数据列
         */
        void reFlashRV(ArrayList<T> newDatas);
        /**
         * 当前是哪个按钮的样式需要更换
         * @param nowCase CASE_RV_UP 或 CASE_RV_DOWN
         * @param b 当前按钮enable的boolean判断
         */
        void setBtnEnable(@IntRange(from = 1,to = 2) int nowCase, boolean b);
    }

    /**
     * 输入最大个数
     * @param param_max_number 最大个数
     */
    public void setParamMaxNumber(int param_max_number) {
        this.param_max_number = param_max_number;
    }

    /**
     * 输入数据源
     * @param totalDatas 数据源
     */
    public void setTotalDatas(ArrayList<T> totalDatas) {
        this.totalDatas = totalDatas;
    }
}
