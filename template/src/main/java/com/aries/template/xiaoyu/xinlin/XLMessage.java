package com.aries.template.xiaoyu.xinlin;

import com.ainemo.util.JsonUtil;
import com.aries.template.xiaoyu.model.RtcStartInvokeEndPoint;
import com.aries.template.xiaoyu.model.RtcStartInvokeRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 信令
 * 消息发送填装器
 */
public class XLMessage {

    /**
     * 向doctor发送信息
     * @return 将数据转称 json 字符串，发送给 socket
     */
    public String sendDoctorMsg(String xlPatientUserId,
                                String xlPatientName,
                                String doctorUserId,
                                long consultId,
                                String meetingRoomNumber){
        //{"topic":"TX_RTC_START_INVOKE","endPoint":{"patientUserId":"62933bcbcf8912669abf4b98","doctorUserId":"5f339ceb9cd0500a923af577","patientName":"胡江","remark":"未知","orderId":815463559,"roomId":"910007727377","thirdAppVideoConsult":"xyLink","requestMode":"4"}}
        RtcStartInvokeRequest request = new RtcStartInvokeRequest();
        request.setTopic("TX_RTC_START_INVOKE");
        RtcStartInvokeEndPoint endPoint = new RtcStartInvokeEndPoint();
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
    public String sendPatientLeave(String doctorUserId, String patientUserId){

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
        Map<String, Object> endPoint = new HashMap<>();
        endPoint.put("doctorUserId",doctorUserId);
        endPoint.put("patientUserId",patientUserId);
        endPoint.put("role","patient");
        endPoint.put("thirdAppVideoConsult","xyLink");

        Map<String, Object> map = new HashMap<>();
        map.put("topic","TX_RTC_SHUTDOWN");
        map.put("endPoint",endPoint);

        String cmd = JsonUtil.toJson(endPoint);
        return cmd;
    }


}
