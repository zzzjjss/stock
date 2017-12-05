package com.uf.wechat;

import com.google.gson.JsonObject;
import com.uf.wechat.bean.JsCode2SessionResponse;
import com.uf.wechat.bean.WechatAppConfig;
import com.uf.wechat.util.HttpsUtil;

public class WechatAppApi {
	private WechatAppConfig config;
	public WechatAppApi(WechatAppConfig config) {
		this.config=config;
	}
	
	public JsCode2SessionResponse jsCode2SessionResponse(String jsCode) {
		String url="https://api.weixin.qq.com/sns/jscode2session?appid="+config.getAppId()+"&secret="+config.getAppSecret()+"&js_code="+jsCode+"&grant_type=authorization_code";
		JsonObject jsonResponse=HttpsUtil.httpGet_ResponseJson(url);
		JsCode2SessionResponse  response=new JsCode2SessionResponse();
		response.setOpenid(jsonResponse.get("openid").getAsString());
		response.setSessionKey(jsonResponse.get("session_key").getAsString());
		response.setUnionid(jsonResponse.get("unionid").getAsString());
		return response;
	}
	
	
}
