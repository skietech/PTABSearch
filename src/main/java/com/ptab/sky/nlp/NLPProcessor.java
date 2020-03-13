package com.ptab.sky.nlp;

import java.util.List;
import java.util.Map;

public interface NLPProcessor {
	
	List<String> getPersonEntities(String inText);
	List<String> getOrganizationEntities(String inText);
	List<String> getLocationEntities(String inText);
	List<String> getEventsEntities(String inText);
	List<String> getDatesEntities(String inText);
	
	Map<String,Object> getEntities(String inText);
}
