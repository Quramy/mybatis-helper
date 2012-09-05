package com.example.mybatis.helper.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;

import com.example.mybatis.helper.ConfigurationManager;
import com.example.mybatis.helper.MapperManager;
import com.example.mybatis.helper.model.Mapper;

public class AddMapperTask extends Task {

	private File srcfile;

	private File distfile;

	private boolean verbose = false;

	private List<Mapperset> mappersetList = new ArrayList<Mapperset>();

	private ConfigurationManager configurationManager;

	public File getSrcfile() {
		return srcfile;
	}

	public void setSrcfile(File srcfile) {
		this.srcfile = srcfile;
	}

	public File getDistfile() {
		return distfile;
	}

	public void setDistfile(File distfile) {
		this.distfile = distfile;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public List<Mapperset> getMappersetList() {
		return mappersetList;
	}

	public void setMappersetList(List<Mapperset> mappersetList) {
		this.mappersetList = mappersetList;
	}

	public void addMapperset(Mapperset mapperset) {
		this.mappersetList.add(mapperset);
	}

	@Override
	public void execute() {
		Document doc = addConf();
		MapperManager mm = new MapperManager(doc);
		
		String sep = System.getProperty("file.separator");
		
		for (Mapperset mapperset : mappersetList) {
			DirectoryScanner directoryScanner = mapperset.getDirectoryScanner();
			for (String name : directoryScanner.getIncludedFiles()) {
				Mapper mapper = new Mapper();

				if (mapperset.getType().equals(Mapperset.TYPE_URL)) {

					String url = "file://" + directoryScanner.getBasedir().getAbsolutePath() + sep + name;
					if (verbose) {
						log("add mapper(url: " + url + " )");
					}
					mapper.setUrl(url);
				} else if (mapperset.getType().equals(Mapperset.TYPE_RESOURCE)) {
					String resource = name;
					if (verbose) {
						log("add mapper(resource: " + resource + " )");
					}
					mapper.setResource(resource);
				}
				mm.addMapper(mapper);
			}
		}

		writeConf(mm.build());
	}

	private Document addConf() {
		configurationManager = new ConfigurationManager();
		Document doc = configurationManager.read(srcfile);

		return doc;
	}

	private void writeConf(Document doc) {
		configurationManager.write(doc, distfile);
	}
}
