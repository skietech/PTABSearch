package com.ptab.sky;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.io.Files;
import com.ptab.sky.nlp.OpenNLPProcessor;
import com.ptab.sky.nlp.StanfordNLPLoad;
import com.ptab.sky.nlp.StanfordNLPProcessor;
import com.ptab.sky.processors.FileProcessor;



@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = { "classpath:com/ptab/sky/config/PtabSkyDataContext.xml" })
@PropertySource("classpath:application.properties")

public class TestFileUtilsV2 {

	
	
	@Test
	public void testFileProcessor() throws IOException {
		
		List<String> file = FileUtils.readLines(new File("/Users/raviramani/SkyTechSpace/Airforce/sample/WSC-239.1.6.1-DGS-5-GMS.meta"));
		List<Object> list = Arrays.stream(file.toArray()).skip(300000).collect(Collectors.toList());
		FileUtils.writeLines(new File("/Users/raviramani/SkyTechSpace/Airforce/sample/WSC-239.1.6.1-DGS-5-GMS.meta1"), list);
		
	}

}
