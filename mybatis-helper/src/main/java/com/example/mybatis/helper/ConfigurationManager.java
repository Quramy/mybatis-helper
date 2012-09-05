package com.example.mybatis.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.tools.ant.BuildException;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.example.mybatis.helper.exception.RuntimeIOException;
import com.example.mybatis.helper.exception.RuntimeParserConfigurationException;
import com.example.mybatis.helper.exception.RuntimeSAXException;

public class ConfigurationManager {

	private static final String SYSTEM_ID = "http://mybatis.org/dtd/mybatis-3-config.dtd";
	private static final String PUBLIC_ID = "-//mybatis.org//DTD Config 3.0//EN";

	public Document read(File file) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document doc = null;
		try {
			db = dbf.newDocumentBuilder();
			db.setEntityResolver(new mybatisEntityResolver());
			doc = db.parse(file);

		} catch (ParserConfigurationException e) {
			throw new RuntimeParserConfigurationException(e);
		} catch (SAXException e) {
			throw new RuntimeSAXException(e);
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}

		return doc;
	}

	public void write(Document doc, File dist) {
		write(doc, dist, true);

	}

	public void write(Document doc, File dist, boolean override) {

		if (!override) {
			if (dist.exists()) {
				return;
			}
		}
		try {
			DOMSource source = new DOMSource(doc);

			TransformerFactory transFactory = TransformerFactory.newInstance();

			// transFactory.setAttribute("indent-number", new Integer(2));

			// transFactory.setAttribute(, )
			Transformer transformer;

			transformer = transFactory.newTransformer();
			// transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, SYSTEM_ID);
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, PUBLIC_ID);
			// transformer.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT,
			// "2");

			FileOutputStream os = new FileOutputStream(dist);
			StreamResult result = new StreamResult(os);
			transformer.transform(source, result);

		} catch (IOException e) {
			throw new RuntimeIOException(e);
		} catch (TransformerConfigurationException e) {
			throw new BuildException(e);
		} catch (TransformerException e) {
			throw new BuildException(e);
		}

	}

	final class mybatisEntityResolver implements EntityResolver {

		private static final String DTD_RESOURCE_LOCATION = "mybatis-3-config.dtd";

		public mybatisEntityResolver() {

		}

		@Override
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			if (systemId.equals(SYSTEM_ID)) {
				return new InputSource(getClass().getResourceAsStream(DTD_RESOURCE_LOCATION));
			}
			// If no match, returning null makes process continue normally
			return null;
		}

	}
}
