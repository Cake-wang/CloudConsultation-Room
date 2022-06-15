package com.aries.template.retrofit.repository;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Map;

/******
 * 网络错误统一处理器
 * 将数据放进网络统一处理器后，会得到所有错误结果
 * todo 返回错误的参数，每一种参数名称都不一样，要怎么判定？
 *
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/15 3:23 PM
 *
 */
public class ErrorProxy {

    /**
     * 验证输入的数据是否有错误
     * @param context 用于创建提示
     * @param msg 错误数据源，ta应该是一个JSONOBJECT 转换的String对象
     * @param args 我关注的错误信息，他会以MAP的形式return回来
     * @return 将我关注的数据返回回来
     */
    public Map<String, Object> doErrorCheck(Context context,int code, String msg, String[] args){
        String toastTip = "复诊单异常"; //默认提示
        String cid = "0";// 默认是0
        try {
            switch (code){
                case 611:
                    JSONObject json611 = new JSONObject(msg);
                    cid = json611.get("consultId").toString();
                    toastTip = json611.get("title").toString();
                    break;
                case 613:
                    JSONObject json613 = new JSONObject(msg);
                    cid = json613.get("cid").toString();
                    toastTip ="有一条未结束的非团队复诊单";
                    break;
                case 614:
                    JSONObject json614 = new JSONObject(msg);
                    cid = json614.get("cid").toString();
                    toastTip ="有一条未结束的团队复诊单";
                    break;
                case 608:
                    cid = "0";
                    toastTip =msg;
                    break;
            }
            Toast.makeText(context, toastTip, Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
