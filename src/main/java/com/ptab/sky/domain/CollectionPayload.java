package com.ptab.sky.domain;

import java.util.List;

public class CollectionPayload {
	
	
	private List<PtabTrail> results;

	public CollectionPayload() {
		super();
	}

	public CollectionPayload(List<PtabTrail> results) {
		super();
		this.results = results;
	}

	public List<PtabTrail> getResults() {
		return results;
	}

	public void setResults(List<PtabTrail> results) {
		this.results = results;
	}
}
