package com.lic.epgs.common.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.lic.epgs.common.dto.CommonDocumentMasterDto;
import com.lic.epgs.common.dto.CommonExternalApiResponseDto;
import com.lic.epgs.common.dto.CommonResponseDto;
import com.lic.epgs.common.dto.DepositTransferResponse;
import com.lic.epgs.common.dto.PolicyResponseDataAnnuityDto;
import com.lic.epgs.common.dto.SearchDepositTransferDto;
import com.lic.epgs.common.dto.PolicyResponseDataAnnuityDto;
import com.lic.epgs.common.exception.ApplicationException;
import com.lic.epgs.policy.entity.MemberMasterEntity;
import com.lic.epgs.policy.entity.MphMasterEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;

public interface CommonService {

	public String getSequence(String type);

	public CommonResponseDto commonStatus();

	public CommonExternalApiResponseDto claimrequireddocument();

	public CommonExternalApiResponseDto claimdocumentstatus();

	public CommonExternalApiResponseDto claimIntimaitonType();

	public CommonExternalApiResponseDto claimReasonforexit();

	public CommonExternalApiResponseDto claimMemberUploadFileType();

	public CommonExternalApiResponseDto claimMakerActions();

	public CommonExternalApiResponseDto claimApproverActions();

	public CommonExternalApiResponseDto claimCommutationCalc();

	public CommonExternalApiResponseDto claimAmountPayable();

	public CommonExternalApiResponseDto claimAnuityCreationOptions();

	public CommonExternalApiResponseDto nomineeRelationShip();

	public CommonExternalApiResponseDto policyStatusApi();

	public CommonExternalApiResponseDto maritalStatus();

	public CommonExternalApiResponseDto accountType();

	public CommonExternalApiResponseDto sharedAmountType();

	public void getSampleFile(String fileType, HttpServletResponse response);

	public CommonResponseDto saveSampleFile(String fileType, String createdBy, MultipartFile file);

	public CommonResponseDto getFinancialYeartDetails(String date);

	PolicyMasterEntity findPolicyDetails(String policyNumber, Long policyId) throws ApplicationException;

	MphMasterEntity findMphDetails(Long mphId) throws ApplicationException;

	MemberMasterEntity searchMember(Long policyId, String licId, Long memberId, String financialYear)
			throws ApplicationException;

	public List<CommonDocumentMasterDto> getDocumentDetails(CommonDocumentMasterDto commonDocumentMasterDto)
			throws ApplicationException;

	public PolicyResponseDataAnnuityDto policyresponseData(String policyNumber);

	MemberMasterEntity searchMemberNew(Long policyId, String licId, Long memberId, String financialYear)
			throws ApplicationException;

	public List<DepositTransferResponse> searchDepositTransfer(SearchDepositTransferDto searchDepositTransferDto);

	public List<DepositTransferResponse> DepositTransferSearch(SearchDepositTransferDto searchDepositTransferDto);

	public PolicyResponseDataAnnuityDto depositAppropriation(String collectionNo); 
}
