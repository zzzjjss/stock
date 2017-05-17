package com.uf.wechat.bean;

public class TemplateMsgData {
	
	protected String template_id;
	protected String msgType;

	public static class TemplateMsgDataItem {
		private String value;
		private String color;

		public TemplateMsgDataItem(String value,String color){
			this.value=value;
			this.color=color;
		}
		
		public TemplateMsgDataItem(String value){
			this.value=value;
			this.color="#173177";
		}

		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
		}
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
}
