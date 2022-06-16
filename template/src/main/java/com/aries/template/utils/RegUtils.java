package com.aries.template.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/******
 * 正则表达式工具
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/16 10:39 AM
 *
 */
public class RegUtils {
    /**
     * 验证手机号是否正确
     */
    public static boolean isMobileNO(String mobiles) {
        String PHONE = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$";
        Pattern p = Pattern.compile(PHONE);
//                .compile("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6])|(17[0,1,3,5-8])|(18[0-9])|(19[8,9]))\\\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


    /**
     * 检测验证码是否为4位
     */
    public static boolean isVerifyCode(String verifyCode) {
        Pattern p = Pattern
                .compile("^\\d{4}$");
        Matcher m = p.matcher(verifyCode);
        return m.matches();
    }
}
