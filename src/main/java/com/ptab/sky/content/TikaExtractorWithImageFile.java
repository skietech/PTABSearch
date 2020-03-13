package com.ptab.sky.content;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
//@PropertySource("classpath:SearchJsonAttributes.properties")
public class TikaExtractorWithImageFile implements ContentExtractor {

	@Autowired
	private Environment env;

	/*
	 * @Autowired
	 * 
	 * @Qualifier(value = "openNLPProcessor") private OpenNLPProcessor nlpProcessor;
	 * 
	 * @Autowired DTRALocationUtils dtraLoc;
	 */

	@Override
	public Map<String, Object> extractTextContent(File inFile) {
		Map<String, Object> outMap = null;

		try {
			// TikaConfig config = new
			// TikaConfig("/Users/raviramani/Documents/NLPModels/tika-config.xml");

			final AutoDetectParser parser = new AutoDetectParser();
			final BodyContentHandler handler = new BodyContentHandler(-1);
			final Metadata metadata = new Metadata();

			final InputStream stream = new FileInputStream(inFile);
			parser.parse(stream, handler, metadata);
			outMap = processMetaData(metadata);
			String originalContent = handler.toString();

			if (StringUtils.containsIgnoreCase(inFile.getName(), "summary"))
				;
			{
				// processSummaryText(originalContent);

				outMap.putAll(processSummaryText(originalContent));

				originalContent = originalContent.replaceAll("\\s", " ");

				/*
				 * // originalContent = originalContent.replaceAll("\t", " "); List<String>
				 * namedList = new ArrayList<>(); List<String> orgList = new ArrayList<>();
				 * List<String> loclist = new ArrayList<>(); namedList =
				 * nlpProcessor.getPersonEntities(originalContent);
				 *
				 *
				 * outMap.put("openperson", namedList);
				 *
				 *
				 * loclist = nlpProcessor.getLocationEntities(originalContent);
				 *
				 *
				 * outMap.put("openlocation", loclist);
				 *
				 * orgList = nlpProcessor.getOrganizationEntities(originalContent);
				 *
				 *
				 * outMap.put("openorganization", orgList);
				 */

				outMap.put("originalContent", originalContent);

			}

			if (StringUtils.isBlank(originalContent)) {

				final String extractedText = processImageFiles(inFile);

				outMap.put("originalContent", extractedText);

			}

		} catch (IOException | SAXException | TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return outMap;
	}

	private Map<String, Object> processSummaryText(String originalContent) {

		final Map<String, Object> summaryMetaMap = new HashMap<>();
		final List<String> tokenizedOriginal = Arrays.asList(StringUtils.split(originalContent, "\n"));
		final String summaryLooksUps = env.getProperty("Summary.Text.LookUp");

		final List<String> summaryLookList = Arrays.asList(summaryLooksUps.split(";"));
		for (final String indvToken : tokenizedOriginal)
			for (final String indvSummaryLookUp : summaryLookList)
				if (StringUtils.contains(indvToken, indvSummaryLookUp)) {

					if (indvSummaryLookUp.equals("Description:"))
						summaryMetaMap.put("description",
								originalContent.substring(originalContent.indexOf("Description:") + 12,
										originalContent.indexOf("From:")).replaceAll("\\s", " "));
					else
						summaryMetaMap.put(indvToken.substring(0, indvToken.indexOf(":")),
								indvToken.substring(indvToken.indexOf(":") + 1));

					break;
				}
		return summaryMetaMap;

	}

	@Override
	public Map<String, Object> extractIMmageContent(File inFile) {
		// TODO Auto-generated method stub
		return null;
	}

	private Map<String, Object> processMetaData(Metadata md) {

		final Map<String, Object> metaMap = new HashMap<>();
		final String configuredAttributes = env.getProperty("JSON.INCLUDE.ATTRIBUTES");

		for (final String name : md.names()) {

			if (configuredAttributes.contains(name))
				if (md.isMultiValued(name))
					metaMap.put(env.getProperty(name + ".JSON.label"), Arrays.asList(md.getValues(name)));
				else
					metaMap.put(env.getProperty(name + ".JSON.label"), md.get(name));

		}
		return metaMap;
	}

	private String processImageFiles(File inFile) throws InterruptedException, IOException {

		env.getProperty("DOC.CONVERT.PDF.TO.TIFF.COMMNAD");

		ProcessBuilder pb = new ProcessBuilder("/opt/local/bin/convert", "-density", "300", inFile.getPath(), "-depth", "8",
				"-strip", "-background", "white", "-alpha", "off", inFile.getPath() + ".tiff");

		pb.redirectErrorStream(true);
		final Map<String, String> env = pb.environment();
		final String path = env.get("PATH") + ":/opt/local/lib/ImageMagick-6.9.9";
		env.put("PATH", path);

		Process p = pb.start();
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		while ((line = br.readLine()) != null)

			System.out.println(p.waitFor());

		if (new File(inFile.getPath() + ".tiff").isFile()) {
			pb = new ProcessBuilder("/usr/local/bin/tesseract", "-l", "eng", "--tessdata-dir",
					"/usr/local/share/tessdata", inFile.getPath() + ".tiff", inFile.getPath() + ".tiff");

			pb.redirectErrorStream(true);

			p = pb.start();
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			line = null;

			System.out.println(p.waitFor());
		}

		return IOUtils.readStringFromStream(new FileInputStream(new File(inFile.getPath() + ".tiff.txt")));

	}

}
