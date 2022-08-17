package com.aries.template.xiaoyu.model;

//{"topic":"TX_RTC_SHUTDOWN_RES","payload":{"role":"patient","doctorUserId":"627dcfd9cc2f204b0217f3a7","thirdAppVideoConsult":"xyLink","patientUserId":"62aa8f36cc2f205c7eaa4536"}}
public class RegReponse {
    private String topic;
    private Integer payload;

    @Override
    public String toString() {
        return "RegReponse{" +
                "topic='" + topic + '\'' +
                ", payload=" + payload +
                '}';
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getPayload() {
        return payload;
    }

    public void setPayload(Integer payload) {
        this.payload = payload;
    }
}
