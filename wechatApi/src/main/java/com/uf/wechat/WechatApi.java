package com.uf.wechat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.uf.wechat.bean.Oauth2Response;
import com.uf.wechat.bean.PrePayResponse;
import com.uf.wechat.bean.SendTemplateMsgRequest;
import com.uf.wechat.bean.SendTemplateMsgResult;
import com.uf.wechat.bean.TemplateMsgData;
import com.uf.wechat.bean.WechatUserInfo;
import com.uf.wechat.util.HttpsUtil;
import com.uf.wechat.util.Resource;

public class WechatApi {
	private static final String APP_ID = Resource.getAttribute("wechat.app_id");
	private static final String SECRET = Resource.getAttribute("wechat.secret");
	private static final String MCH_ID = Resource.getAttribute("wechat.mch_id");
	private static final String API_KEY = Resource.getAttribute("wechat.api_key");
	private static final String PAY_NOTIFY_URL = Resource.getAttribute("wechat.pay_notify_url");

	/**
	 * 通过access_token进行接口调用，获取用户基本数据资源或帮助用户实现基本操作
	 *
	 * @param access_token
	 * @return
	 */
	public static WechatUserInfo getWechatUserInfo(String code) {
		String get_user_info = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
		Oauth2Response oauth2Response = getOauth2Response(code);
		if (oauth2Response != null) {
			String url = String.format(get_user_info, oauth2Response.getAccess_token(), oauth2Response.getOpenid());
			JsonObject jsonObject = HttpsUtil.httpGet_ResponseJson(url);
			if (jsonObject != null && jsonObject.has("openid")) {
				Gson gson = new Gson();
				WechatUserInfo userInfo = gson.fromJson(jsonObject, WechatUserInfo.class);
				return userInfo;
			}
			System.out.println("getWechatUserInfo response:" + jsonObject.toString());
		}
		return null;
	}

	private static Oauth2Response getOauth2Response(String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
		url = String.format(url, APP_ID, SECRET, code);
		JsonObject jsonObject = HttpsUtil.httpGet_ResponseJson(url);
		if (jsonObject != null && jsonObject.has("access_token")) {
			Gson gson = new Gson();
			return gson.fromJson(jsonObject, Oauth2Response.class);
		}
		if (jsonObject != null) {
			System.out.println("getOauth2AccessTokon response:" + jsonObject.toString());
		} else {
			System.out.println("response is null");
		}
		return null;
	}

	private static Long preGetTokenTime = null;
	private static String cacheAccessToken = null;

	public static synchronized String getAccessToken() {
		if (cacheAccessToken == null || preGetTokenTime == null
				|| System.currentTimeMillis() - preGetTokenTime.longValue() > 3600 * 1000) {
			cacheAccessToken = getAccessTokenFromWechatApi();
			if (cacheAccessToken != null) {
				preGetTokenTime = System.currentTimeMillis();
			}
		}
		return cacheAccessToken;
	}

	private static String getAccessTokenFromWechatApi() {
		String getAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
		String url = String.format(getAccessTokenUrl, APP_ID, SECRET);
		System.out.println("getAccessToken url:" + url);
		JsonObject result = HttpsUtil.httpGet_ResponseJson(url);
		if (result != null && result.has("access_token")) {
			return result.get("access_token").getAsString();
		}
		System.out.println("getAccessToken error result:" + result);
		return null;
	}

	public static List<String> getOpenIds() {
		List<String> openIds = new ArrayList<String>();
		String url = String.format("https://api.weixin.qq.com/cgi-bin/user/get?access_token=%s", getAccessToken());
		boolean isneedNext = false;
		{
			JsonObject jsonResponse = HttpsUtil.httpGet_ResponseJson(url);
			int total = jsonResponse.get("total").getAsInt();
			JsonArray ids = jsonResponse.get("data").getAsJsonObject().get("openid").getAsJsonArray();
			if (ids != null && ids.size() > 0) {
				for (int i = 0; i < ids.size(); i++) {
					String id = ids.get(i).getAsString();
					openIds.add(id);
				}
			}
			if (total == openIds.size()) {
				isneedNext = false;
			} else {
				String nextOpenidTmp = jsonResponse.get("next_openid").getAsString();
				url = "&next_openid=" + nextOpenidTmp;
			}
		}
		while (isneedNext)
			;
		return openIds;
	}

