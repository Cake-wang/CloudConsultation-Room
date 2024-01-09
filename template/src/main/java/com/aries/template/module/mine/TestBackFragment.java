package com.aries.template.module.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.aries.library.fast.util.SPUtil;
import com.aries.template.GlobalConfig;
import com.aries.template.R;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.template.utils.RegUtils;
import com.aries.ui.view.title.TitleBarView;

import androidx.annotation.Nullable;
import butterknife.BindView;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * 显示结果页
 * 根据传入的信息，变化各种文字和图片信息的结果页
 *
 * 注意，打印不能使用 换行符 \n。 可能出现无限打印的情况, 必须使用 \r\n
 * @author louisluo
 * @Author: AriesHoo on 2018/7/13 17:09
 * @E-Mail: AriesHoo@126.com
 * @Function: 我的
 * @Description:
 */
public class TestBackFragment extends BaseEventFragment implements ISupportFragment {

    @BindView(R.id.btn_inquiry)
    Button btn_inquiry;
    @BindView(R.id.jtjk_recipe_name)
    TextView jtjk_recipe_name;

    public static TestBackFragment newInstance() {
        Bundle args = new Bundle();
        TestBackFragment fragment = new TestBackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getContentLayout() {
        if(GlobalConfig.thirdFactory.equals("3")){
            return R.layout.fragment_test_back;
        }else {
            return R.layout.fragment_test_back;
        }
//        return R.layout.fragment_result;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setBgColor(Color.WHITE)
                .setTitleMainText(R.string.mine);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        // 显示名称
        jtjk_recipe_name.setText(RegUtils.nameDesensitization(GlobalConfig.ssCard.getName())+"，您好");

        btn_inquiry.setOnClickListener(v -> {

            SPUtil.put(mContext,"tag","fzpy");
            start(MineCardFragment.newInstance("fzpy"));

        });

    }





    @Override
    public void loadData() {


    }




}
