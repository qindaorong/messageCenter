package com.xhxd.messagecenter.common.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 输出xml和解析xml的工具类
 * 
 * @ClassName:XmlUtil
 * @author: chenyoulong Email: chen.youlong@payeco.com
 * @date :2012-9-29 上午9:51:28
 * @Description:TODO
 */
public class XmlUtil {
	/**
	 * java 转换成xml
	 * 
	 * @Title: toXml
	 * @Description: TODO
	 * @param obj
	 *            对象实例
	 * @return String xml字符串 属性-值
	 */
	public static String toXml(Object obj) {
		XStream xstream = new XStream();
		xstream.processAnnotations(obj.getClass()); // 通过注解方式的，一定要有这句话
		xstream.aliasSystemAttribute(null, "class"); // 去掉 class 属性 
		return xstream.toXML(obj);
	}
	
	/**
	 * java 转换成xml
	 * 
	 * @Title: toXml
	 * @Description: TODO
	 * @param obj
	 *            对象实例
	 * @param alias 别名
	 * @return String xml字符串 属性-值
	 */
	public static String toXml(Object obj,String alias,Class classes) {
		XStream xstream = new XStream(new DomDriver(null,new XmlFriendlyNameCoder("_-", "_")));
		xstream.processAnnotations(obj.getClass()); // 通过注解方式的，一定要有这句话
		xstream.alias(alias, classes);
		return xstream.toXML(obj);
	}

	/**
	 * 将传入xml文本转换成Java对象
	 * 
	 * @Title: toBean
	 * @Description: TODO
	 * @param xmlStr
	 * @param cls
	 *            xml对应的class类
	 * @return T xml对应的class类的实例对象
	 * 
	 *         调用的方法实例：PersonBean person=XmlUtil.toBean(xmlStr,
	 *         PersonBean.class);
	 */
	public static <T> T toBean(String xmlStr, Class<T> cls) {
		// 注意：不是new Xstream(); 否则报错：java.lang.NoClassDefFoundError:
		// org/xmlpull/v1/XmlPullParserFactory
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(cls);
		xstream.alias(Integer.class.getName(), cls);
		xmlStr = xmlStr.replaceFirst("<xml>", "<"+cls.getName()+">").replace("</xml>", "</"+cls.getName()+">");
		
		System.out.println("----------");
		T obj = (T) xstream.fromXML(xmlStr);
		return obj;
	}
	

/**
	 * Xml string转换成Map
	 * @param xmlStr
	 * @return
	 */
	public static Map<String,Object> xmlToMap(String xmlStr){
		Map<String,Object> map = new HashMap<String,Object>();
		Document doc;
		try {
			doc = DocumentHelper.parseText(xmlStr);
			Element el = doc.getRootElement();
			map = recGetXmlElementValue(el,map);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return map;
	}

/**
	 * 循环解析xml
	 * @param ele
	 * @param map
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	private static Map<String, Object> recGetXmlElementValue(Element ele, Map<String, Object> map){
		List<Element> eleList = ele.elements();
		if (eleList.size() == 0) {
			map.put(ele.getName(), ele.getTextTrim());
			return map;
		} else {
			for (Iterator<Element> iter = eleList.iterator(); iter.hasNext();) {
				Element innerEle = iter.next();
				recGetXmlElementValue(innerEle, map);
			}
			return map;
		}
	}

}