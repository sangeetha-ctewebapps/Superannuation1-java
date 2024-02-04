package com.lic.epgs.policyservicing.policylvl.dto.merger;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lic.epgs.policyservicing.memberlvl.transferout.entity.TransferInAndOutMasterEntity;
import com.lic.epgs.policyservicing.policylvl.dto.freelookcancellation.FreeLookCancellationDto;
import com.lic.epgs.policyservicing.policylvl.dto.memberaddition.PolicyServiceMemberAdditionDto;
import com.lic.epgs.policyservicing.policylvl.entity.policydetailschange.policyDetailsChangeTempEntity;
import com.lic.epgs.surrender.entity.PolicySurrenderTempEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
public class PolicyLevelServiceDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long serviceId;
	private Long policyId;
	private Long unitId;
	
	private String serviceNo;
	private Integer serviceStatus;
	private Integer workflowStatus;
	
	private Long serviceType;
	private String srvDoneBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date srvEffectiveDt;

	private String reqRecvdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date reqRecvdDt;

	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;


	private Set<FreeLookCancellationDto> flcTemp = new HashSet<>();
	private Set<PolicyLevelMergerDto> mergerTemp = new HashSet<>();
	private Set<PolicyServiceMemberAdditionDto> additionOfMemberTemp = new HashSet<>();
	private Set<policyDetailsChangeTempEntity> policyDetailChangeTemp = new HashSet<>();
	
	private Set<PolicySurrenderTempEntity> surrender = new HashSet<>();
	private Set<TransferInAndOutMasterEntity> memberLevelTrnOut = new HashSet<>();
}
