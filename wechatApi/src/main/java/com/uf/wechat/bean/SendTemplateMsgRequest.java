package com.uf.wechat.bean;

public class SendTemplateMsgRequest {
	private String touser;
	private String template_id;
	private TemplateMsgData data;

	public SendTemplateMsgRequest() {
		
	}
	
	public SendTemplateMsgRequest(String touser, String template_id,TemplateMsgData data) {
		super();
		this.touser = touser;
		this.template_id = template_id;
		this.data = data;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public TemplateMsgData getData() {
		return data;
	}

	public void setData(TemplateMsgData data) {
		this.data = data;
	}
}
