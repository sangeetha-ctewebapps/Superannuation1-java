package com.lic.epgs.policyservicing.policylvl.dto.merger;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lic.epgs.common.dto.CommonDocsDto;
import com.lic.epgs.common.dto.CommonNotesDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Narayanan.D
 *
 */

@NoArgsConstructor
@Getter
@Setter
public class PolicyLevelMergerDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long mergeId;
	private Long serviceId;
	private Long policyId;
	private Set<CommonDocsDto> docs = new HashSet<>();
	private Set<CommonNotesDto> notes = new HashSet<>();
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date effectiveDt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date reqRecvdDt;
	private Long mergerType;
	private Long destinationPolicyType;
	private String mergingPolicy;
	private String destinationPolicy;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	private Integer mergeStatus;
	private Integer workflowStatus;
	private Boolean isActive;
	private String unitCode;
	private String roleType;
	private String rejectionReasonCode;
	private String rejectionRemarks;
	private String policyNumber;
	private String mphCode;
	private String mphName;
	private String product;
	
	
	
}
