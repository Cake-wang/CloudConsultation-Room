package com.aries.template.xiaoyu.dapinsocket;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * socket交互的数据格式
 * @author vV
 */
@JSONType(ignores={"sendDataByte"})
public class DataBean {
    /**
     * 1：字符串，2：文件，3：心跳
     */
    int dataType = 1;
    /**
     * 见 MessageType
     */
    int messageType;
    /**
     * body长度
     */
    int length;
    /**
     * 具体内容
     */
    String body = "";
    /**
     * 这个用来解析PC发过来的body，可能存在多个Bean
     */
    Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
        if(body != null && body.length()>0){
            length = body.getBytes().length;
        }
    }
    public byte[] getSendDataByte(){
        byte[] dataTypes = NumberUtil.tolh(dataType);
        byte[] messTypes = NumberUtil.tolh(messageType);
        byte[] lengths = NumberUtil.tolh(length);
        byte[] bodys = body.getBytes();
        byte[] concat = NumberUtil.concat(NumberUtil.concat(NumberUtil.concat(dataTypes, messTypes), lengths), bodys);
        return concat;
    }
}
