package com.lic.epgs.policy.old.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pradeepramesh
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@Entity
//@Table(name = "POL_ADRS")
public class PolicyAddressEntity implements Serializable {

	private static final long serialVersionUID = 1L;
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_ADS_M_SEQUENCE")
//	@SequenceGenerator(name = "POL_ADS_M_SEQUENCE", sequenceName = "POL_ADS_M_SEQ", allocationSize = 1)
	@Column(name = "ADDRESS_ID")
	private Long addressId;

	@Column(name = "POLICY_ID")
	private Long policyId;

	@Column(name = "ADDRESS_TYPE")
	private String addressType;

	@Column(name = "PINCODE")
	private String pinCode;

	@Column(name = "COUNTRY_ID")
	private Integer countryId;

	@Column(name = "STATE_ID")
	private Integer stateId;

	@Column(name = "DISTRICT_ID")
	private Integer districtId;

	@Column(name = "CITY_ID")
	private String cityId;

	@Column(name = "TOWN_LOCALITY")
	private String townLocality;

	@Column(name = "ADDRESS_LINE_1")
	private String addressLine1;

	@Column(name = "ADDRESS_LINE_2")
	private String addressLine2;

	@Column(name = "ADDRESS_LINE_3")
	private String addressLine3;

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
}
