package com.uf.store.restful.action;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uf.wechat.WechatApi;
import com.uf.wechat.bean.WechatUserInfo;

@RestController
public class WechatAction {
	private Logger logger=LoggerFactory.getLogger(WechatAction.class);

	@RequestMapping(value = "wechatCheck", method = RequestMethod.GET)	
	public void wechatCheck(HttpServletRequest request,HttpServletResponse response) {
//		Enumeration<String> params=request.getParameterNames();
//		while(params.hasMoreElements()) {
//			String name=params.nextElement();
//			logger.info(name+":"+request.getParameter(name));
//		}
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        try {
			response.getWriter().write(echostr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
