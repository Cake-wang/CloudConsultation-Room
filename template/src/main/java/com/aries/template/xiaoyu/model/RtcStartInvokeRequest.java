/**
  * Copyright 2022 bejson.com 
  */
package com.aries.template.xiaoyu.model;

/**
 * Auto-generated: 2022-06-27 0:46:47
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class RtcStartInvokeRequest {

    private String topic;
    private RtcStartInvokeEndPoint rtcStartInvokeEndPoint;
    public void setTopic(String topic) {
         this.topic = topic;
     }
     public String getTopic() {
         return topic;
     }

    public void setEndPoint(RtcStartInvokeEndPoint rtcStartInvokeEndPoint) {
         this.rtcStartInvokeEndPoint = rtcStartInvokeEndPoint;
     }
     public RtcStartInvokeEndPoint getEndPoint() {
         return rtcStartInvokeEndPoint;
     }

}