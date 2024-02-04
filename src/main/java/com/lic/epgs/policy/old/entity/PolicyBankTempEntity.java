package com.lic.epgs.policy.old.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author dhiraj wakade Date:23.07.2022
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@Entity
//@Table(name = "POL_BNK_TEMP")
public class PolicyBankTempEntity implements Serializable {

	private static final long serialVersionUID = 1L;

//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_BNK_SEQUENCE")
//	@SequenceGenerator(name = "POL_BNK_SEQUENCE", sequenceName = "POL_BNK_SEQ", allocationSize = 1)
	@Column(name = "BANK_ACCOUNT_ID")
	private Long bankAccountId;

	@Column(name = "POLICY_ID")
	private Long policyId;

	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;

	@Column(name = "ACCOUNT_TYPE")
	private String accountType;

	@Column(name = "IFSC_CODE")
	private String ifscCode;

	@Column(name = "BANK_NAME")
	private String bankName;

	@Column(name = "BANK_BRANCH")
	private String bankBranch;
	

	@Column(name = "BANK_ADDRESS")
	private String bankAddress;

	@Column(name = "COUNTRY_ID")
	private String countryId;

	@Column(name = "STATE_ID")
	private String stateId;

	@Column(name = "DISTRICT_ID")
	private String districtId;

	@Column(name = "CITY_ID")
	private String cityId;
	
	@Column(name = "TOWN_LOCALITY")
	private String townLocality;
	
	@Column(name = "STD_CODE")
	private String stdCode;
	
	@Column(name = "LANDLINE_NUMBER")
	private String landLineNumber;

	@Column(name = "EMAIL_ID")
	private String emailId;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive = Boolean.TRUE;
	
	@Column(name = "IS_MAPPED")
	private Boolean isMapped = Boolean.FALSE;
	
	@Column(name = "SURRENDER_ID", length = 10)
	private Long surrenderId;

}
