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
@Table(name = "MEMBER_ADDRESS")
public class MemberAddressEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "P_MBR_AD_M_SEQUENCE")
	@SequenceGenerator(name = "P_MBR_AD_M_SEQUENCE", sequenceName = "P_MBR_AD_M_SEQ", allocationSize = 1)
	@Column(name = "ADDRESS_ID")
	private Long addressId;
	@Column(name = "MEMBER_ID")
	private Long memberId;
	@Column(name = "ADDRESS_1")
	private String address1;
	@Column(name = "ADDRESS_2")
	private String address2;
	@Column(name = "ADDRESS_3")
	private String address3;
	@Column(name = "ADDRESS_TYPE")
	private String addressType;
	@Column(name = "CITY")
	private String city;
	@Column(name = "DISTRICT")
	private String district;
	@Column(name = "STATE_NAME")
	private String stateName;
	@Column(name = "COUNTRY")
	private String country;
	@Column(name = "PINCODE")
	private Integer pincode;

	@Column(name = "POLICY_ID")
	private Long policyId;
	
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