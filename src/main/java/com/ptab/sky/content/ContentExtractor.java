package com.ptab.sky.content;

import java.io.File;
import java.util.Map;

public interface ContentExtractor {
	
	Map<String, Object> extractTextContent(File inFile);
	
	Map<String, Object> extractIMmageContent(File inFile);

}
