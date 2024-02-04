//package com.lic.epgs.policyservicing.policylvl.entity.memberaddition;
//
//
//import java.io.Serializable;
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.Table;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@Entity
//@Table(name = "POLICY_SRV_MBR_BANK")
//public class PolicyServiceMemberBankEntity implements Serializable {
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_MBR_BANK_SEQUENCE")
//	@SequenceGenerator(name = "POL_MBR_BANK_SEQUENCE", sequenceName = "POL_MBR_BANK_SEQ", allocationSize = 1)
//	@Column(name = "BANK_ID", length = 10)
//	private Long bankId;
//
//	@Column(name = "MEMBER_ID")
//	private Long memberId;
//
//	@Column(name = "ACCOUNT_NUMBER")
//	private String accountNumber;
//
//	@Column(name = "CONFIRM_ACCOUNT_NUMBER")
//	private String confirmAccountNumber;
//
//	@Column(name = "ACCOUNT_TYPE")
//	private String accountType;
//
//	@Column(name = "IFSC_CODE_AVAILABLE")
//	private String ifscCodeAvailable;
//
//	@Column(name = "IFSC_CODE")
//	private String ifscCode;
//
//	@Column(name = "BANK_NAME")
//	private String bankName;
//
//	@Column(name = "BANK_BRANCH")
//	private String bankBranch;
//
//	@Column(name = "BANK_ADDRESS", length = 10)
//	private String bankAddress;
//
//	@Column(name = "COUNTRY_CODE")
//	private Integer countryCode;
//
//	@Column(name = "STD_CODE")
//	private Integer stdCode;
//
//	@Column(name = "LANDLINE_NUMBER", length = 10)
//	private Long landlineNumber;
//
//	@Column(name = "EMAIL_ID", length = 50)
//	private String emailId;
//
//	@Column(name = "IS_ACTIVE")
//	private Boolean isActive = Boolean.TRUE;
//
//	@Column(name = "CREATED_BY", length = 50)
//	private String createdBy;
//
//	@Column(name = "CREATED_ON")
//	private Date createdOn = new Date();
//
//	@Column(name = "MODIFIED_BY", length = 50)
//	private String modifiedBy;
//
//	@Column(name = "MODIFIED_ON")
//	private Date modifiedOn = new Date();
//}
