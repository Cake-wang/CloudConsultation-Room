package com.aries.template.xiaoyu.dapinsocket;


import java.util.ArrayList;
import java.util.List;

public class SessionContext {
	private static SessionContext mContext;

//	private mSessionListener;
//	public abstract class OnContextChangedListener {
//		
//	}
	/**
	 * 测试模式标记
	 */
	public boolean isTest = false;

	private List<OnContextChangedListener> mListeners;
	
	public static SessionContext getSessiontContext() {
		if (mContext == null) {
			mContext = new SessionContext();
		}
		
		return mContext;
	}

	public static void logout() {
		mContext = null;
	}

	
	public SessionContext() {
		mListeners = new ArrayList<OnContextChangedListener>();
	}
	

	
	public void addOnContextChangedListener(OnContextChangedListener listener) {
		if (!mListeners.contains(listener)) {
			mListeners.add(listener);
		}
	}
	
	public void removeOnContextChangedListener(OnContextChangedListener listener) {
		if (mListeners.contains(listener)) {
			mListeners.remove(listener);
		}
	}

	/**
	 * 是否有监听
	 */
	public boolean isHaveContextChangedListener(){
		boolean back = mListeners!=null && mListeners.size()>0;
		return back;
	}

	/**
	 * 释放所有监听
	 */
	public void removeAll(){
		mListeners = new ArrayList<OnContextChangedListener>();
	}


	/**
	 * 发送数据
	 * @param sendData
	 */
	public void onSendData(byte[] sendData) {
		for (int i = 0; i < mListeners.size(); i++) {
			mListeners.get(i).onSendData(sendData);
		}
	}

	/**
	 * 发送数据
	 * @param sendData
	 */
	public void onSendData(String sendData) {
		for (int i = 0; i < mListeners.size(); i++) {
			mListeners.get(i).onSendData(sendData);
		}
	}

	/**
	 * 接收数据
	 * @param receiveData
	 */
	public void onReceiveData(byte[] receiveData) {
		for (int i = 0; i < mListeners.size(); i++) {
			mListeners.get(i).onReceiveData(receiveData);
		}
	}

	/**
	 * 接收数据
	 * @param receiveData
	 */
	public void onReceiveData(String receiveData) {
		for (int i = 0; i < mListeners.size(); i++) {
			mListeners.get(i).onReceiveData(receiveData);
		}
	}


}
