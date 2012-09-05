package com.example.mybatis.helper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.example.mybatis.helper.exception.RuntimeXPathException;
import com.example.mybatis.helper.model.Mapper;

public class MapperManager {
	private Document doc;

	private Text indenteTemplate;

	private List<Mapper> mapperList;

	public MapperManager(Document doc) {
		this.doc = doc;
		mapperList = new ArrayList<Mapper>();
	}

	public void addMapper(Mapper mapper) {
		mapperList.add(mapper);
	}

	public Document build() {
		Node mappers = getMappers();

		for (Mapper mapper : mapperList) {
			Node first = mappers.getFirstChild();
			if (first != null) {
				mappers.insertBefore(indenteTemplate.cloneNode(false), first);
				mappers.insertBefore(createMapper(mapper), first);
			} else {
				mappers.appendChild(createMapper(mapper));
				mappers.appendChild(indenteTemplate.cloneNode(false));
			}
		}

		return doc;
	}

	private Node getMappers() {
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		try {
			XPathExpression expr = xPath.compile("/configuration/mappers");
			Node mappers = (Node) expr.evaluate(doc, XPathConstants.NODE);
			if (mappers != null) {
				Node first = mappers.getFirstChild();
				if (first instanceof Text) {
					indenteTemplate = (Text) first.cloneNode(false);
				}
			} else {
				mappers = doc.createElement("mappers");
				indenteTemplate = doc.createTextNode(System.getProperty("line.separator"));
				doc.getDocumentElement().appendChild(mappers);
				doc.getDocumentElement().appendChild(indenteTemplate);
				mappers.appendChild(indenteTemplate.cloneNode(false));
			}

			if (indenteTemplate == null) {
				indenteTemplate = doc.createTextNode(System.getProperty("line.separator"));
			}

			return mappers;
		} catch (XPathExpressionException e) {

			throw new RuntimeXPathException(e);
		}
	}

	public Node createMapper(Mapper mapper) {
		Element node = doc.createElement("mapper");
		if (mapper.getResource() != null) {
			Attr attr = doc.createAttribute("resource");
			attr.setValue(mapper.getResource());
			node.setAttributeNode(attr);
		} else if (mapper.getUrl() != null) {
			Attr attr = doc.createAttribute("url");
			attr.setValue(mapper.getUrl());
			node.setAttributeNode(attr);
		} else if (mapper.getClazz() != null) {
			Attr attr = doc.createAttribute("class");
			attr.setValue(mapper.getClazz());
			node.setAttributeNode(attr);
		} else {
			throw new IllegalArgumentException("mapper should have 'resource' or 'url' or 'class' attribute. ");
		}

		return node;
	}
}
