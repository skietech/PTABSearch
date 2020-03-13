package com.ptab.sky.dao;


import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@PropertySource("classpath:SearchJsonAttributes.properties")
public class LoadContentDao {

	@Autowired
	private Environment env;
	
	

	public void loadContent(String jsonString, String id,boolean isSummary) {

		TransportClient client = null;
		try {
			client = new PreBuiltTransportClient(Settings.EMPTY)
					.addTransportAddress(new TransportAddress(InetAddress.getByName(env.getProperty("ELASTIC.HOST")), Integer.parseInt(env.getProperty("ELASTIC.PORT"))));
           if (isSummary)
         {
        	   IndexResponse response = client.prepareIndex("rfisummaries", "RFIDoc", id).setSource(jsonString).get();
           }
          
			IndexResponse response = client.prepareIndex("dtrasearch", "RFIDoc", id).setSource(jsonString).get();
			
			
			
			client.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

	}
//
//		// on shutdown
//
	}
	
	public String searchContent(String inString)
	{
		RestTemplate springRestClient = new RestTemplate();
		
	String request = null;
		try {
			request = FileUtils.readFileToString(new File(env.getProperty("summary.search.mlt.teamplate.lcation").toString()), "UTF-8");
			ResponseEntity<String> response = springRestClient.postForEntity(env.getProperty("elastic.summary.url"), request, String.class);
			
	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	}
		return request;
		
		
	}

	

}
