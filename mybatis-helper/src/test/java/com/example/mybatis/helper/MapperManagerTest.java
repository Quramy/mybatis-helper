package com.example.mybatis.helper;

import java.io.File;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.example.mybatis.helper.model.Mapper;

public class MapperManagerTest {

	private MapperManager mm;

	@Test
	public void buildTest01() throws Exception {
		mm = new MapperManager(new ConfigurationManager().read(new File(
				"src/test/resources/mybatis/configuration/conf.xml")));

		Mapper mapper01 = new Mapper("hoge");
		mm.addMapper(mapper01);

		Document doc = mm.build();

		assertXpath("/configuration/mappers/mapper[@resource='hoge']", doc);
	}

	@Test(expected = IllegalArgumentException.class)
	public void buildTest02() throws Exception {
		mm = new MapperManager(new ConfigurationManager().read(new File(
				"src/test/resources/mybatis/configuration/conf.xml")));

		Mapper mapper01 = new Mapper();
		mm.addMapper(mapper01);

		mm.build();
	}

	@Test
	public void buildTest03() throws Exception {
		mm = new MapperManager(new ConfigurationManager().read(new File(
				"src/test/resources/mybatis/configuration/conf_nomappers.xml")));

		Mapper mapper01 = new Mapper("hoge");
		mm.addMapper(mapper01);

		Document doc = mm.build();

		assertXpath("/configuration/mappers/mapper[@resource='hoge']", doc);
	}

	@Test
	public void buildTest04() throws Exception {
		mm = new MapperManager(new ConfigurationManager().read(new File(
				"src/test/resources/mybatis/configuration/conf_emptymappers.xml")));

		Mapper mapper01 = new Mapper("hoge");
		mm.addMapper(mapper01);

		Document doc = mm.build();

		assertXpath("/configuration/mappers/mapper[@resource='hoge']", doc);
	}

	private static void assertXpath(String query, Document doc) throws Exception {
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		XPathExpression expr = xPath.compile(query);
		Node res = (Node) expr.evaluate(doc, XPathConstants.NODE);
		Assert.assertNotNull(res);
	}
}
