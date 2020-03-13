package com.ptab.sky.nlp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
@Component
@Qualifier("stanfordNLPProcessor")
public class StanfordNLPLoad  {
	 StanfordCoreNLP pipeline =  new StanfordCoreNLP(
				PropertiesUtils.asProperties("annotators", "tokenize, ssplit, pos, lemma, ner"));;
	public Map<String,Object> getEntities(String inText) {
		//init();
		//System.out.println(inText);
		inText = StringUtils.replace(inText, "-", " ");
		inText = StringUtils.replace(inText, ".", " ");
		Map<String,Object> outMap = new HashMap();
		System.out.println(inText);
		
		
		 
		
		
		
		// create an empty Annotation just with the given text
	    Annotation document = new Annotation(inText);

	    // run all Annotators on this text
	    pipeline.annotate(document);
	
	  

	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	   boolean stich =false;
	   String person = "";
	   String org="";
	   String loc="";
	   String date = "";
	   String amount="";
	   String percent="";
	   String period="";
	   String duartion="";
	   String misc="";
	   
	   List<String> personlist = new ArrayList<>();
       List<String> orglist = new ArrayList<>();
       List<String> loclist = new ArrayList<>();
       List<String> datelist = new ArrayList<>();
       List<String> amountlist = new ArrayList<>();
       List<String> percentlist = new ArrayList<>();
       List<String> periodlist = new ArrayList<>();
       List<String> durationlist = new ArrayList<>();
       List<String> misclist = new ArrayList<>();
	    for(CoreMap sentence: sentences) {
	    	
	    	
	    	// Set normalized named entity for chunked tokens
            if (sentence.containsKey(CoreAnnotations.NumerizedTokensAnnotation.class)) {
                for (CoreMap numerizedToken : sentence.get(CoreAnnotations.NumerizedTokensAnnotation.class)) {
                    if (numerizedToken.containsKey(CoreAnnotations.TokensAnnotation.class)) {
                        // The normalized named entity got deleted?
                        numerizedToken.set(CoreAnnotations.NormalizedNamedEntityTagAnnotation.class, numerizedToken.get(CoreAnnotations.TokensAnnotation.class).get(0).get(CoreAnnotations.NormalizedNamedEntityTagAnnotation.class));
                        // The named entity type got deleted?
                        numerizedToken.set(CoreAnnotations.NamedEntityTagAnnotation.class, numerizedToken.get(CoreAnnotations.TokensAnnotation.class).get(0).get(CoreAnnotations.NamedEntityTagAnnotation.class));
                    }
                }
            }
        
    
	    	
	    	
	    
	      // traversing the words in the current sentence
	      // a CoreLabel is a CoreMap with additional token-specific methods
	      for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	        // this is the text of the token
	    	
	    	 
	        String word = token.get(TextAnnotation.class);
	        // this is the POS tag of the token
	      //  String pos = token.get(PartOfSpeechAnnotation.class);
	        // this is the NER label of the token
	        String ne = token.get(NamedEntityTagAnnotation.class);
	       
	        
	    	if ("PERSON".equals(ne) ||"MISC".equals(ne) || "LOCATION".equals(ne) || "ORGANIZATION".equals(ne) || "DATE".equals(ne) ||  "MONEY".equals(ne) || "PERCENT".equals(ne) || "TIME".equals(ne) || "DURATION".equals(ne) )
	    			{
	    		
	    		stich=true;
	    		if ("PERSON".equals(ne) )
	    		{
	    			person+=" "+word;
	    		}
	    		if ("LOCATION".equals(ne) )
	    		{
	    			loc+=" "+word;
	    		}
	    		if ("ORGANIZATION".equals(ne) )
	    		{
	    			org+=" "+word;
	    		}
	    		if ("DATE".equals(ne) )
	    		{
	    			date+=" "+word;
	    		}
	    		if ("MONEY".equals(ne) )
	    		{
	    			amount+=" "+word;
	    		}
	    		if ("PERCENT".equals(ne) )
	    		{
	    			percent+=" "+word;
	    		}
	    		if ("TIME".equals(ne) )
	    		{
	    			period+=" "+word;
	    		}
	    		if ("DURATION".equals(ne) )
	    		{
	    			duartion+=" "+word;
	    		}
	    		if ("MISC".equals(ne) )
	    		{
	    			misc+=" "+word;
	    		}
	    		
	    		// System.out.println("word: " + word + " pos: " + pos + " ne:" + ne);
	    			}
	    	else
	    	{
	    		
	    		if (StringUtils.isNotBlank(person)) {
	    			personlist.add("\""+person.trim()+"\"");
	    		}
	    		if (StringUtils.isNotBlank(loc)) {
	    			loclist.add("\""+loc.trim()+"\"");
	    		}
	    		if (StringUtils.isNotBlank(org)) {
	    			orglist.add("\""+org.trim()+"\"");
	    		}
	    		
	    		if (StringUtils.isNotBlank(date)) {
	    			datelist.add("\""+date.trim()+"\"");
	    		}
	    		if (StringUtils.isNotBlank(amount)) {
	    			amountlist.add("\""+amount.trim()+"\"");
	    		}
	    		if (StringUtils.isNotBlank(percent)) {
	    			percentlist.add("\""+percent.trim()+"\"");
	    		}
	    		if (StringUtils.isNotBlank(period)) {
	    			periodlist.add("\""+period.trim()+"\"");
	    		}
	    		if (StringUtils.isNotBlank(duartion)) {
	    			durationlist.add("\""+duartion.trim()+"\"");
	    		}
	    		if (StringUtils.isNotBlank(misc)) {
	    			misclist.add("\""+misc.trim()+"\"");
	    		}
	    		
	    	
	    		person="";
	    		loc="";
	    		org="";
	    		  date = "";
	    		    amount="";
	    		   percent="";
	    		    period="";
	    		    misc="";
	    		    duartion="";
	    		stich=false;
	    	}
	        
	       
	      }
	    }
	    
