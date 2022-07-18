package com.aries.template.xiaoyu.dapinsocket;



public abstract class OnContextChangedListener {

	/**
	 * socket发送
	 * @param sendData
	 */
	public void onSendData(byte[] sendData){}

	/**
	 * 发送数据
	 * @param sendData
	 */
	public void onSendData(String sendData){}

	/**
	 * 接收数据
	 * @param receiveData
	 */
	public void onReceiveData(byte[] receiveData){}

	/**
	 * 接收数据
	 * @param receiveData
	 */
	public void onReceiveData(String receiveData){}


}