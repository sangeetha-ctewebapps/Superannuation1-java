package com.lic.epgs.gst.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GstCalculationResponse {
	private BigDecimal cGstRate;
	private BigDecimal cGstRateAmount;
	private BigDecimal sGstRate;
	private BigDecimal sGstRateAmount;
	private BigDecimal iGstRate;
	private BigDecimal iGstRateAmount;
	private BigDecimal utGstRate;
	private BigDecimal utGstRateAmount;
	private BigDecimal totalGstRate;
	private BigDecimal totalGstAmount;
	private String fromGstStateCode;
	private String toGstStateCode;
	private BigDecimal taxablePercengtage;
	
	private String message;
	private Integer resultCode;
	
	public BigDecimal getcGstRate() {
		return cGstRate;
	}

	public void setcGstRate(BigDecimal cGstRate) {
		this.cGstRate = cGstRate;
	}

	public BigDecimal getcGstRateAmount() {
		return cGstRateAmount;
	}

	public void setcGstRateAmount(BigDecimal cGstRateAmount) {
		this.cGstRateAmount = cGstRateAmount;
	}

	public BigDecimal getsGstRate() {
		return sGstRate;
	}

	public void setsGstRate(BigDecimal sGstRate) {
		this.sGstRate = sGstRate;
	}

	public BigDecimal getsGstRateAmount() {
		return sGstRateAmount;
	}

	public void setsGstRateAmount(BigDecimal sGstRateAmount) {
		this.sGstRateAmount = sGstRateAmount;
	}

	public BigDecimal getiGstRate() {
		return iGstRate;
	}

	public void setiGstRate(BigDecimal iGstRate) {
		this.iGstRate = iGstRate;
	}

	public BigDecimal getiGstRateAmount() {
		return iGstRateAmount;
	}

	public void setiGstRateAmount(BigDecimal iGstRateAmount) {
		this.iGstRateAmount = iGstRateAmount;
	}

	public BigDecimal getUtGstRate() {
		return utGstRate;
	}

	public void setUtGstRate(BigDecimal utGstRate) {
		this.utGstRate = utGstRate;
	}

	public BigDecimal getUtGstRateAmount() {
		return utGstRateAmount;
	}

	public void setUtGstRateAmount(BigDecimal utGstRateAmount) {
		this.utGstRateAmount = utGstRateAmount;
	}

	public BigDecimal getTotalGstRate() {
		return totalGstRate;
	}

	public void setTotalGstRate(BigDecimal totalGstRate) {
		this.totalGstRate = totalGstRate;
	}

	public BigDecimal getTotalGstAmount() {
		return totalGstAmount;
	}

	public void setTotalGstAmount(BigDecimal totalGstAmount) {
		this.totalGstAmount = totalGstAmount;
	}

	public String getFromGstStateCode() {
		return fromGstStateCode;
	}

	public void setFromGstStateCode(String fromGstStateCode) {
		this.fromGstStateCode = fromGstStateCode;
	}

	public String getToGstStateCode() {
		return toGstStateCode;
	}

	public void setToGstStateCode(String toGstStateCode) {
		this.toGstStateCode = toGstStateCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public BigDecimal getTaxablePercengtage() {
		return taxablePercengtage;
	}

	public void setTaxablePercengtage(BigDecimal taxablePercengtage) {
		this.taxablePercengtage = taxablePercengtage;
	}
}
