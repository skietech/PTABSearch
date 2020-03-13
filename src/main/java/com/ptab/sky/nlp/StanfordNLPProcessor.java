package com.ptab.sky.nlp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import lombok.extern.slf4j.Slf4j;

@Component(value = "stanfordNLPProcessor")
@Slf4j
public class StanfordNLPProcessor implements NLPProcessor {
	StanfordCoreNLP pipeline;

	@Override
	public Map<String, Object> getEntities(String inText) {
		init();

		final Map<String, Object> outMap = new HashMap();
		StanfordNLPProcessor.log.info(inText);
		// create an empty Annotation just with the given text
		final Annotation document = new Annotation(inText);

		// run all Annotators on this text
		pipeline.annotate(document);

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and has values
		// with custom types
		final List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		String person = "";
		String org = "";
		String loc = "";
		final List<String> personlist = new ArrayList<>();
		final List<String> orglist = new ArrayList<>();
		final List<String> loclist = new ArrayList<>();
		for (final CoreMap sentence : sentences)
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (final CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				final String word = token.get(TextAnnotation.class);
				// this is the POS tag of the token
				final String pos = token.get(PartOfSpeechAnnotation.class);
				// this is the NER label of the token
				final String ne = token.get(NamedEntityTagAnnotation.class);

				if ("PERSON".equals(ne) || "LOCATION".equals(ne) || "ORGANIZATION".equals(ne)) {

					if ("PERSON".equals(ne))
						person += " " + word;
					if ("LOCATION".equals(ne))
						loc += " " + word;
					if ("ORGANIZATION".equals(ne))
						org += " " + word;

					StanfordNLPProcessor.log.info("word: " + word + " pos: " + pos + " ne:" + ne);
				} else {

					if (StringUtils.isNotBlank(person))
						personlist.add(person.trim());
					if (StringUtils.isNotBlank(loc))
						loclist.add(loc.trim());
					if (StringUtils.isNotBlank(org))
						orglist.add(org.trim());

					person = "";
					loc = "";
					org = "";
				}

			}

		if (!orglist.isEmpty())
			outMap.put("stanford-organization", orglist.stream().distinct().collect(Collectors.toList()));

		if (!loclist.isEmpty())
			outMap.put("stanford-location", loclist.stream().distinct().collect(Collectors.toList()));

		if (!personlist.isEmpty())
			outMap.put("stanford-person", personlist.stream().distinct().collect(Collectors.toList()));

		// log.info(orglist.stream().distinct().collect(Collectors.toList()));
		// log.info(loclist.stream().distinct().collect(Collectors.toList()));
		// log.info(personlist.stream().distinct().collect(Collectors.toList()));

		return outMap;
	}

	void init() {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER,
		// parsing, and coreference resolution
		final Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner");

		pipeline = new StanfordCoreNLP(props);

	}

	@Override
	public List<String> getPersonEntities(String inText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getOrganizationEntities(String inText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getLocationEntities(String inText) {
		// TODO Auto-generated method stub
		return null;
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

}
