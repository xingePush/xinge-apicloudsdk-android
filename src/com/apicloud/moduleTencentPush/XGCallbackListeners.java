package com.apicloud.moduleTencentPush;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

public class XGCallbackListeners {
	public static final String TAG_ON_TEXT_MESSAGE = "message";
	public static final String TAG_ON_NOTIFACTION_SHOW = "notifactionShow";
	public static final String TAG_ON_NOTIFACTION_CLICK = "notifactionClick";
	public static final String TAG_ON_NOTIFACTION_CLEAR = "notifactionClear";
	public static final String TAG_ON_SET_TAG = "setTag";
	public static final String TAG_ON_DEL_TAG = "delTag";
	private volatile static XGCallbackListeners instance = null;
	private Map<String, UZModuleContext> mapListerners = new HashMap<String, UZModuleContext>(
			6);

	private XGCallbackListeners() {
	}

	public static XGCallbackListeners getInstance() {
		if (instance == null) {
			synchronized (XGCallbackListeners.class) {
				if (instance == null) {
					instance = new XGCallbackListeners();
				}

			}
		}
		return instance;
	}

	public void onTextMessage(XGPushTextMessage textMessage) {
		try {
			UZModuleContext cb = mapListerners.get(TAG_ON_TEXT_MESSAGE);
			if (cb != null) {
				JSONObject result = new JSONObject();
				result.put("status", true);
				safeJsonPut(result, "title", textMessage.getTitle());
				safeJsonPut(result, "content", textMessage.getContent());
				safeJsonPut(result, "customContent",
						textMessage.getCustomContent());
				cb.success(result, false);
			}
		} catch (Throwable e) {
		}
	}

	public void onSetTagResult(int errorCode, String tagName) {
		try {
			UZModuleContext cb = mapListerners.get(TAG_ON_SET_TAG);
			if (cb != null) {
				JSONObject result = new JSONObject();
				result.put("status", errorCode == XGPushBaseReceiver.SUCCESS);
				result.put("code", errorCode);
				result.put("tag", tagName);
				cb.success(result, true);
			}
		} catch (Throwable e) {
		}

	}

	public void onDeleteTagResult(int errorCode, String tagName) {
		try {
			UZModuleContext cb = mapListerners.get(TAG_ON_DEL_TAG);
			if (cb != null) {
				JSONObject result = new JSONObject();
				result.put("status", errorCode == XGPushBaseReceiver.SUCCESS);
				result.put("code", errorCode);
				result.put("tag", tagName);
				cb.success(result, true);
			}
		} catch (Throwable e) {
		}

	}

	public void onNotifactionClickedResult(XGPushClickedResult message) {
		try {
			UZModuleContext cb = null;
			if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
				cb = mapListerners.get(TAG_ON_NOTIFACTION_CLICK);
			} else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
				cb = mapListerners.get(TAG_ON_NOTIFACTION_CLEAR);
			} else {
				return;
			}
			if (cb != null) {
				JSONObject result = new JSONObject();
				result.put("status", true);
				safeJsonPut(result, "title", message.getTitle());
				safeJsonPut(result, "content", message.getContent());
				safeJsonPut(result, "customContent", message.getCustomContent());
				safeJsonPut(result, "activity", message.getActivityName());
				result.put("actionType", message.getNotificationActionType());
				result.put("msgid", message.getMsgId());
				cb.success(result, false);
			}
		} catch (Throwable e) {
		}

	}

	public void onNotifactionShowedResult(XGPushShowedResult message) {
		try {
			UZModuleContext cb = mapListerners.get(TAG_ON_NOTIFACTION_SHOW);
			if (cb != null) {
				JSONObject result = new JSONObject();
				result.put("status", true);
				safeJsonPut(result, "title", message.getTitle());
				safeJsonPut(result, "content", message.getContent());
				safeJsonPut(result, "customContent", message.getCustomContent());
				safeJsonPut(result, "activity", message.getActivity());
				result.put("actionType", message.getNotificationActionType());
				result.put("msgid", message.getMsgId());
				cb.success(result, false);
			}
		} catch (Throwable e) {
		}

	}

	private void safeJsonPut(JSONObject result, String key, String value)
			throws JSONException {
		if (result != null && XGPush.isStringValid(key) && value != null) {
			result.put(key, value);
		}
	}
	
	public void put(String key, UZModuleContext cb){
		mapListerners.put(key, cb);
	}

	public void clear() {
		mapListerners.clear();
	}
	
	public void remove(String key){
		mapListerners.remove(key);
	}

}
