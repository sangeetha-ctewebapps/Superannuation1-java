package com.lic.epgs.policyservicing.policylvl.dto.merger;

import java.io.Serializable;
import java.util.List;

import com.lic.epgs.common.dto.CommonDocsDto;
import com.lic.epgs.common.dto.CommonNotesDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
public class PolicyLevelMergerApiResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<PolicyLevelMergerDto> responseDataList;
	private PolicyLevelMergerDto responseData;
	private List<CommonNotesDto> commonNotesDto;
	private Object responseObject;
	private PolicyLevelMergerDto quotationDto;
	private CommonDocsDto docsDto;
	private Long mergeId;
	private String transactionStatus;
	private String transactionMessage;
	

}
