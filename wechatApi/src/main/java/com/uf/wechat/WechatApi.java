package com.uf.wechat;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import com.uf.wechat.bean.PrePayResponse;
import com.uf.wechat.bean.SendTemplateMsgRequest;
import com.uf.wechat.bean.SendTemplateMsgResult;
import com.uf.wechat.bean.TemplateMsgData;

public class WechatApi {
    
  
	private static final String LOGIN_APP_ID=Resource.getAttribute("wechat.login_app_id");
	private static final String LOGIN_SECRET=Resource.getAttribute("wechat.login_secret");
    private static final String APP_ID=Resource.getAttribute("wechat.app_id");
    private static final String SECRET=Resource.getAttribute("wechat.secret");
    private static final String MCH_ID=Resource.getAttribute("wechat.mch_id");
    private static final String API_KEY=Resource.getAttribute("wechat.api_key");
    private static final String PAY_NOTIFY_URL=Resource.getAttribute("wechat.pay_notify_url");
    
	/**获取access_token	*/
	private static String get_access_token = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
	/**获取用户信息	*/
	private static String get_user_info = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
	
	/**
	 * 通过access_token进行接口调用，获取用户基本数据资源或帮助用户实现基本操作
	 * @param access_token
	 * @return
	 */
	public static JSONObject getUserInfo(String code){
//		String url = String.format(get_user_info, access_token, "wx887cfe28ae411a6d");
		
		JSONObject accessToken = getAccessToken(code);
		String access_token = accessToken.getString("access_token");
		String openid = accessToken.getString("openid");
		String url = String.format(get_user_info, access_token, openid);
		System.out.println("code:"+code);
		System.out.println("access_token:"+access_token);
		System.out.println("openid:"+openid);
		
//		JSONObject result = HttpsUtil.HttpJsonPostRequest(url);
		JSONObject result = HttpsUtil.HttpGetRequest(url);
		System.out.println("unionid:"+result.getString("unionid"));
		if(result==null || !result.containsKey("unionid")){
			return getUserInfo(code);
		}
		return result;
	}
	
	/**
	 * 通过code参数加上AppID和AppSecret等，通过API换取access_token
	 * @param code
	 * @return
	 */
	public static JSONObject getAccessToken(String code){
		String url = String.format(get_access_token,LOGIN_APP_ID,LOGIN_SECRET,code);
//		JSONObject result = HttpsUtil.HttpJsonPostRequest(url);
		JSONObject result = HttpsUtil.HttpGetRequest(url);
		if(result==null || !result.containsKey("access_token")){
			return getAccessToken(code);
		}
		return result;
	}
	
	private static Long preGetTokenTime=null;
	private static String cacheAccessToken=null;
	public static synchronized String getAccessToken(){
			if(cacheAccessToken==null||preGetTokenTime==null||System.currentTimeMillis()-preGetTokenTime.longValue()>3600*1000){
				cacheAccessToken=getAccessTokenFromWechatApi();
				if(cacheAccessToken!=null){
					preGetTokenTime=System.currentTimeMillis();
				}
			}
			return cacheAccessToken;
	}
	
