package com.lic.epgs.adjustmentcontribution.service;
/**
 * @author pradeepramesh
 *
 */
import java.util.Set;
import com.lic.epgs.adjustmentcontribution.dto.ACSaveAdjustmentRequestDto;
import com.lic.epgs.adjustmentcontribution.dto.AdjustmentContributionResponseDto;
import com.lic.epgs.policy.entity.MemberContributionEntity;
import com.lic.epgs.policy.entity.MemberContributionSummaryEntity;
import com.lic.epgs.policy.entity.PolicyContributionEntity;
import com.lic.epgs.policy.entity.PolicyContributionSummaryEntity;

public interface AdjustmentContributionCalcService {

	AdjustmentContributionResponseDto saveAdjustment(ACSaveAdjustmentRequestDto adjustmentDto);
	AdjustmentContributionResponseDto approve(Long adjustmentContributionId, String adjustmentContributionStatus, String role,String variantType);
	Set<PolicyContributionSummaryEntity> savePolicyContributionSummary(Set<PolicyContributionEntity> contributionEntities, String role ,String variantType);
	Set<MemberContributionSummaryEntity> saveMemberContributionSummary(Set<MemberContributionEntity> memberContributionEntiies, String role);
}