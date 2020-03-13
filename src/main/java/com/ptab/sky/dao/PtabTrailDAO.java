package com.ptab.sky.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptab.sky.domain.CollectionPayload;
import com.ptab.sky.domain.CollectionPayloadDocuments;
import com.ptab.sky.domain.DocumentsMetaData;
import com.ptab.sky.domain.LinkDetails;
import com.ptab.sky.domain.PtabTrail;
import com.ptab.sky.nlp.OpenNLPProcessor;
import com.ptab.sky.nlp.StanfordNLPProcessor;
import com.ptab.sky.processors.FileProcessor;

@Service
@PropertySource("classpath:application.properties")
public class PtabTrailDAO {

	@Value("${ptab.trail.url}")
	private String url;

	@Value("${ptab.trail.limit}")
	private String limit;

	@Value("${ptab.trail.offset}")
	private String count;

	@Value("${ptab.trail.file.path}")
	private String filePath;

	@Value("${ignored.type}")
	private String ignoredType;
	
	@Value("${ptab.trail.totalcount}")
	private String totalcount;
	
	@Value("${post.request.payload}")
	private String postRequestPayload;
	
	@Value("${post.url}")
	private String postUrl;
	
	@Autowired
	private FileProcessor fileProcess;
	
	
	@Autowired
private StanfordNLPProcessor stndFordNLP;
	
	@Autowired
private OpenNLPProcessor openNLP;

	public void getTrails() {
		System.out.print(totalcount);
		
		int totCount = Integer.valueOf(totalcount);
		
		int offSetI = Integer.valueOf(count);
		int limitI = Integer.valueOf(limit);
		System.out.println(totCount);
		for (int i = 0; i <= totCount;) {
			System.out.println(limitI);
			System.out.println(offSetI);
			
			getTrailDetails(String.valueOf(offSetI),limitI);
			offSetI = offSetI+limitI;
		}
	}

	public void getTrailDetails(String offSet, int limitI) {
		
		final RestTemplate restTemplate = new RestTemplate();
		System.out.println("ddddd"+url + "offset=" + offSet + "&limit=" + limit);
		final CollectionPayload collectionPayload = restTemplate
				.getForObject(url + "offset=" + offSet + "&limit=" + limitI, CollectionPayload.class);
		final List<PtabTrail> list = collectionPayload.getResults();
		System.out.println("lsitsize"+list.size());
		for (final PtabTrail ptabTrail : list) {
			System.out.println("ddddd"+ptabTrail.getTrialNumber());
			final ObjectMapper mapper = new ObjectMapper();
			final String folderName = ptabTrail.getTrialNumber();
			final String filename = ptabTrail.getTrialNumber() + ".json";
			final String filename2 = ptabTrail.getPatentNumber() + ".json";
			String result=postRequestPayload.replace("NumberTobeReplaced", ptabTrail.getPatentNumber());
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> request = new HttpEntity<>(result, headers);
			
			String response=restTemplate.postForObject(postUrl, request, String.class);
			System.out.println(response);
			final File file = new File(filePath + folderName + "//" + filename);
			final File file2 = new File(filePath + folderName + "//" + filename2);
			file.getParentFile().mkdirs();
			try {
				mapper.writeValue(file, ptabTrail);
				FileUtils.writeStringToFile(file2, response);
				//mapper.writeValue(file2, response);
			} catch (final IOException e) {
				e.printStackTrace();
			}
			final List<LinkDetails> linkDetailsList = ptabTrail.getLinks();
			for (final LinkDetails linkDetails : linkDetailsList) {
				getMetaDataInfo(restTemplate, folderName, linkDetails);
			}
			try {
			List<Map> contentMap = fileProcess.processFiles(filePath + folderName);
			
			FileWriter fout;
			
				fout = new FileWriter(new File(filePath + folderName+"/NLPData"));
			
			for (Map indvMap : contentMap)
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
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void getMetaDataInfo(final RestTemplate restTemplate, final String folderName,
			final LinkDetails linkDetails) {
		if (linkDetails.getRel().equalsIgnoreCase("documents")) {
			final String documentsUrl = linkDetails.getHref();
			final CollectionPayloadDocuments collectionPayloadDocuments = restTemplate.getForObject(documentsUrl,
					CollectionPayloadDocuments.class);
			final List<DocumentsMetaData> documentsMetaDatalist = collectionPayloadDocuments.getResults();
			for (final DocumentsMetaData documentsMetaData : documentsMetaDatalist) {
				if (documentsMetaData.getType()!= null && !documentsMetaData.getType().equalsIgnoreCase(ignoredType)) {
					downloadTrailDocument(restTemplate, folderName, documentsMetaData);
				}
			}
		}
	}

	private void downloadTrailDocument(final RestTemplate restTemplate, final String folderName,
			final DocumentsMetaData documentsMetaData) {
		final String fileNameForPdf = documentsMetaData.getTrialNumber() + "_" + documentsMetaData.getDocumentNumber()+"+"+documentsMetaData.getFilingParty();
		final List<LinkDetails> documentlinkDetailsList = documentsMetaData.getLinks();
		for (final LinkDetails documentLinkDetails : documentlinkDetailsList) {
			if (documentLinkDetails.getRel().equalsIgnoreCase("download")) {
				final String downloadUrl = documentLinkDetails.getHref();
				final RestTemplate restTemplateDownload = new RestTemplate();
				restTemplateDownload.getMessageConverters().add(new ByteArrayHttpMessageConverter());
				final HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
				final HttpEntity<String> entity = new HttpEntity<String>(headers);
				generatePdf(restTemplate, folderName, fileNameForPdf, downloadUrl, entity);
			}

		}
	}

	private void generatePdf(final RestTemplate restTemplate, final String folderName, final String fileNameForPdf,
			final String downloadUrl, final HttpEntity<String> entity) {
		final ResponseEntity<byte[]> response = restTemplate.exchange(downloadUrl, HttpMethod.GET, entity,
				byte[].class);
		if (response.getStatusCode() == HttpStatus.OK) {
			try {
				Files.write(Paths.get(filePath + folderName + "//" + fileNameForPdf + ".pdf"), response.getBody());
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}
}
