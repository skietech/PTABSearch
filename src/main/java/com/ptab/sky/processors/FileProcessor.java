package com.ptab.sky.processors;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ptab.sky.content.ContentExtractor;
import com.ptab.sky.content.TikaExtractorWithImageFile;

@Component
public class FileProcessor {

	@Autowired
	
	private TikaExtractorWithImageFile tikaExtrator;

	public List<Map> processFiles(String folder) {
		List<Map> contentList = new ArrayList<Map>();

		try {

			FileFilter fileFilter = new WildcardFileFilter("*.pdf");
			List<Path> filesToProcess = new ArrayList();
			Files.walk(Paths.get(folder)).filter(p -> p.toString().endsWith(".pdf")).filter(Files::isRegularFile)
					.forEach(filesToProcess::add);

			//System.out.println(filesToProcess);
			for (Path inDPat : filesToProcess) {

				
				Map<String, Object> contentMap = tikaExtrator.extractTextContent(new File(inDPat.toString()));

				if (null != contentMap && contentMap.size() > 0) {
					contentMap.put("caseno", StringUtils.substringBefore(inDPat.getFileName().toString(), "_"));
					contentMap.put("fileName", inDPat.toString());
					contentMap.put("id", StringUtils.substringBefore(inDPat.getFileName().toString(), "+"));
					contentMap.put("documentnumber", StringUtils.substringBetween(inDPat.getFileName().toString(), "_", "+"));
					contentMap.put("filedby", StringUtils.substringBetween(inDPat.getFileName().toString(), "+", "."));
					

					contentList.add(contentMap);
					//break;
				}
				
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contentList;

	}

}
