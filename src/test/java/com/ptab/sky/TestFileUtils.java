package com.ptab.sky;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import com.ptab.sky.nlp.OpenNLPProcessor;
import com.ptab.sky.nlp.StanfordNLPLoad;
import com.ptab.sky.processors.FileProcessor;

import lombok.Data;



@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = { "classpath:com/ptab/sky/config/PtabSkyDataContext.xml" })
@Data
public class TestFileUtils {
	
	
	
	
	
	
	public void testFileProcessor() throws IOException, JAXBException {
		
		Path path = Paths.get("/Users/raviramani/Documents/Contracts/Raytheon/EAS/audiodata","sample.mp3");
		// SeekableByteChannel sbc = (SeekableByteChannel) Files.newByteChannel(path, StandardOpenOption.READ);
		
       
		
		
		File nn  = new File("/Users/raviramani/Documents/SkyTechProjects/PatentData/ipgb20180102.xml");
		  File file = new File("/Users/raviramani/Documents/SkyTechProjects/PatentData/1199526405.xml");
	        JAXBContext jaxbContext = JAXBContext.newInstance(UsPatentGrant.class);
	        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	        UsPatentGrant product = (UsPatentGrant) unmarshaller.unmarshal(file);
	        System.out.println(product.getUsBibliographicDataGrant().getInventionTitle().value);
	       // System.out.println(product.getUsBibliographicDataGrant().get);
	    	
		List<String> vv = FileUtils.readLines(nn, "UTF-8");
		
		System.out.println(vv.size());
		boolean start=false;
		List<String> fileToCreate = new ArrayList();
				for (String indvLine : vv)
		{
			if (indvLine.contains("<us-patent-grant"))
			{
				start= true;
				//System.out.println("Start");
				
			}
			if (indvLine.contains("</us-patent-grant"))
			{
				start=false;
				fileToCreate.add(indvLine);
				FileUtils.writeLines(new File("/Users/raviramani/Documents/SkyTechProjects/PatentData/"+new Random().nextInt()), fileToCreate);
				fileToCreate = new ArrayList();		
				//System.out.println("End");
			}
			if (start)
			{
				fileToCreate.add(indvLine);
			}
			
		}
		//FileUtils.readFileToByteArray(new File("/Users/raviramani/Documents/ipgb20180102_wk01⁩/⁨ipgb20180102lst.txt"));
		//FileUtils.readLines(new File("/Users/raviramani/Documents/ipgb20180102_wk01⁩/⁨ipgb20180102.xml"));
	}

}
