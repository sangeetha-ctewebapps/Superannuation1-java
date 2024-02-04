package com.lic.epgs.policy.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class policyContributionExportDto {
	private Date date;
	private String contributionNumber;
	private String licId;
	private String employerContribution;
	private String employeeContribution;
	private String voluntaryContribution;
	private String totalContribution;
	private String groupSchemeDepartment;
	private String policyNo;
	private String contributionFrom;
	private String contributionTo;
	private List<String> dueOn;
	private List<String> paidOn;
	private String customerCode;
	private String customerName;
	private String annualRenewalDate;


}
