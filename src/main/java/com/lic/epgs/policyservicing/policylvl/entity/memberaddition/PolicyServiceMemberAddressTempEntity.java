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
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@Entity
//@Table(name = "POLICY_SRV_MBR_ADRS_TEMP")
//public class PolicyServiceMemberAddressTempEntity implements Serializable {
//	private static final long serialVersionUID = 1L;
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_MBR_ADRS_TEMP_SEQUENCE")
//	@SequenceGenerator(name = "POL_MBR_ADRS_TEMP_SEQUENCE", sequenceName = "POL_MBR_ADRS_TEMP_SEQ", allocationSize = 1)
//	@Column(name = "ADDRESS_ID")
//	private Long addressId;
//
//	@Column(name = "MEMBER_ID")
//	private Long memberId;
//	
//	@Column(name = "POLICY_ID")
//	private Long policyId;
//
//	@Column(name = "ADDRESS_TYPE")
//	private String addressType;
//
//	@Column(name = "COUNTRY")
//	private String country;
//
//	@Column(name = "PINCODE")
//	private Integer pinCode;
//
//	@Column(name = "DISTRICT")
//	private String district;
//
//	@Column(name = "STATE")
//	private String state;
//	
//	@Column(name = "CITY")
//	private String city;
//
//	@Column(name = "ADDRESS_1")
//	private String addressLineOne;
//
//	@Column(name = "ADDRESS_2")
//	private String addressLineTwo;
//
//	@Column(name = "ADDRESS_3")
//	private String addressLineThree;
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
