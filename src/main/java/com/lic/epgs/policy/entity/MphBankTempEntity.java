package com.lic.epgs.policy.entity;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "MPH_BANK_TEMP")
public class MphBankTempEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MPH_BNK_T_SEQUENCE")
	@SequenceGenerator(name = "MPH_BNK_T_SEQUENCE", sequenceName = "MPH_BNK_T_SEQ", allocationSize = 1)
	@Column(name = "MPH_BANK_ID")
	private Long mphBankId;
	@Column(name = "IS_DEFAULT ")
	private Boolean isDefault;
	@Column(name = "ACCOUNT_NUMBER ")
	private String accountNumber;
	@Column(name = "ACCOUNT_TYPE ")
	private String accountType;
	@Column(name = "BANK_ADDRESS ")
	private String bankAddress;
	@Column(name = "BANK_BRANCH ")
	private String bankBranch;
	@Column(name = "BANK_NAME ")
	private String bankName;
	@Column(name = "CONFIRM_ACCOUNT_NUMBER ")
	private String confirmAccountNumber;
	@Column(name = "COUNTRY_CODE ")
	private Integer countryCode;
	@Column(name = "CREATED_BY ")
	private String createdBy;
	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();
	@Column(name = "EMAIL_ID ")
	private String emailId;
	@Column(name = "IFSC_CODE ")
	private String ifscCode;
	@Column(name = "IFSC_CODE_AVAILABLE")
	private String ifscCodeAvailable;
	@Column(name = "IS_ACTIVE ")
	private Boolean isActive = Boolean.TRUE;
	@Column(name = "LANDLINE_NUMBER")
	private Long landlineNumber;
//	@Column(name = "MEMBER_ID")
//	private Long memberId;
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	@Column(name = "MODIFIED_ON ")
	private Date modifiedOn = new Date();
	@Column(name = "STD_CODE")
	private Integer stdCode;
	@Column(name = "MPH_ID ")
	private Long mphId;
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


}
