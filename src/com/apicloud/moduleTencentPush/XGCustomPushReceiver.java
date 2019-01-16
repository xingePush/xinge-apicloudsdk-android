package com.apicloud.moduleTencentPush;

import android.content.Context;
import android.util.Log;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

/**
 * 本类主要用于接收消息和处理结果反馈<br>
 * APP可以参考本类，实现自己的Receiver<br>
 * 
 * 常见的错误码：<br>
 * 0：表示成功<br>
 * 1：系统错误，指针非法，内存错误等 <br>
 * 2：非法参数<br>
 * 其它：内部错误<br>
 * 
 * 
 * Copyright (c) 1998-2014 Tencent
 * 
 */
public class XGCustomPushReceiver extends XGPushBaseReceiver {

	private static final String TAG = XGCustomPushReceiver.class
			.getSimpleName();

	/**
	 * 注册结果
	 * 
	 * @param context
	 *            APP上下文对象
	 * @param errorCode
	 *            错误码，{@link XGPushBaseReceiver#SUCCESS}表示成功，其它表示失败
	 * @param registerMessage
	 *            注册结果返回
	 */
	@Override
	public void onRegisterResult(Context context, int errorCode,
			XGPushRegisterResult registerMessage) {
		if (context == null || registerMessage == null) {
			return;
		}
		Log.i(TAG, "onRegisterResult, errorCode:" + errorCode
				+ ",registerMessage:" + registerMessage);
	}

	/**
	 * 反注册结果
	 * 
	 * @param context
	 *            APP上下文对象
	 * @param errorCode
	 *            错误码，{@link XGPushBaseReceiver#SUCCESS}表示成功，其它表示失败
	 */
	@Override
	public void onUnregisterResult(Context context, int errorCode) {
		if (context == null) {
			return;
		}
		Log.i(TAG, "onUnregisterResult, errorCode:" + errorCode);
	}

	/**
	 * 设置标签操作结果
	 * 
	 * @param context
	 *            APP上下文对象
	 * @param errorCode
	 *            错误码，{@link XGPushBaseReceiver#SUCCESS}表示成功，其它表示失败
	 * @tagName 标签名称
	 */
	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
		if (context == null || tagName == null) {
			return;
		}
		Log.i(TAG, "onSetTagResult, errorCode:" + errorCode + ",tagName:"
				+ tagName);
		XGCallbackListeners.getInstance().onSetTagResult(errorCode, tagName);
	}

	/**
	 * 删除标签操作结果
	 * 
	 * @param context
	 *            APP上下文对象
	 * @param errorCode
	 *            错误码，{@link XGPushBaseReceiver#SUCCESS}表示成功，其它表示失败
	 * @tagName 标签名称
	 */
	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
		if (context == null || tagName == null) {
			return;
		}
		Log.i(TAG, "onDeleteTagResult, errorCode:" + errorCode + ",tagName:"
				+ tagName);
		XGCallbackListeners.getInstance().onDeleteTagResult(errorCode, tagName);
	}

	/**
	 * 收到消息<br>
	 * 
	 * @param context
	 *            APP上下文对象
	 * @param message
	 *            收到的消息
	 */
	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		if (context == null || message == null) {
			return;
		}
		Log.i(TAG, "onTextMessage, message:" + message);
		XGCallbackListeners.getInstance().onTextMessage(message);
	}

	/**
	 * 通知被打开结果反馈
	 * 
	 * @param context
	 *            APP上下文对象
	 * @param message
	 *            被打开的消息对象
	 */
	@Override
	public void onNotifactionClickedResult(Context context,
			XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
		Log.i(TAG, "onNotifactionClickedResult, message:" + message);
		XGCallbackListeners.getInstance().onNotifactionClickedResult(message);
	}

	/**
	 * 通知被展示的回调
	 */
	@Override
	public void onNotifactionShowedResult(Context context,
			XGPushShowedResult notifiShowedRlt) {
		if (context == null || notifiShowedRlt == null) {
			return;
		}
		Log.i(TAG, "onNotifactionShowedResult, message:" + notifiShowedRlt);
		XGCallbackListeners.getInstance().onNotifactionShowedResult(
				notifiShowedRlt);
	}
}
