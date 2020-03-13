package com.ptab.sky;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ptab.sky.nlp.OpenNLPProcessor;
import com.ptab.sky.nlp.StanfordNLPLoad;
import com.ptab.sky.nlp.StanfordNLPProcessor;
import com.ptab.sky.processors.FileProcessor;



@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = { "classpath:com/ptab/sky/config/PtabSkyDataContext.xml" })
@PropertySource("classpath:application.properties")

public class TestFileProcessor {
	
	
	@Autowired
	private FileProcessor fileProcess;
	
	@Autowired
private StanfordNLPProcessor stndFordNLP;
	
	@Autowired
private OpenNLPProcessor openNLP;
	
	
	@Test
	public void testFileProcessor() throws IOException {
		
		//System.out.println(FileUtils.readLines(new File("/Users/raviramani/SkyTechSpace/Airforce/WSC-239.1.6.1-DGS-5-GMS.meta")).size());
		
		
		FileWriter fout = new FileWriter(new File("/Users/raviramani/SkyTechSpace/SkyData/PTAB/IPR2013-00133/NLPData"));
		
		List<Map> contentMa = fileProcess.processFiles("/Users/raviramani/SkyTechSpace/SkyData/PTAB/IPR2013-00133");
		fout.write("***********\n");
		//FileUtils.writeStringToFile(new File("/Users/raviramani/SkyTechSpace/SkyData/PTAB/IPR2013-00107/NLPData"), "***********\n", true);
		for (Map indvMap : contentMa)
		{
			
			String orginalContent = indvMap.get("originalContent").toString();
			String stripContent = orginalContent.replaceAll("[^a-zA-Z0-9]", " ");
			
			System.out.println(indvMap.get("fileName").toString());
				//System.out.println(stndFordNLP.getEntities(orginalContent));
			fout.write(indvMap.get("fileName").toString()+"\n");
			List<String> personEnt =  openNLP.getPersonEntities(stripContent);
			List<String> orgEnt =  openNLP.getOrganizationEntities(stripContent);
			Map out  = stndFordNLP.getEntities(orginalContent);
			System.out.println("Got Output");
			fout.write(personEnt+"\n");
			fout.write(orgEnt+"\n");
			fout.write(out+"\n");
			System.out.println("After");
			//FileUtils.writeStringToFile(new File("/Users/raviramani/SkyTechSpace/SkyData/PTAB/IPR2013-00107/NLPData"),indvMap.get("fileName").toString()+ stndFordNLP.getEntities(orginalContent)+"\n", true);
			//System.out.println( openNLP.getPersonEntities(indvMap.get("originalContent").toString()));
			//System.out.println( openNLP.getOrganizationEntities(indvMap.get("originalContent").toString()));
			//System.out.println( openNLP.getLocationEntities(indvMap.get("originalContent").toString()));
			fout.flush();
		}
		fout.close();
		
	}

}
