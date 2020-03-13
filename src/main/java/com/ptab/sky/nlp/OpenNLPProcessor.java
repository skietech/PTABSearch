package com.ptab.sky.nlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

@Component
@Slf4j
@Qualifier("openNLPProcessor")
@PropertySource("classpath:SearchJsonAttributes.properties")
public class OpenNLPProcessor implements NLPProcessor {

	static InputStream personModelIn = null;;
	static InputStream locationModelIn = null;
	static InputStream orgModelIn = null;
	static InputStream modelInToken = null;
	static TokenizerModel modelToken;

	@Autowired
	private Environment env;

	@Override
	public List<String> getPersonEntities(String inText) {
		List<String> personList = new ArrayList<>();

		try {

			final String[] tokens = tokenizeInText(inText);
			// 2. find names

			OpenNLPProcessor.personModelIn = new FileInputStream(env.getProperty("NLP.PERSON.MODEL.LOC"));

			personList = extractEntitiesFromToken(tokens, OpenNLPProcessor.personModelIn);

			personList.removeIf(u -> u.length() < 2);
		} catch (final Exception ex) {
			ex.printStackTrace();
		} finally {

		}
		return new ArrayList<>(new HashSet<>(personList));
	}

	@Override
	public List<String> getOrganizationEntities(String inText) {
		List<String> orgList = new ArrayList<>();

		try {

			final String[] tokens = tokenizeInText(inText);
			// 2. find names

			OpenNLPProcessor.orgModelIn = new FileInputStream(env.getProperty("NLP.ORG.MODEL.LOC"));
			OpenNLPProcessor.log.info("locading");

			orgList = extractEntitiesFromToken(tokens, OpenNLPProcessor.orgModelIn);

			orgList.removeIf(u -> u.length() < 2);
		} catch (final Exception ex) {
			ex.printStackTrace();
		} finally {

		}
		return new ArrayList<>(new HashSet<>(orgList));
	}

	@Override
	public List<String> getLocationEntities(String inText) {
		List<String> locationList = new ArrayList<>();

		try {

			final String[] tokens = tokenizeInText(inText);
			// 2. find names

			OpenNLPProcessor.locationModelIn = new FileInputStream(env.getProperty("NLP.LOC.MODEL.LOC"));

			locationList = extractEntitiesFromToken(tokens, OpenNLPProcessor.locationModelIn);

			locationList.removeIf(u -> u.length() < 2);
		} catch (final Exception ex) {
			ex.printStackTrace();
		} finally {

		}
		return new ArrayList<>(new HashSet<>(locationList));
	}

	@Override
	public List<String> getEventsEntities(String inText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getDatesEntities(String inText) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 * @param inText
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	private String[] tokenizeInText(String inText) {

		if (OpenNLPProcessor.modelInToken == null)
			try {
				OpenNLPProcessor.modelInToken = new FileInputStream(env.getProperty("NLP.TOKEN.MODEL.LOC"));
				OpenNLPProcessor.modelToken = new TokenizerModel(OpenNLPProcessor.modelInToken);
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		final Tokenizer tokenizer = new TokenizerME(OpenNLPProcessor.modelToken);
		return tokenizer.tokenize(inText);
	}

	/**
	 *
	 * @param tokenizeSenTence
	 * @param modelIn
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	private List<String> extractEntitiesFromToken(String[] tokenizeSenTence, InputStream modelIn) {
		final List<String> entityList = new ArrayList<>();

		try {
			final TokenNameFinderModel model = new TokenNameFinderModel(modelIn);

			final NameFinderME nameFinder = new NameFinderME(model);

			final Span nameSpans[] = nameFinder.find(tokenizeSenTence);

			for (final Span nameSpan : nameSpans)
				entityList.add(tokenizeSenTence[nameSpan.getStart()]);
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// find probabilities for names
		// double[] spanProbs = nameFinder.probs(nameSpans);

		return entityList;
	}

	@Override
	public Map<String, Object> getEntities(String inText) {
		// TODO Auto-generated method stub
		return null;
	}

}
