package com.lic.epgs.policy.old.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PolicySearchDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String policyDtlsId;
	private String policyNumber;
	private String mphCode;
	private String mphName;
	private String from;
	private String to;
	private String role;
	private String product;
	private String lineOfBusiness;
	private String policyStatus;
	private String variant;
	private String unitCode;
	private String proposalNumber;
	private String pageName;
	private String userName;
	private Boolean isLoad;
	private String policyMemberNo;
	private String serviceNo;
	private String mergerStatus;
	
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	private String rejectionReasonCode;
	private String rejectionRemarks;
	

}
