package com.lic.epgs.policy.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.constant.MemberMasterConstant;
import com.lic.epgs.policy.dao.MemberMasterDao;
import com.lic.epgs.policy.dto.GetPolicyMemberDetailsRequestDto;
import com.lic.epgs.policy.dto.GetPolicyMemberDetailsResponseDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@PropertySource(value = "classpath:queries/member-master.properties", ignoreResourceNotFound = true)
public class MemberMasterDaoImpl implements MemberMasterDao {
	
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	public MemberMasterDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Autowired
	private Environment env;
	
	protected final Logger logger = LogManager.getLogger(getClass());
	
	//@SuppressWarnings("deprecation")
	//@SuppressWarnings("deprecation")
	@Override
	public List<GetPolicyMemberDetailsResponseDto> getPolicyMemberDetailsDataTable(
			GetPolicyMemberDetailsRequestDto getPolicyMemberDetailsRequestDto) {
		String query = env.getProperty(MemberMasterConstant.GET_POLICY_MEMBER_DETAILS_DATATABLE);
		logger.info("GET_POLICY_MEMBER_DETAILS_DATATABLE---   {}",query);
		jdbcTemplate.setFetchSize(900000);
		
		
		
		return jdbcTemplate.query(query.toString(),
				new Object[] {  
						getPolicyMemberDetailsRequestDto.getPolicyId(),
						getPolicyMemberDetailsRequestDto.getMembershipNumber(),
						getPolicyMemberDetailsRequestDto.getMembershipNumber(),
						getPolicyMemberDetailsRequestDto.getLicId(),
						getPolicyMemberDetailsRequestDto.getLicId(),
						
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						getPolicyMemberDetailsRequestDto.getColumnSort(),
						
						getPolicyMemberDetailsRequestDto.getStart(),
						getPolicyMemberDetailsRequestDto.getEnd()
		},
				new int[] {
						Types.INTEGER,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
						
						Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,
						Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,
						Types.INTEGER,Types.INTEGER,
						
						Types.INTEGER,Types.INTEGER
				}
		,			
				new RowMapper<GetPolicyMemberDetailsResponseDto>() {
					@Override
					public GetPolicyMemberDetailsResponseDto mapRow(ResultSet rs, int arg1) throws SQLException {
						GetPolicyMemberDetailsResponseDto getPolicyMemberDetailsResponseDto = new GetPolicyMemberDetailsResponseDto();

						getPolicyMemberDetailsResponseDto.setMemberId(rs.getInt("MEMBER_ID"));
						getPolicyMemberDetailsResponseDto.setLicId(StringUtils.trim(rs.getString("LIC_ID")));
						getPolicyMemberDetailsResponseDto.setDateOfBirth(StringUtils.trim(rs.getDate("DATE_OF_BIRTH").toString()));
						getPolicyMemberDetailsResponseDto.setMemberStatus(StringUtils.trim(rs.getString("MEMBER_STATUS")));
						getPolicyMemberDetailsResponseDto.setFatherName(StringUtils.trim(rs.getString("FATHER_NAME")));
						getPolicyMemberDetailsResponseDto.setFirstName(StringUtils.trim(rs.getString("FIRST_NAME")));
						getPolicyMemberDetailsResponseDto.setLastName(StringUtils.trim(rs.getString("LAST_NAME")));
						getPolicyMemberDetailsResponseDto.setCategoryNo(rs.getInt("CATEGORY_NO"));
						getPolicyMemberDetailsResponseDto.setPolicyId(rs.getInt("POLICY_ID"));
						getPolicyMemberDetailsResponseDto.setMembershipNumber(StringUtils.trim(rs.getString("MEMBERSHIP_NUMBER")));
						
						getPolicyMemberDetailsResponseDto.setTotalRecords(rs.getLong("TOTAL_COUNT"));
						getPolicyMemberDetailsResponseDto.setNoOfPages(rs.getLong("NO_OF_PAGES"));
						
						return getPolicyMemberDetailsResponseDto;
					}
				});
		
	}

}
