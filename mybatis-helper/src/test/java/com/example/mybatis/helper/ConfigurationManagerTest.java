package com.example.mybatis.helper;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.example.mybatis.helper.exception.RuntimeIOException;
import com.example.mybatis.helper.exception.RuntimeSAXException;

public class ConfigurationManagerTest {

	@Test
	public void readTest01() throws Exception {
		File src = new File("src/test/resources/mybatis/configuration/conf.xml");
		ConfigurationManager cr = new ConfigurationManager();
		Document doc = cr.read(src);
		Assert.assertNotNull(doc);
	}

	@Test(expected = RuntimeIOException.class)
	public void readTest02() throws Exception {
		File src = new File("src/test/resources/mybatis/configuration/nowhere.xml");
		ConfigurationManager cr = new ConfigurationManager();
		cr.read(src);
	}

	@Test(expected = RuntimeSAXException.class)
	public void readTest03() throws Exception {
		File src = new File("src/test/resources/mybatis/configuration/conf_broken.xml");
		ConfigurationManager cr = new ConfigurationManager();
		cr.read(src);
	}

	@Test
	public void writeTest01() throws Exception {
		
		beforeWrite();

		File src = new File("src/test/resources/mybatis/configuration/conf.xml");
		ConfigurationManager configurationManager = new ConfigurationManager();
		configurationManager.read(src);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.newDocument();

		Element test = document.createElement("configuration");
		document.appendChild(test);

		Element mappers = document.createElement("mappers");
		Element mapper = document.createElement("mapper");
		test.appendChild(mappers);
		mapper.setAttribute("resource", "hoge.xml");
		mappers.appendChild(mapper);
		
		File dist = new File("target/testdist/writeTest01.xml");

		configurationManager.write(document,dist);
		
		Assert.assertTrue(dist.exists());
	}
	
	private static void beforeWrite(){
		File dir = new File("target/testdist");
		if(!dir.exists()){
			dir.delete();
		}
		dir.mkdir();
	}

}
