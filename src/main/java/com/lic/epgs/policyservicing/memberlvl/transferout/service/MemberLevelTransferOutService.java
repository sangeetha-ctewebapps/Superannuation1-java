package com.lic.epgs.policyservicing.memberlvl.transferout.service;

import com.lic.epgs.policyservicing.common.dto.PolicyServiceNotesDto;
import com.lic.epgs.policyservicing.memberlvl.transferout.dto.TransferInAndOutDto;
import com.lic.epgs.policyservicing.memberlvl.transferout.dto.TransferInAndOutReqDto;
import com.lic.epgs.policyservicing.memberlvl.transferout.dto.TransferInAndOutResponseDto;

public interface MemberLevelTransferOutService {

	TransferInAndOutResponseDto saveAndUpdateMemberTransferOut(TransferInAndOutDto transferInAndOutDto);
	TransferInAndOutResponseDto getOverallDetails(TransferInAndOutReqDto transferInAndOutReqDto);
	TransferInAndOutResponseDto sendToChecker(TransferInAndOutDto transferInAndOutDto);
	TransferInAndOutResponseDto memberSearch(TransferInAndOutReqDto memberSearchDto);
	TransferInAndOutResponseDto sendToMaker(TransferInAndOutReqDto reqDto);
	TransferInAndOutResponseDto approve(TransferInAndOutReqDto reqDto);
	TransferInAndOutResponseDto reject(TransferInAndOutReqDto reqDto);
	
	TransferInAndOutResponseDto getPolicyDetails(TransferInAndOutReqDto dto);
	
	
	
	

	TransferInAndOutResponseDto approvedAndRejectMemberTransferOut(TransferInAndOutReqDto memberLevelTransferOutReqDto,String status);

	TransferInAndOutResponseDto inprogresOrExitingTransferOut(String roleType, String inprogress);

	TransferInAndOutResponseDto saveNotesDetails(PolicyServiceNotesDto commonNotesDto);
	


	Object getInprogressLoad(String role, String unitCode);

	Object getExistingLoad(String role, String unitCode);

	TransferInAndOutResponseDto inprogressCitrieaSearch(TransferInAndOutReqDto memberSearchDto);

	TransferInAndOutResponseDto existingCitrieaSearch(TransferInAndOutReqDto policySearchDto);

	


}
