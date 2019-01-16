package com.apicloud.moduleTencentPush;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGLocalMessage;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.annotation.UzJavascriptMethod;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

public class XGPush extends UZModule {
	public static final String featureName = "tencentPush";
	public static final String androidAccessIdParaName = "android_accessId";
	public static final String androidAccessKeyParaName = "android_accessKey";

	private long accessId = 0L;
	private String accessKey = "";


	public XGPush(UZWebView webView) {
		super(webView);
	}

	private void initIdAndKey() {
		if (accessId <= 0) {
			String accidConfig = getFeatureValue(featureName,
					androidAccessIdParaName);
			String accKeyConfig = getFeatureValue(featureName,
					androidAccessKeyParaName);
			accessId = Long.valueOf(accidConfig);
			accessKey = accKeyConfig;
			XGPushConfig.setAccessId(getContext(), accessId);
			XGPushConfig.setAccessKey(getContext(), accessKey);
		}
	}

	public static boolean isStringValid(String str) {
		if (str == null || str.trim().length() == 0) {
			return false;
		}
		return true;
	}

	@UzJavascriptMethod
	public void jsmethod_config(final UZModuleContext moduleContext) {
		boolean debug = moduleContext.optBoolean("debug", false);
		XGPushConfig.enableDebug(moduleContext.getContext(), debug);
		initIdAndKey();
		if (accessId > 0 && isStringValid(accessKey)) {
			XGPushConfig.setAccessId(moduleContext.getContext(), accessId);
			XGPushConfig.setAccessKey(moduleContext.getContext(), accessKey);
			JSONObject result = new JSONObject();
			try {
				result.put("status", true);
				result.put("accessId", accessId);
				result.put("accessKey", accessKey);
				result.put("debug", debug);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			moduleContext.success(result, true);
		} else {
			JSONObject result = new JSONObject();
			JSONObject error = new JSONObject();
			try {
				result.put("status", false);
				error.put("code", -1);
				error.put("msg", "invalid accessid:" + accessId
						+ ", or accesskey:" + accessKey
						+ ", please config it on config.xml first");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			moduleContext.error(result, error, true);
		}
	}

	@UzJavascriptMethod
	public void jsmethod_setInstallChannel(final UZModuleContext moduleContext) {
		initIdAndKey();
		String channel = moduleContext.optString("channel", null);
		if (isStringValid(channel)) {
			XGPushConfig.setInstallChannel(moduleContext.getContext(), channel);
		}
	}

	@UzJavascriptMethod
	public void jsmethod_setGameServer(final UZModuleContext moduleContext) {
		initIdAndKey();
		String gameServer = moduleContext.optString("server", null);
		if (isStringValid(gameServer)) {
			XGPushConfig.setGameServer(moduleContext.getContext(), gameServer);
		}
	}

	@UzJavascriptMethod
	public void jsmethod_registerPush(final UZModuleContext moduleContext) {
		initIdAndKey();
		final String account = moduleContext.optString("account", null);
		XGIOperateCallback cbCallback = new XGIOperateCallback() {
			@Override
			public void onSuccess(Object token, int arg1) {
				JSONObject result = new JSONObject();
				try {
					result.put("status", true);
					result.put("token", token);
					if (isStringValid(account)) {
						result.put("account", account);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				moduleContext.success(result, true);
			}

			@Override
			public void onFail(Object arg0, int errCode, String msg) {
				JSONObject result = new JSONObject();
				JSONObject error = new JSONObject();
				try {
					result.put("status", false);
					error.put("code", errCode);
					error.put("msg", msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				moduleContext.error(result, error, true);
			}
		};

		if (!isStringValid(account)) {
			XGPushManager.registerPush(moduleContext.getContext(), cbCallback);
		} else {
			XGPushManager.registerPush(moduleContext.getContext(), account,
					cbCallback);
		}

	}

	@UzJavascriptMethod
	public void jsmethod_unregisterPush(final UZModuleContext moduleContext) {
		initIdAndKey();
		XGPushManager.unregisterPush(moduleContext.getContext(),
				new XGIOperateCallback() {
					@Override
					public void onSuccess(Object token, int arg1) {
						JSONObject result = new JSONObject();
						try {
							result.put("status", true);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						moduleContext.success(result, true);
					}

					@Override
					public void onFail(Object arg0, int errCode, String msg) {
						JSONObject result = new JSONObject();
						JSONObject error = new JSONObject();
						try {
							result.put("status", false);
							error.put("code", errCode);
							error.put("msg", msg);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						moduleContext.error(result, error, true);
					}
				});
	}

	@UzJavascriptMethod
	public void jsmethod_setTag(final UZModuleContext moduleContext) {
		initIdAndKey();
		String tagName = moduleContext.optString("tag");
		if (isStringValid(tagName)) {
			XGPushManager.setTag(moduleContext.getContext(), tagName);
			XGCallbackListeners.getInstance().put(
					XGCallbackListeners.TAG_ON_SET_TAG, moduleContext);
		} else {
			onCommonError(moduleContext);
		}
	}

	@UzJavascriptMethod
	public void jsmethod_delTag(final UZModuleContext moduleContext) {
		initIdAndKey();
		String tagName = moduleContext.optString("tag");
		if (isStringValid(tagName)) {
			XGPushManager.deleteTag(moduleContext.getContext(), tagName);
			XGCallbackListeners.getInstance().put(
					XGCallbackListeners.TAG_ON_DEL_TAG, moduleContext);
		} else {
			onCommonError(moduleContext);
		}
	}

	private void onCommonError(UZModuleContext moduleContext) {
		try {
			JSONObject result = new JSONObject();
			JSONObject error = new JSONObject();
			result.put("status", false);
			error.put("code", -1);
			error.put("msg", "requese param is invalid, null or empty.");
			moduleContext.error(result, error, true);
		} catch (JSONException e) {
		}

	}

	@UzJavascriptMethod
	public void jsmethod_addlocalNotification(
			final UZModuleContext moduleContext) {
		initIdAndKey();
		XGLocalMessage localMsg = new XGLocalMessage();
		String title = moduleContext.optString("title");
		String content = moduleContext.optString("content");
		if (!isStringValid(title) || !isStringValid(content)) {
			onCommonError(moduleContext);
			return;
		}
		String date = moduleContext.optString("date", "");
		String hour = moduleContext.optString("hour", "00");
		String min = moduleContext.optString("min", "00");
		localMsg.setTitle(title);
		localMsg.setContent(content);
		localMsg.setDate(date);
		localMsg.setHour(hour);
		localMsg.setMin(min);
		try {
			JSONObject customContent = new JSONObject(moduleContext.optString(
					"customContent", ""));
			Log.e("TPush", "add location:" + customContent);
			int length = customContent.length();
			if (customContent != null && length > 0) {
				HashMap<String, Object> map = new HashMap<String, Object>(
						length);
				Iterator<?> it = customContent.keys();
				while (it.hasNext()) {
					String key = (String) it.next();
					Object value = customContent.opt(key);
					map.put(key, value);
				}
				localMsg.setCustomContent(map);
			}
		} catch (JSONException e) {
		}
		localMsg.setActivity(moduleContext.optString("activity", ""));

		localMsg.setBuilderId(moduleContext.optInt("builderId", 0));
		localMsg.setRing(moduleContext.optInt("ring", 1));
		localMsg.setVibrate(moduleContext.optInt("vibrate", 1));
		localMsg.setIcon_type(moduleContext.optInt("iconType", 0));
		localMsg.setStyle_id(moduleContext.optInt("styleId", 1));
		localMsg.setAction_type(moduleContext.optInt("actionType", 1));

		localMsg.setRing_raw(moduleContext.optString("ringRaw", ""));
		localMsg.setIcon_res(moduleContext.optString("iconRes", ""));
		localMsg.setSmall_icon(moduleContext.optString("smallIcon", ""));
		localMsg.setUrl(moduleContext.optString("url", ""));
		localMsg.setIntent(moduleContext.optString("intent", ""));
		long notifactionId = XGPushManager.addLocalNotification(
				moduleContext.getContext(), localMsg);
		try {
			JSONObject result = new JSONObject();
			result.put("status", true);
			result.put("notiId", notifactionId);
			moduleContext.success(result, true);
		} catch (JSONException e) {
		}
	}

	@UzJavascriptMethod
	public void jsmethod_clearLocalNotifications(
			final UZModuleContext moduleContext) {
		initIdAndKey();
		XGPushManager.clearLocalNotifications(moduleContext.getContext());
	}

	@UzJavascriptMethod
	public void jsmethod_setListener(final UZModuleContext moduleContext) {
		initIdAndKey();
		String name = moduleContext.optString("name",
				XGCallbackListeners.TAG_ON_TEXT_MESSAGE);
		XGCallbackListeners.getInstance().put(name, moduleContext);
	}

	@UzJavascriptMethod
	public void jsmethod_removeListener(final UZModuleContext moduleContext) {
		initIdAndKey();
		String name = moduleContext.optString("name",
				XGCallbackListeners.TAG_ON_TEXT_MESSAGE);
		XGCallbackListeners.getInstance().remove(name);
	}

	@UzJavascriptMethod
	public void jsmethod_cancelNotifaction(final UZModuleContext moduleContext) {
		try {
			int id = moduleContext.optInt("nid", -1);
			NotificationManager nm = (NotificationManager) moduleContext
					.getContext()
					.getSystemService(Context.NOTIFICATION_SERVICE);
			if (id == -1) {
				nm.cancelAll();
			} else {
				nm.cancel(id);
			}
		} catch (Throwable ex) {

		}
	}
}
