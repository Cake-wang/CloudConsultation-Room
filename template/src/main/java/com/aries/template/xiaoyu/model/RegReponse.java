package com.aries.template.xiaoyu.model;

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