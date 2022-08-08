package com.aries.template.xiaoyu.xinlin;

import com.ainemo.util.JsonUtil;
import com.aries.template.xiaoyu.model.EndPoint;
import com.aries.template.xiaoyu.model.RegEndPoint;
import com.aries.template.xiaoyu.model.RegRequest;
import com.aries.template.xiaoyu.model.RtcStartInvokeRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 信令 发送消息的标准
 */
public class XLSend {
    /**
     * 向客户端发送登录信息
     * @return 将数据转称 json 字符串，发送给 socket
     */
    public String getLoginMsg(String xlPatientUserId){
        RegEndPoint regEndPoint = new RegEndPoint();
        regEndPoint.setUserId(xlPatientUserId);
        regEndPoint.setRoleId("patient");
        regEndPoint.setAppVersion("4.1.1");
        regEndPoint.setIsInVideo(0);
        regEndPoint.setAppType(4);

        RegRequest regRequest = new RegRequest();
        regRequest.setTopic("REG");
        regRequest.setEndPoint(regEndPoint);
        String registerMsg = JsonUtil.toJson(regRequest);
        return registerMsg;
    }

    /**
     * 向doctor发送信息
     * @return 将数据转称 json 字符串，发送给 socket
     */
    public String getDoctorMsg(String xlPatientUserId,
                               String xlPatientName,
                               String doctorUserId,
                               long consultId,
                               String meetingRoomNumber){
        //{"topic":"TX_RTC_START_INVOKE","endPoint":{"patientUserId":"62933bcbcf8912669abf4b98","doctorUserId":"5f339ceb9cd0500a923af577","patientName":"胡江","remark":"未知","orderId":815463559,"roomId":"910007727377","thirdAppVideoConsult":"xyLink","requestMode":"4"}}

        RtcStartInvokeRequest request = new RtcStartInvokeRequest();
        request.setTopic("TX_RTC_START_INVOKE");
        // map 的名称 必须是 endPoint
        EndPoint endPoint = new EndPoint();
        endPoint.setPatientUserId(xlPatientUserId);
        endPoint.setPatientName(xlPatientName);
        endPoint.setDoctorUserId(doctorUserId);
        endPoint.setOrderId(consultId);
        endPoint.setRemark("未知");
        endPoint.setRoomId(meetingRoomNumber);
        endPoint.setThirdAppVideoConsult("xyLink");
        endPoint.setRequestMode("4");

        request.setEndPoint(endPoint);
        String cmd = JsonUtil.toJson(request);
        return cmd;
    }

    /**
     * 病人离开会议室
     */
    public String getPatientLeaveMsg(String doctorUserId, String patientUserId){
        // 数据结构
//      if (socket != null) {
//        let msgObj = {
//                topic: `TX_RTC_SHUTDOWN`,
//        endPoint: {
//            doctorUserId: doctor.loginId,
//                    patientUserId: user.userId,
//                    role: 'patient',
//                    thirdAppVideoConsult: 'xyLink',
//        },
//                };
//        socket.send(JSON.stringify(msgObj));
//    }
        // map 的名称 必须是 endPoint
        Map<String, Object> endPoint = new HashMap<>();
        endPoint.put("doctorUserId",doctorUserId);
        endPoint.put("patientUserId",patientUserId);
        endPoint.put("role","patient");
        endPoint.put("thirdAppVideoConsult","xyLink");

        Map<String, Object> map = new HashMap<>();
        map.put("topic","TX_RTC_SHUTDOWN");
        map.put("endPoint",endPoint);

        String cmd = JsonUtil.toJson(map);
        return cmd;
    }
}
