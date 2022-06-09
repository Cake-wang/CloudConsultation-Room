package com.aries.template;

/******
 * 全世界存储对象
 * @author  ::: louis luo
 * Date ::: 2022/6/9 10:31 AM
 *
 */
public class GlobalConfig {

    /**
     *  全世界网络通信凭据
     */
    public static String token = "";

    /**
     * 用户个人信息
     * 姓名
     */
    public static  String name = "";

    /**
     * 清空所有缓存数据
     * 比如使用在返回主页中
     */
    public static void clear(){
        token="";
        name="";
    }

}
