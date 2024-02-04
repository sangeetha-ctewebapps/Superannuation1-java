package com.lic.epgs.common.dto;

import java.util.Arrays;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class SearchDepositTransferDto {
	
	
	private static final long serialVersionUID = 1L;
	
	
	
	
	private String policyNumber;
	private String quotationNumber;
	private String proposalNumber;
	private long mphId;
	private String mphCode;

	private String policyCreationFromDate;
	private String policyCreationToDate;
	private String customerCode;
	private String panNumber;
	private String gstIn;
	private String unitCode;
	private String unitCodeArray[];
	private String vanNumber;
	private String roleType;
	
	
//	private String policyNumber;
	private String mphName;
//	private String mphCode;
	private String product;
//	private String unitCode;
	private String customerId;
	private String policyStatus;
	private String ifscCode;
	private String accountType;
	private String bankName;
	private String bankBranch;
	private String productCode;
	private String schemeName;
	private String variantCode;
//	private String productName;
	
	
//
//	public String getPolicyNumber() {
//		return policyNumber;
//	}
//	public void setPolicyNumber(String policyNumber) {
//		this.policyNumber = policyNumber;
//	}
//	public Integer getQuotationNumber() {
//		return quotationNumber;
//	}
//	public void setQuotationNumber(Integer quotationNumber) {
//		this.quotationNumber = quotationNumber;
//	}
//	public Integer getProposalNumber() {
//		return proposalNumber;
//	}
//	public void setProposalNumber(Integer proposalNumber) {
//		this.proposalNumber = proposalNumber;
//	}
//	
//	public String getMphCode() {
//		return mphCode;
//	}
//	public void setMphCode(String mphCode) {
//		this.mphCode = mphCode;
//	}
//	public Date getPolicyCreationFromDate() {
//		return policyCreationFromDate;
//	}
//	public void setPolicyCreationFromDate(Date policyCreationFromDate) {
//		this.policyCreationFromDate = policyCreationFromDate;
//	}
//	public Date getPolicyCreationToDate() {
//		return policyCreationToDate;
//	}
//	public void setPolicyCreationToDate(Date policyCreationToDate) {
//		this.policyCreationToDate = policyCreationToDate;
//	}
//	public String getCustomerCode() {
//		return customerCode;
//	}
//	public void setCustomerCode(String customerCode) {
//		this.customerCode = customerCode;
//	}
//	public String getPanNumber() {
//		return panNumber;
//	}
//	public void setPanNumber(String panNumber) {
//		this.panNumber = panNumber;
//	}
//	public String getGstIn() {
//		return gstIn;
//	}
//	public void setGstIn(String gstIn) {
//		this.gstIn = gstIn;
//	}
//	public String getUnitCode() {
//		return unitCode;
//	}
//	public void setUnitCode(String unitCode) {
//		this.unitCode = unitCode;
//	}
//	public String[] getUnitCodeArray() {
//		return unitCodeArray;
//	}
//	public void setUnitCodeArray(String[] unitCodeArray) {
//		this.unitCodeArray = unitCodeArray;
//	}

	
	
	
//	private String policyNumber;
//	private String customerCode;
//	private String mphCode;
//	private String vannumber;
//	private String proposalNumber;
//	private String quotationNumber;
//	private String panNumber;
//	private String gstIn;
//	private String policyCreationFromDate;
//	private String policyCreationToDate;
//	private String mph;
//	private String roleType;
//	
//	
//	
//	private String mphName;
////	private String mphCode;
//	private String product;
//	private String unitCode;
//	private String customerId;
//	private String policyStatus;
//	private String ifscCode;
//	private String accountType;
//	private String bankName;
//	private String bankBranch;
//	private String productCode;
//	private String schemeName;
//	private String variantCode;
//	
	
	
	          

	
	
	          
}
