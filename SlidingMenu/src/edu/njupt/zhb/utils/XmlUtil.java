package edu.njupt.zhb.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtil {

	public static List XmlStringToListByMap(String xml) {
		List datas = new ArrayList();
		if(xml.length()==0)
			return datas;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringComments(true);

		dbf.setIgnoringElementContentWhitespace(true);

		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(StringUtil.String2InputStream(xml));
			NodeList tagNodes = doc.getElementsByTagName("DATA");
			for (int i = 0; i < tagNodes.getLength(); i++) {
				Map map = new HashMap();
				Element element = (Element) tagNodes.item(i);
				map.put("ADI_ID",element.getAttribute("ADI_ID"));
				map.put("UI_CLASS",element.getAttribute("UI_CLASS"));
				map.put("ADI_NAME",element.getAttribute("ADI_NAME"));
				NodeList  atts = tagNodes.item(i).getChildNodes();
				for(int l=0;l<atts.getLength();l++){
					Node att = atts.item(l);
					if(att.getNodeType() ==Node.ELEMENT_NODE){
					Element tag = (Element) atts.item(l);
					map.put("UP_CODE",tag.getAttribute("UP_CODE"));
					map.put(tag.getAttribute("UP_CODE")+"_TAG",tag.getAttribute("TAG_NO"));
					}
					//map.put("ADI_NAME",tag.getAttribute("ADI_NAME"));
				}
				datas.add(map);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}
	
	
	public static List XmlStringToList(String xml) {
		List datas = new ArrayList();
		if(xml.length()==0)
			return datas;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringComments(true);

		dbf.setIgnoringElementContentWhitespace(true);

		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(StringUtil.String2InputStream(xml));
			NodeList tagNodes = doc.getElementsByTagName("DATA");
			for (int i = 0; i < tagNodes.getLength(); i++) {
				NodeList  atts = tagNodes.item(i).getChildNodes();
				Map map = new HashMap();
				for(int l=0;l<atts.getLength();l++){
					Node att = atts.item(l);
					if(att.getNodeType() ==Node.ELEMENT_NODE){
						Element childNode = (Element) att; 
						Node valueNode = childNode.getFirstChild();
						String value="";
						if(valueNode!=null)value = valueNode.getNodeValue();
						map.put(att.getNodeName(), value);
					}
					
				}
				datas.add(map);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}
}
