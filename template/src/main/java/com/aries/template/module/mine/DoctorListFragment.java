package com.aries.template.module.mine;

import android.os.Bundle;

import com.aries.template.R;
import com.aries.template.module.base.BaseEventFragment;
import com.aries.ui.view.title.TitleBarView;

/******
 * 医生的显示列表
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/10 3:38 PM
 *
 */
public class DoctorListFragment extends BaseEventFragment {

    @Override
    public int getContentLayout() {
        return R.layout.fragment_doctor;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }
}