	   if (!orglist.isEmpty())
	   {
		   outMap.put("stanford-organization", orglist.stream().distinct().collect(Collectors.toList()));
	   }
	   
	   if (!loclist.isEmpty())
	   {
		   outMap.put("stanford-location", loclist.stream().distinct().collect(Collectors.toList()));
	   }
	   
	   if (!personlist.isEmpty())
	   {
		   outMap.put("stanford-person", personlist.stream().distinct().collect(Collectors.toList()));
	   }
	   if (!datelist.isEmpty())
	   {
		   outMap.put("stanford-date", datelist.stream().distinct().collect(Collectors.toList()));
	   }
	   if (!amountlist.isEmpty())
	   {
		   outMap.put("stanford-amount", amountlist.stream().distinct().collect(Collectors.toList()));
	   }
	   if (!percentlist.isEmpty())
	   {
		   outMap.put("stanford-percent", percentlist.stream().distinct().collect(Collectors.toList()));
	   }
	   if (!periodlist.isEmpty())
	   {
		   outMap.put("stanford-period", periodlist.stream().distinct().collect(Collectors.toList()));
	   }
	   if (!durationlist.isEmpty())
	   {
		   outMap.put("stanford-duration", durationlist.stream().distinct().collect(Collectors.toList()));
	   }
	   if (!misclist.isEmpty())
	   {
		   outMap.put("stanford-misc", misclist.stream().distinct().collect(Collectors.toList()));
	   }
	    
	   
	    
		//System.out.println(orglist.stream().distinct().collect(Collectors.toList()));
		//System.out.println(loclist.stream().distinct().collect(Collectors.toList()));
		//System.out.println(personlist.stream().distinct().collect(Collectors.toList()));
	    
		return outMap;
	}

	

	
	void init ()
	{
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
	   
	
		pipeline = new StanfordCoreNLP(
				PropertiesUtils.asProperties("annotators", "tokenize, ssplit, pos, lemma, ner"));

	  //pipeline = new StanfordCoreNLP(
		//		PropertiesUtils.asProperties("annotators", "tokenize, ssplit, pos, lemma, ner","tokenize.options","splitHyphenated=true"));
	   
	    
	}


}
