/**
 * 
 */
package com.lic.epgs.policyservicing.policylvl.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.policyservicing.policylvl.entity.memberaddition.PolicyServiceMemberAdditionBatchEntity;
import com.lic.epgs.policyservicing.policylvl.repository.memberaddition.PolicyServiceMemberAdditionBatchRepository;
import com.lic.epgs.policyservicing.policylvl.service.PolicyMemberAdditionService;
import com.lic.epgs.utils.CommonConstants;

/**
 * @author Muruganandam
 *
 */

@Service
public class PolicyMemberAdditionServiceImpl implements PolicyMemberAdditionService{
	
	protected final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private PolicyServiceMemberAdditionBatchRepository policyServiceMemberAdditionBatchRepository;
	

//	@Override
//	public CommonResponseDto save(PolicyServiceMbrDto policyMbrDto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public CommonResponseDto getByData(PolicySearchDto dto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public CommonResponseDto getByPolicyNo(PolicySearchDto dto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public CommonResponseDto sendToCheker(PolicySearchDto dto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public CommonResponseDto approveOrReject(PolicySearchDto dto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public CommonResponseDto saveNotes(PolicyServiceNotesDto policyMbrNotesDto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public CommonResponseDto sendToMaker(PolicySearchDto jsonToSearchPolicySearchDto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public CommonResponseDto citrieaSearch(PolicyServiceMemberAdditionDto dto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public CommonResponseDto uploadDocument(PolicyServiceDocumentDto docsDto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public CommonResponseDto removeDocument(PolicyServiceDocumentDto docsDto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	
//	
//	
	
	
	
	
	

	@Override
	public void bulkDownload(Long batchId, String fileType, HttpServletResponse response) {
		try {
			logger.info("PolicyMemberAdditionServiceImpl -- bulkDownload --end");
			if(batchId !=null && fileType!=null) {
				PolicyServiceMemberAdditionBatchEntity batch =  policyServiceMemberAdditionBatchRepository.findByBatchIdAndIsActiveTrue(batchId);
				 
				if(batch!=null) {
					
					byte[] bytes =null;
					
					
					if(fileType.equalsIgnoreCase("raw")) 
						bytes = batch.getRawFile();
						
					else if(fileType.equalsIgnoreCase("success")) 
						bytes = batch.getSuccessFile();
						
					else
						bytes = batch.getFailedFile();
				
					response.setContentType("Content-Type:text/csv");
					response.setHeader("Content-Disposition","attachment;filename="+fileType+"_file_"+batchId+".csv");
					ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
					baos.write(bytes, 0, bytes.length);
					OutputStream os = response.getOutputStream();
					response.setHeader(CommonConstants.STATUS, "File is Downloaded Successfully!!!");
					baos.writeTo(os);
					os.flush();
					os.close();
				}else {
					response.setHeader("STATUS", "No files Available against this BathId "+batchId);
				}
				
			}else {
				response.setHeader("STATUS","BatchId and FileType is null!!");
			}

		} catch (Exception e) {
			logger.error("Exception-- PolicyMemberAdditionServiceImpl-- bulkDownload --", e);
		
		}
		
		logger.info("PolicyMemberAdditionServiceImpl -- bulkDownload --end");
		
	}
}
