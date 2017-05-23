package com.uf.wechat;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.digest.DigestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.uf.wechat.bean.PrePayResponse;
import com.uf.wechat.bean.WechatPayResult;


public class PayCommonUtil {

  /**
   * @author
   * @date 2016-4-22
   * @Description：sign签名
   * @param characterEncoding
   *            编码格式
   * @param parameters
   *            请求参数
   * @return
   */
  public static String createSign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {
      StringBuffer sb = new StringBuffer();
      Set es = packageParams.entrySet();
      Iterator it = es.iterator();
      while (it.hasNext()) {
          Map.Entry entry = (Map.Entry) it.next();
          String k = (String) entry.getKey();
          String v = "";
          try {
              v = (String) entry.getValue();
          } catch (Exception e) {
              // TODO: handle exception
              v = entry.getValue() + "";
          }

          if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
              sb.append(k + "=" + v + "&");
          }
      }
      sb.append("key=" + API_KEY);
      String mysign=null;
      try {
        mysign = DigestUtils.md5Hex(sb.toString().getBytes(characterEncoding)).toLowerCase();
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      return mysign;
  }
public static String sha1Sign(String str){
    try {
        //指定sha1算法
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(str.getBytes());
        //获取字节数组
        byte messageDigest[] = digest.digest();
        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        // 字节数组转换为 十六进制 数
        for (int i = 0; i < messageDigest.length; i++) {
            String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(shaHex);
        }
        return hexString.toString().toLowerCase();

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
	return null;
}
  /**
   * @author
   * @date 2016-4-22
   * @Description：将请求参数转换为xml格式的string
   * @param parameters
   *            请求参数
   * @return
   */
  public static String getRequestXml(SortedMap<Object, Object> parameters) {
      StringBuffer sb = new StringBuffer();
      sb.append("<xml>");
      Set es = parameters.entrySet();
      Iterator it = es.iterator();
      while (it.hasNext()) {
          Map.Entry entry = (Map.Entry) it.next();
          String k = (String) entry.getKey();
          String v = "";
          try {
              v = (String) entry.getValue();
          } catch (Exception e) {
              // TODO: handle exception
              v = entry.getValue() + "";
          }

          if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
              sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
          } else {
              sb.append("<" + k + ">" + v + "</" + k + ">");
          }
      }
      sb.append("</xml>");
      return sb.toString();
  }

    public static PrePayResponse parsePrePayResponse(String responseXml){
      if(null == responseXml || "".equals(responseXml)) {
        return null;
      }
      try {
        DocumentBuilder builder=DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document=builder.parse(new InputSource(new ByteArrayInputStream(responseXml.getBytes("UTF-8"))));
        PrePayResponse response=new PrePayResponse();
        Field fields[]=response.getClass().getDeclaredFields();
        for(Field field:fields){
          String fieldName=field.getName();
          NodeList nodes=document.getElementsByTagName(fieldName);
          if (nodes!=null&&nodes.getLength()>0) {
            Node node=nodes.item(0);
            if (node !=null&&node instanceof Element) {
              Element ele=(Element)node;
              String value=ele.getTextContent();
              field.setAccessible(true);
              field.set(response, value);
            }
          }
        }
        return response;
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    public static WechatPayResult parsePayResult(String payResultXml){
      if(null == payResultXml || "".equals(payResultXml)) {
        return null;
      }
      try {
        DocumentBuilder builder=DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document=builder.parse(new InputSource(new ByteArrayInputStream(payResultXml.getBytes("UTF-8"))));
        WechatPayResult response=new WechatPayResult();
        Field fields[]=response.getClass().getDeclaredFields();
        for(Field field:fields){
          String fieldName=field.getName();
          NodeList nodes=document.getElementsByTagName(fieldName);
          if (nodes!=null&&nodes.getLength()>0) {
            Node node=nodes.item(0);
            if (node !=null&&node instanceof Element) {
              Element ele=(Element)node;
              Object value=ele.getTextContent();
              field.setAccessible(true);
              field.set(response, value);
//              field.set(response, convert(value, field.getType()));
            }
          }
        }
        return response;
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    public static Object convert(Object object, Class<?> type) {
        if (object instanceof Number) {
            Number number = (Number) object;
            if (type.equals(byte.class) || type.equals(Byte.class)) {
                return number.byteValue();
            }
            if (type.equals(short.class) || type.equals(Short.class)) {
                return number.shortValue();
            }
            if (type.equals(int.class) || type.equals(Integer.class)) {
                return number.intValue();
            }
            if (type.equals(long.class) || type.equals(Long.class)) {
                return number.longValue();
            }
            if (type.equals(float.class) || type.equals(Float.class)) {
                return number.floatValue();
            }
            if (type.equals(double.class) || type.equals(Double.class)) {
                return number.doubleValue();
            }
        }
        return object;
    }


}