	public static SendTemplateMsgResult sendMessageToUser(SendTemplateMsgRequest template) {
		SendTemplateMsgResult result = new SendTemplateMsgResult();
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + getAccessToken();
		result.setOpenId(template.getTouser());
		Gson gson = new Gson();
		JsonObject jsonResponse = HttpsUtil.httpPostJson_ResponseJson(url, gson.toJsonTree(template).getAsJsonObject());
		if (jsonResponse == null) {
			result.setIsOk(false);
			result.setErrmsg("wechat response is null");
			return sendMessageToUser(template);
		}
		if (jsonResponse.get("errcode").getAsInt() == 0) {
			result.setIsOk(true);
			int msgId = jsonResponse.get("msgid").getAsInt();
			result.setMsgId(msgId);
		} else {
			System.out.println("sendTemplateMsg response error :" + jsonResponse.toString());
			String err = jsonResponse.get("errmsg").getAsString();
			result.setIsOk(false);
			result.setErrmsg(err);
			return sendMessageToUser(template);
		}
		return result;
	}

	/**
	 * 发送消息 供业务流程中调用
	 *
	 * @param openId
	 *            openId
	 * @param data
	 *            消息发模板实例
	 */
	public static void sendMessageToUser(String openId, TemplateMsgData data) {
		if (openId != null && !openId.trim().equals("")) {
			SendTemplateMsgRequest template = new SendTemplateMsgRequest(openId, data.getTemplate_id(), data);
			WechatApi.sendMessageToUser(template);
		}
	}

	public static PrePayResponse prePay(String tradeNo, int orderPrice, String product_id, String body,
			String spbill_create_ip, String attach) {
		String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		packageParams.put("appid", APP_ID); // 公众账号ID
		packageParams.put("mch_id", MCH_ID); // 商户号
		packageParams.put("nonce_str", UUID.randomUUID().toString().replaceAll("-", "")); // 随机字符串
		packageParams.put("body", body); // 商品描述
		packageParams.put("out_trade_no", tradeNo); // 商户订单号
		packageParams.put("total_fee", orderPrice); // 交易金额:交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。对账单中的交易金额单位为【元】。
		packageParams.put("spbill_create_ip", spbill_create_ip); // 终端IP:APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
		packageParams.put("notify_url", PAY_NOTIFY_URL); // 通知地址:异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
		packageParams.put("trade_type", "NATIVE"); // 交易类型：NATIVE--原生扫码支付
		packageParams.put("product_id", product_id); // 商品ID:trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
		packageParams.put("time_start", formate.format(new Date())); // 交易起始时间
		packageParams.put("attach", attach); // 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。此例中传入当前用户id
		String sign = PayCommonUtil.createSign("UTF-8", packageParams, API_KEY);
		packageParams.put("sign", sign); // 签名

		String requestXML = PayCommonUtil.getRequestXml(packageParams);
		String response = HttpsUtil.httpPostXml_ResponseString(url, requestXML);
		PrePayResponse payResult = PayCommonUtil.parsePrePayResponse(response);
		return payResult;
	}

	public static String getJsApiTicket(String accessToken) {
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";
		JsonObject response = HttpsUtil.httpGet_ResponseJson(url);
		if (response != null && response.has("ticket")) {
			return response.get("ticket").getAsString();
		} else if (response != null) {
			System.out.println("getJsApiTicket response:" + response.toString());
		}
		return null;
	}
}
