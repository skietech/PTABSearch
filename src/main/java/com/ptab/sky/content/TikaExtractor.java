package com.ptab.sky.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

@Component

public class TikaExtractor implements ContentExtractor {

	
	@Autowired
	private Environment env;

	
	@Override
	public Map<String, Object> extractTextContent(File inFile) {
		Map<String, Object> outMap = new HashMap<String, Object>();
		
		try {
				
			AutoDetectParser parser = new AutoDetectParser();
			BodyContentHandler handler = new BodyContentHandler(-1);
			Metadata metadata = new Metadata();
            
			InputStream stream = new FileInputStream(inFile);
			parser.parse(stream, handler, metadata);
		
			String originalContent = handler.toString();
	       
			originalContent = originalContent.replaceAll("\\s", " ");
			outMap.put("originalContent", originalContent);
			
			
		} catch (IOException | SAXException | TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("Out FromContent Extractor"+outMap);
		return outMap;
	}

	
	
	private Map<String, Object> processMetaData(Metadata md) {

		Map<String, Object> metaMap = new HashMap<String, Object>();
		String configuredAttributes = env.getProperty("JSON.INCLUDE.ATTRIBUTES");

		for (String name : md.names()) {
			
			if (configuredAttributes.contains(name)) {

				if (md.isMultiValued(name)) {
					metaMap.put(env.getProperty(name + ".JSON.label"), Arrays.asList(md.getValues(name)));
				} else {
					metaMap.put(env.getProperty(name + ".JSON.label"), md.get(name));
				}
			}

		}
		return metaMap;
	}



	@Override
	public Map<String, Object> extractIMmageContent(File inFile) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
