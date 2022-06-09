package com.aries.template.module.base;

import android.view.View;
import android.widget.Button;

import com.aries.template.R;
import com.aries.template.module.main.HomeFragment;
import com.xuexiang.xaop.annotation.SingleClick;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;

/******
 * 按钮，行为的基础性事件
 * 必须在每一个页面中，声明对应的id，不然会报错。
 * - 首页返回事件
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/9 2:40 PM
 *
 */
public abstract class BaseEventFragment extends BaseTimerFragment{
    /**
     * 监听按钮
     * @param view
     */
    @BindView(R.id.btn_back)
    Button btn_back;
    @BindView(R.id.btn_main)
    Button btn_main;

    @SingleClick
    @OnClick({R.id.btn_back, R.id.btn_main})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                pop();// 返回上一页
                break;
            case R.id.btn_main:
                gotoMain(); // 返回主页
                break;
            default:
                // 不做任何动作
                break;
        }
    }

    /**
     * 返回主页
     */
    public void gotoMain(){
        // 必须使用 singleTask 将所有Fragemnt的栈干掉
        start(HomeFragment.newInstance(), SupportFragment.SINGLETASK);
    }
}
