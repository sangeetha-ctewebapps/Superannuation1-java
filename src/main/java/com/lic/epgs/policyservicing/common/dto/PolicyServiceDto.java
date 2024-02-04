package com.lic.epgs.policyservicing.common.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.lic.epgs.policyservicing.memberlvl.transferout.dto.TransferInAndOutDto;
import com.lic.epgs.policyservicing.policylvl.dto.conversion.PolicyLevelConversionDto;
import com.lic.epgs.policyservicing.policylvl.dto.freelookcancellation.FreeLookCancellationDto;
import com.lic.epgs.policyservicing.policylvl.dto.memberaddition.PolicyServiceMemberAdditionDto;
import com.lic.epgs.policyservicing.policylvl.dto.merger.PolicyLevelMergerDto;
import com.lic.epgs.policyservicing.policylvl.dto.policydetailschange.PolicyDetailsChangeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pradeepramesh
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyServiceDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long serviceId;
	
	private String serviceNumber;
	private String serviceType;
	
	private String serviceDoneBy;
	private Date serviceEffectiveDate ;
	
	private String requestReceivedBy;
	private Date requestReceivedDate ;
	
	private String serviceStatus;
	private String workflowStatus;
	
	private Long policyId;
	
//	private Boolean isUsing;
	private Boolean isActive;
	
	private String unitCode;

	private String createdBy;
	private Date createdOn ;
	private String modifiedBy;
	private Date modifiedOn ;
	
	private Set<TransferInAndOutDto> memberTransferOutTemp = new HashSet<>();
	private Set<FreeLookCancellationDto> flcTemp = new HashSet<>();
	private Set<PolicyLevelMergerDto> mergerTemp = new HashSet<>();
	private Set<PolicyLevelConversionDto> conversionTemp = new HashSet<>();
	private Set<PolicyServiceMemberAdditionDto> memberAdditionTemp = new HashSet<>();
	private Set<PolicyDetailsChangeDto> policyDetailsChangeTempEntity = new HashSet<>();

}
