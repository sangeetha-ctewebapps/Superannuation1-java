package com.lic.epgs.surrender.dto;

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
public class PolicySurrenderApiResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<PolicySurrenderDto> responseData;
	private List<CommonDocsDto> commonDocsDto;
	private List<CommonNotesDto> commonNotesDto;
	private Object responseObject;
	private PolicySurrenderDto surrenderDto;
	private PolicySurrenderPayOutDto payOutDto;
	private CommonDocsDto docsDto;
	private CommonNotesDto notesDto;
	private Long surrenderId;
	private String transactionStatus;
	private String transactionMessage;

}
