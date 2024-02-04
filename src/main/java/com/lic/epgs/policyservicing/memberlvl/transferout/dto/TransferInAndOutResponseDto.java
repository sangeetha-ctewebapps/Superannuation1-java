package com.lic.epgs.policyservicing.memberlvl.transferout.dto;

import java.io.Serializable;
import java.util.List;

import com.lic.epgs.policyservicing.common.dto.PolicyServiceNotesDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
public class TransferInAndOutResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;
//
//	private List<TransferInAndOutReqDto> responseDataList;
//	private TransferInAndOutReqDto responseData;
//	private List<PolicyServiceNotesDto> commonNotesDto;
//	private Object responseObject;
//	private PolicyServiceNotesDto notesDto;
//	private Long memberId;
	private transient Object responseData;
	private String transactionStatus;
	private String transactionMessage;
	

}
