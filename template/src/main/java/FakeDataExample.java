import com.aries.template.constant.ApiConstant;
import com.aries.template.retrofit.repository.ApiRepository;
import com.aries.template.utility.ConvertJavaBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/******
 * 自动化测试用的假数据填充器
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/10 9:57 AM
 *
 */
public class FakeDataExample {


    /**
     * 网络专用假数据填充
     */
    protected RequestBody BodyCreate(){
        Map<String,String> bizContent = new HashMap<>();
        bizContent.put("appKey", ApiConstant.NALI_APPKEY);
        bizContent.put("tid",ApiConstant.NALI_TID);
        bizContent.put("organId","1");
        ArrayList<Map> maps = new ArrayList<>();
        maps.add(bizContent);
        // 创建body
        ApiRepository.common.getInstance().machineId = "SY0001";
        ApiRepository.common.getInstance().userId = "2fcd34d6dde742098737b10ff0fddd9a";
        final String  logTraceId = "eebcbbcf2c664c28a671e980265c6c76";//getUUID();

        final Map<String, Object> params = new HashMap<>(4);
        params.put("logTraceId", logTraceId);
        params.put("methodCode","findValidOrganProfessionForRevisit");
        params.put("common", ApiRepository.common.getInstance());
        params.put("bizContent", maps);
        String strEntity = ConvertJavaBean.converJavaBeanToJsonNew(params);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("Content-Type:application/json;charset=UTF-8"),strEntity);
        return body;
    }
}
