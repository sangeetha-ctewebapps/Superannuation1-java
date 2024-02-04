package com.lic.epgs.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class DepositTransferResponse {
	
	private String policyNumber;
	private String mphName;
	private String mphCode;
	private String product;
	private String unitCode;
	private String customerId;
	private String policyStatus;
	private String ifscCode;
	private String accountType;
	private String bankName;
	private String bankBranch;
	private String productCode;
	private String schemeName;
//	private String productName;
	
//	public String getProductName() {
//		return productName;
//	}
//	public void setProductName(String productName) {
//		this.productName = productName;
//	}
	public String getMphId() {
		return mphId;
	}
	public void setMphId(String mphId) {
		this.mphId = mphId;
	}




	private String variantCode;
	private String mphId;
	
//	private String transactionMessage;
//	private String transactionStatus;
	
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getMphName() {
		return mphName;
	}
	public void setMphName(String mphName) {
		this.mphName = mphName;
	}
	public String getMphCode() {
		return mphCode;
	}
	public void setMphCode(String mphCode) {
		this.mphCode = mphCode;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getPolicyStatus() {
		return policyStatus;
	}
	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankBranch() {
		return bankBranch;
	}
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getVariantCode() {
		return variantCode;
	}
	public void setVariantCode(String variantCode) {
		this.variantCode = variantCode;
	}
	
	
	
	
	@Override
	public String toString() {
		return "DepositTransferResponse [policyNumber=" + policyNumber + ", mphName=" + mphName + ", mphCode=" + mphCode
				+ ", product=" + product + ", unitCode=" + unitCode + ", customerId=" + customerId + ", policyStatus="
				+ policyStatus + ", ifscCode=" + ifscCode + ", accountType=" + accountType + ", bankName=" + bankName
				+ ", bankBranch=" + bankBranch + ", productCode=" + productCode + ", schemeName=" + schemeName
				+ ", variantCode=" + variantCode + "]";
	}

}
