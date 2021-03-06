package com.tonghang.web.common.util;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author 蔡政滦
 * @version 2015年7月23日
 */
public class XmlUtils {
	
	/**
	* 根据Map组装xml消息体，值对象仅支持基本数据类型、String、BigInteger、BigDecimal，以及包含元素为上述支持数据类型的Map
	* 
	* @param vo
	* @param rootElement
	* @return
	* @author modify by 2015-6-5
	*/
	public static String map2xmlBody(Map<String, Object> vo) {
		org.dom4j.Document doc = DocumentHelper.createDocument();
		Element body = DocumentHelper.createElement("Request");
		doc.add(body);
		__buildMap2xmlBody(body, vo);
		return doc.asXML();
	}
	
	private static void __buildMap2xmlBody(Element body, Map<String, Object> vo) {
		if (vo != null) {
			Iterator<String> it = vo.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				if (StringUtils.isNotEmpty(key)) {
					Object obj = vo.get(key);
					Element element = DocumentHelper.createElement(key);
					if (obj != null) {
						if (obj instanceof java.lang.String) {
							element.setText((String) obj);
						} else {
							if (obj instanceof java.lang.Character || obj instanceof java.lang.Boolean || obj instanceof java.lang.Number
									|| obj instanceof java.math.BigInteger || obj instanceof java.math.BigDecimal) {
								element.setText(String.valueOf(obj));
							} else if (obj instanceof java.util.Map) {
								org.dom4j.Attribute attr = DocumentHelper.createAttribute(element, "type", java.util.Map.class.getCanonicalName());
								element.add(attr);
								__buildMap2xmlBody(element, (Map<String, Object>) obj);
							} else {
							}
						}
					}
					body.add(element);
				}
			}
		}
	}
	
	/**
	 * 根据xml消息体转化为Map
	 * 
	 * @param xml
	 * @param rootElement
	 * @return
	 * @throws DocumentException
	 * @author modify by 2015-6-5
	 */
	public static Map xmlBody2map(String xml,String root) throws DocumentException {
		org.dom4j.Document doc = DocumentHelper.parseText(xml);
		Element body = (Element) doc.selectSingleNode("/"+root);
		Map vo = __buildXmlBody2map(body);
		return vo;
	}
	
	private static Map __buildXmlBody2map(Element body) {
		Map vo = new HashMap();
		if (body != null) {
			List<Element> elements = body.elements();
			for (Element element : elements) {
				String key = element.getName();
				if (StringUtils.isNotEmpty(key)) {
					String text = element.getText().trim();
					Object value = null;
					vo.put(key, value);
				}
			}
		}
		return vo;
	}
	
}
