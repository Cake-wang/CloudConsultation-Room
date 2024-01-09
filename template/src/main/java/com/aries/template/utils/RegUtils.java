package com.aries.template.utils;

import android.text.TextUtils;

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
                .compile("^\\d{6}$");
        Matcher m = p.matcher(verifyCode);
        return m.matches();
    }

    /**
     * 姓脱敏
     * @param name
     * @return
     */
    public static String nameDesensitization(String name) {
        if(TextUtils.isEmpty(name)) {
            return name;
        }
        char[] sArr = name.toCharArray();
        if (sArr.length == 2) {
            return sArr[0]+"*";
        } else if (sArr.length  > 2) {
            for (int i = 1; i < sArr.length ; i++) {
                // if ('·' != sArr[i]) {
                sArr[i] = '*';
                // }
            }
            return new String(sArr);
        }
        return name;
    }

}
