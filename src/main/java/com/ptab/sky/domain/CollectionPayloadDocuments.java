package com.ptab.sky.domain;

import java.util.List;

public class CollectionPayloadDocuments {

	private List<DocumentsMetaData> results;

	
	public CollectionPayloadDocuments() {
		super();
	}

	public CollectionPayloadDocuments(List<DocumentsMetaData> results) {
		super();
		this.results = results;
	}

	public List<DocumentsMetaData> getResults() {
		return results;
	}

	public void setResults(List<DocumentsMetaData> results) {
		this.results = results;
	}
	
	
}
