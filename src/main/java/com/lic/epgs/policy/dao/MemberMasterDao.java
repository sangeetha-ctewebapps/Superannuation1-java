package com.lic.epgs.policy.dao;

import java.util.List;

import com.lic.epgs.policy.dto.GetPolicyMemberDetailsRequestDto;
import com.lic.epgs.policy.dto.GetPolicyMemberDetailsResponseDto;

public interface MemberMasterDao {
	
	List<GetPolicyMemberDetailsResponseDto> getPolicyMemberDetailsDataTable(GetPolicyMemberDetailsRequestDto getPolicyMemberDetailsRequestDto);

}


