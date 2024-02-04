package com.lic.epgs.topupda.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Dhanush
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopupdaDto {

	private Long topupId;
	private String message;
	private String customErrorCode;
	private String quotationNumber;
	private String quotationStatus;
	private String policyNumber;
	private String policyStatus;
	private String mphName;
	private String product;
	private String varient;
	private String calculationValue;
	private String typeOfTopup;
	private String gstBorneBy;
	private String grossPurchasePrice;
	private String gst;
	private String quotationAmount;
	private String purchasePrice;
	private String arrears;
	private String recovery;
	private String topupStagId;
	private String topupStatus;
	private String unitCode;
	private String modifiedBy;
	private Date modifiedOn ;
	private String createdBy;
	private Date createdOn ;
	private Boolean isActive ;
	private String rejectionRemarks;
	private String rejectioncode;
	private Set<TopupdaNotesDto> notes = new HashSet<>();

}
