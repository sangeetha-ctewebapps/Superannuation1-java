package com.lic.epgs.policy.contribution.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.lic.epgs.policy.contribution.dao.PolicyContributionDao;
import com.lic.epgs.policy.contribution.dto.PolicyContributionConstant;
import com.lic.epgs.policy.contribution.dto.PolicyContributionResponseDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional
@PropertySource(value = "classpath:queries/policy.contribution.properties", ignoreResourceNotFound = true)
public class PolicyContributionDaoImpl implements PolicyContributionDao {

	JdbcTemplate jdbcTemplate;

	@Autowired
	public PolicyContributionDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Autowired
	private Environment env;

	protected final Logger logger = LogManager.getLogger(getClass());

	@Override
	public List<PolicyContributionResponseDto> getPolcyContributionsDetails(Integer policyId, String financialYear,
			String quarter) {

		String query = env.getProperty(PolicyContributionConstant.GET_POLICY_CONTRIBUTION_DETAILS);
		logger.info(
				"GET_POLICY_CONTRIBUTION_DETAILS Query {}, and parameter like Policy id : {}, Financial year : {}, Quarter : {} ",
				query, policyId, financialYear, quarter);

		if (quarter == "")
			quarter = null;
		//jdbcTemplate.setFetchSize(900000);
		return jdbcTemplate.query(query.toString(), new Object[] { policyId, financialYear, quarter, quarter },
				new int[] { Types.INTEGER, Types.VARCHAR,Types.VARCHAR,Types.VARCHAR},
				new RowMapper<PolicyContributionResponseDto>() {
					@Override
					public PolicyContributionResponseDto mapRow(ResultSet rs, int arg1) throws SQLException {
						PolicyContributionResponseDto policyContributionResponseDto = new PolicyContributionResponseDto();

						policyContributionResponseDto.setContributionId(rs.getString("CONTRIBUTION_ID"));
						policyContributionResponseDto.setDepositNumber(rs.getString("DEPOSIT_NUMBER"));
						policyContributionResponseDto.setDepositDate(rs.getString("DEPOSIT_DATE"));
						policyContributionResponseDto.setContributionType(rs.getString("CONTRIBUTION_TYPE"));
						policyContributionResponseDto.setContributionDate(rs.getString("CONTRIBUTION_DATE"));
						policyContributionResponseDto.setAmount(rs.getString("AMOUNT"));

						return policyContributionResponseDto;

					}
				});
	}

}
