package com.ptab.sky.domain;

import java.util.List;

public class DocumentsMetaData {
	private String title;
	private String filingDatetime;
	private String documentNumber;
	private String sizeInBytes;
	private String trialNumber;
	private String lastModifiedDatetime;
	private String filingParty;
	private String mediaType;
	private String id;
	private String type;

	private List<LinkDetails> links;

	public DocumentsMetaData() {
		super();
	}

	public DocumentsMetaData(String title, String filingDatetime, String documentNumber, String sizeInBytes,
			String trialNumber, String lastModifiedDatetime, String filingParty, String mediaType, String id,
			String type, List<LinkDetails> links) {
		super();
		this.title = title;
		this.filingDatetime = filingDatetime;
		this.documentNumber = documentNumber;
		this.sizeInBytes = sizeInBytes;
		this.trialNumber = trialNumber;
		this.lastModifiedDatetime = lastModifiedDatetime;
		this.filingParty = filingParty;
		this.mediaType = mediaType;
		this.id = id;
		this.type = type;
		this.links = links;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFilingDatetime() {
		return filingDatetime;
	}

	public void setFilingDatetime(String filingDatetime) {
		this.filingDatetime = filingDatetime;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getSizeInBytes() {
		return sizeInBytes;
	}

	public void setSizeInBytes(String sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}

	public String getTrialNumber() {
		return trialNumber;
	}

	public void setTrialNumber(String trialNumber) {
		this.trialNumber = trialNumber;
	}

	public String getLastModifiedDatetime() {
		return lastModifiedDatetime;
	}

	public void setLastModifiedDatetime(String lastModifiedDatetime) {
		this.lastModifiedDatetime = lastModifiedDatetime;
	}

	public String getFilingParty() {
		return filingParty;
	}

	public void setFilingParty(String filingParty) {
		this.filingParty = filingParty;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<LinkDetails> getLinks() {
		return links;
	}

	public void setLinks(List<LinkDetails> links) {
		this.links = links;
	}

}
