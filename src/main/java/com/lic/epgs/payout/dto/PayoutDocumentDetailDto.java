package com.lic.epgs.payout.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayoutDocumentDetailDto {

	private Long documentId;

	private String documentNo;

	private String documentName;

	private Long documentType;

	private String issuedBy;

	private String issuedDate;

	private String payoutNo;

	private Integer docStatus;

	private String requirement;

	private String documentIndex;

	private Integer folderIndex;

	private Long mergeId;

	private String createdBy;
	
	private String initiMationNo;

}
