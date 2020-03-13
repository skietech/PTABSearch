package com.ptab.sky.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class PtabTrail {

	private String patentOwnerName;
	private String prosecutionStatus;
	private String patentNumber;
	private Date accordedFilingDate;
	private Date institutionDecisionDate;
	private String petitionerPartyName;
	private Date lastModifiedDatetime;
	private String inventorName;
	private Date filingDate;
	private String applicationNumber;
	private String trialNumber;

	@JsonProperty(access = Access.WRITE_ONLY)
	private List<LinkDetails> links;

	public PtabTrail() {
		super();
	}

	public PtabTrail(String patentOwnerName, String prosecutionStatus, String patentNumber, Date accordedFilingDate,
			Date institutionDecisionDate, String petitionerPartyName, Date lastModifiedDatetime, String inventorName,
			Date filingDate, String applicationNumber, String trialNumber, List<LinkDetails> links) {
		super();
		this.patentOwnerName = patentOwnerName;
		this.prosecutionStatus = prosecutionStatus;
		this.patentNumber = patentNumber;
		this.accordedFilingDate = accordedFilingDate;
		this.institutionDecisionDate = institutionDecisionDate;
		this.petitionerPartyName = petitionerPartyName;
		this.lastModifiedDatetime = lastModifiedDatetime;
		this.inventorName = inventorName;
		this.filingDate = filingDate;
		this.applicationNumber = applicationNumber;
		this.trialNumber = trialNumber;
		this.links = links;
	}

	public String getPatentOwnerName() {
		return patentOwnerName;
	}

	public void setPatentOwnerName(String patentOwnerName) {
		this.patentOwnerName = patentOwnerName;
	}

	public String getProsecutionStatus() {
		return prosecutionStatus;
	}

	public void setProsecutionStatus(String prosecutionStatus) {
		this.prosecutionStatus = prosecutionStatus;
	}

	public String getPatentNumber() {
		return patentNumber;
	}

	public void setPatentNumber(String patentNumber) {
		this.patentNumber = patentNumber;
	}

	public Date getAccordedFilingDate() {
		return accordedFilingDate;
	}

	public void setAccordedFilingDate(Date accordedFilingDate) {
		this.accordedFilingDate = accordedFilingDate;
	}

	public Date getInstitutionDecisionDate() {
		return institutionDecisionDate;
	}

	public void setInstitutionDecisionDate(Date institutionDecisionDate) {
		this.institutionDecisionDate = institutionDecisionDate;
	}

	public String getPetitionerPartyName() {
		return petitionerPartyName;
	}

	public void setPetitionerPartyName(String petitionerPartyName) {
		this.petitionerPartyName = petitionerPartyName;
	}

	public Date getLastModifiedDatetime() {
		return lastModifiedDatetime;
	}

	public void setLastModifiedDatetime(Date lastModifiedDatetime) {
		this.lastModifiedDatetime = lastModifiedDatetime;
	}

	public String getInventorName() {
		return inventorName;
	}

	public void setInventorName(String inventorName) {
		this.inventorName = inventorName;
	}

	public Date getFilingDate() {
		return filingDate;
	}

	public void setFilingDate(Date filingDate) {
		this.filingDate = filingDate;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public String getTrialNumber() {
		return trialNumber;
	}

	public void setTrialNumber(String trialNumber) {
		this.trialNumber = trialNumber;
	}

	public List<LinkDetails> getLinks() {
		return links;
	}

	public void setLinks(List<LinkDetails> links) {
		this.links = links;
	}

}
