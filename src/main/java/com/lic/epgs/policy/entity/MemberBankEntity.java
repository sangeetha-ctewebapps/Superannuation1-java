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
@Table(name = "MEMBER_BANK")
public class MemberBankEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "P_MBR_BNK_M_SEQUENCE")
	@SequenceGenerator(name = "P_MBR_BNK_M_SEQUENCE", sequenceName = "P_MBR_BNK_M_SEQ", allocationSize = 1)
	@Column(name = "MEMBER_BANK_ID")
	private Long memberBankId;

	@Column(name = "MEMBER_ID")
	private Long memberId;

	@Column(name = "POLICY_ID")
	private Long policyId;

	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;

	@Column(name = "CONFIRM_ACCOUNT_NUMBER")
	private String confirmAccountNumber;

	@Column(name = "ACCOUNT_TYPE")
	private String accountType;

	@Column(name = "IFSC_CODE_AVAILABLE")
	private String ifscCodeAvailable;

	@Column(name = "IFSC_CODE")
	private String ifscCode;

	@Column(name = "BANK_NAME")
	private String bankName;

	@Column(name = "BANK_BRANCH")
	private String bankBranch;

	@Column(name = "BANK_ADDRESS")
	private String bankAddress;

	@Column(name = "COUNTRY_CODE")
	private Integer countryCode;

	@Column(name = "STD_CODE")
	private Integer stdCode;

	@Column(name = "LANDLINE_NUMBER")
	private Long landlineNumber;

	@Column(name = "EMAIL_ID")
	private String emailId;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive = Boolean.TRUE;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();
}
