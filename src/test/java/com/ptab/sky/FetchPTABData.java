package com.ptab.sky;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ptab.sky.dao.PtabTrailDAO;
import com.ptab.sky.nlp.OpenNLPProcessor;
import com.ptab.sky.nlp.StanfordNLPLoad;
import com.ptab.sky.processors.FileProcessor;



@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = { "classpath:com/ptab/sky/config/PtabSkyDataContext.xml" })

public class FetchPTABData {
	
	@Autowired
	private PtabTrailDAO ptabTrailDAO;
	
	
	@Test
	public void fetch() throws IOException {
		
		
		ptabTrailDAO.getTrails();
		
		
		
	}

}
