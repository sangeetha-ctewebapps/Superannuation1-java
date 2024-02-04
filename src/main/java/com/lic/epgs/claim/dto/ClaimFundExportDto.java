/**
 * 
 */
package com.lic.epgs.claim.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Karthick M
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClaimFundExportDto {
	
	private String sNo;
	private String date;
	private String type;
	private String amount;
	private String intRate;
	private String noOfDays;
	private String avgBalance;
	private String interestAmount;
	private String total;
	private String aIR;
	private String mFR;
	private String fMC;
	private String fMCReconDays;
	private String gST;
}
