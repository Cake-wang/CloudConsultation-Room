package com.aries.template.widget.updownbtn;

import com.aries.template.utils.DefenceUtil;

/******
 * 防止重复提交按钮
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/24 10:36 AM
 *
 */
public class DefenceUpDownProxy<T> extends UpDownProxy<T>{

    @Override
    public void doNextReFlash() {
        if (!DefenceUtil.checkReSubmit("DefenceUpDownProxy.doNextReFlash"))
            return;
        super.doNextReFlash();
    }

    @Override
    public void doProReFlash() {
        if (!DefenceUtil.checkReSubmit("DefenceUpDownProxy.doProReFlash"))
            return;
        super.doProReFlash();
    }

    @Override
    public void doStartReFlash() {
        if (!DefenceUtil.checkReSubmit("DefenceUpDownProxy.doStartReFlash"))
            return;
        super.doStartReFlash();
    }
}