	private static String getAccessTokenFromWechatApi(){
		String getAccessTokenUrl="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
		String url = String.format(getAccessTokenUrl, APP_ID,SECRET);
		System.out.println("getAccessToken url:"+url);
//		JSONObject result = HttpsUtil.HttpJsonPostRequest(url);
		JSONObject result = HttpsUtil.HttpGetRequest(url);
		if(result!=null&&result.containsKey("access_token")){
			return result.getString("access_token");
		}else{
			getAccessTokenFromWechatApi();
		}
		System.out.println("getAccessToken error result:"+result);
		return null;
	}
	public static List<String> getOpenIds(){
		List<String>  openIds=new ArrayList<String>();
		String url=String.format("https://api.weixin.qq.com/cgi-bin/user/get?access_token=%s",getAccessToken());
		String nextOpenId="";
		boolean isneedNext=false;
		{
			JSONObject jsonResponse=HttpsUtil.HttpGetRequest(url+nextOpenId);
			int total=jsonResponse.getInt("total");
			JSONArray ids=jsonResponse.getJSONObject("data").getJSONArray("openid");
			if(ids!=null&&ids.size()>0){
				for(int i=0;i<ids.size();i++){
					String id=ids.getString(i);
					openIds.add(id);
				}
			}
			if(total==openIds.size()){
				isneedNext=false;
			}else{
				String nextOpenid=jsonResponse.getString("next_openid");
				nextOpenId="&next_openid="+nextOpenid;
			}
		}while(isneedNext);
		return openIds;
	}
	public static SendTemplateMsgResult sendMessageToUser(SendTemplateMsgRequest template){
	  SendTemplateMsgResult result=new SendTemplateMsgResult();
	  String url="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+getAccessToken();
	  result.setOpenId(template.getTouser());
	  JSONObject jsonResponse=HttpsUtil.httpJsonPostRequest(url, JSONObject.fromObject(template).toString());
	  if(jsonResponse==null){
		  result.setIsOk(false);
	      result.setErrmsg("wechat response is null");
	      return sendMessageToUser(template);
	  }
	  if(jsonResponse.getInt("errcode")==0){
		  result.setIsOk(true);
		  int msgId=jsonResponse.getInt("msgid");
		  result.setMsgId(msgId);
	  }else {
        System.out.println("sendTemplateMsg response error :"+jsonResponse.toString());
        String err=jsonResponse.getString("errmsg");
        result.setIsOk(false);
        result.setErrmsg(err);
        return sendMessageToUser(template);
      }
	  return result;
	}
	
	/**
	 * 发送消息
	 * 供业务流程中调用
	 * @param openId openId
	 * @param data 消息发模板实例
	 */
	public static void sendMessageToUser(String openId,TemplateMsgData data){
		if(StringUtils.isNotBlank(openId)){
			SendTemplateMsgRequest template = new SendTemplateMsgRequest(openId,data.getTemplate_id(),data); 
			WechatApi.sendMessageToUser(template);
		}
	}
	
	public static PrePayResponse prePay(String tradeNo,int orderPrice,String product_id,String body,String spbill_create_ip,String attach){
	  String url="https://api.mch.weixin.qq.com/pay/unifiedorder";
      SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
      packageParams.put("appid", APP_ID);		//公众账号ID
      packageParams.put("mch_id", MCH_ID);		//商户号
      packageParams.put("nonce_str", UUID.randomUUID().toString().replaceAll("-", ""));		//随机字符串
      packageParams.put("body", body);		//商品描述
      packageParams.put("out_trade_no", tradeNo);		//商户订单号
      packageParams.put("total_fee", orderPrice);		//交易金额:交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。对账单中的交易金额单位为【元】。
      packageParams.put("spbill_create_ip",spbill_create_ip);		//终端IP:APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
      packageParams.put("notify_url", PAY_NOTIFY_URL);		//通知地址:异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
      packageParams.put("trade_type", "NATIVE");		//交易类型：NATIVE--原生扫码支付
      packageParams.put("product_id", product_id);		//商品ID:trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
      packageParams.put("time_start", DateUtils.formatDate("yyyyMMddHHmmss"));		//交易起始时间
      packageParams.put("attach", attach);		//附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。此例中传入当前用户id
      String sign=PayCommonUtil.createSign("UTF-8", packageParams, API_KEY);
      packageParams.put("sign", sign);		//签名
      
      String requestXML = PayCommonUtil.getRequestXml(packageParams);
      String  response=HttpTookit.doPost(url, requestXML, "UTF-8");
      PrePayResponse payResult=PayCommonUtil.parsePrePayResponse(response);
      return payResult;
	}
	
	public static String getJsapiTicket(String accessToken){
	  String url="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi";
	  JSONObject response=HttpsUtil.HttpGetRequest(url);
	  if (response!=null&&response.containsKey("ticket")){
        return response.getString("ticket");
      }else if(response!=null){
    	  System.out.println("getJsApiTicket response:"+response.toString());
      }
	  return  null;
	}
	public static String getOauth2Openid(String code){
		String getOpenIdUrl="https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
		String url=String.format(getOpenIdUrl, WechatApi.APP_ID,WechatApi.SECRET,code);
		JSONObject response=HttpsUtil.HttpGetRequest(url);
		if(response==null){
			System.out.println("getOauth2Openid response is null,url:"+url);
			return null;
		}
		if(response!=null&&response.containsKey("openid")){
			return response.getString("openid");
		}
		System.out.println("getOauth2Openid  response is:"+response.toString());
		return null;
	}
	
}
