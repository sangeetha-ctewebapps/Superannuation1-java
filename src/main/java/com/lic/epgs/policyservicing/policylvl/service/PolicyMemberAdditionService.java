/**
 * 
 */
package com.lic.epgs.policyservicing.policylvl.service;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Muruganandam
 *
 */
public interface PolicyMemberAdditionService {

//	CommonResponseDto save(PolicyServiceMbrDto policyMbrDto);
//
//	CommonResponseDto getByData(PolicySearchDto dto);
//
//	CommonResponseDto getByPolicyNo(PolicySearchDto dto);
//
//	CommonResponseDto sendToCheker(PolicySearchDto dto);
//
//	CommonResponseDto approveOrReject(PolicySearchDto dto);
//
//	CommonResponseDto saveNotes(PolicyServiceNotesDto policyMbrNotesDto);
//
//	CommonResponseDto sendToMaker(PolicySearchDto jsonToSearchPolicySearchDto);
//
//	CommonResponseDto citrieaSearch(PolicyServiceMemberAdditionDto dto);
//
//	CommonResponseDto uploadDocument(PolicyServiceDocumentDto docsDto);
//
//	CommonResponseDto removeDocument(PolicyServiceDocumentDto docsDto);

	
	
	
	void bulkDownload(Long batchId, String fileType, HttpServletResponse response);

}
