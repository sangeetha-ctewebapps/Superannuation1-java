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
@Table(name = "MPH_ADDRESS")
public class MphAddressEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MPH_ADD_M_SEQUENCE")
	@SequenceGenerator(name = "MPH_ADD_M_SEQUENCE", sequenceName = "MPH_ADD_M_SEQ", allocationSize = 1)
	@Column(name = "ADDRESS_ID ")
	private Long addressId;
	
	@Column(name = "MPH_ID ")
	private Long mphId;
	
	@Column(name = "IS_DEFAULT ")
	private Boolean isDefault;
	
	@Column(name = "ADDRESS_TYPE ")
	private String addressType;
	@Column(name = "ADDRESS_LINE1 ")
	private String addressLine1;
	@Column(name = "ADDRESS_LINE2 ")
	private String addressLine2;
	@Column(name = "ADDRESS_LINE3 ")
	private String addressLine3;
	@Column(name = "CITY_LOCALITY ")
	private String cityLocality;
	@Column(name = "DISTRICT ")
	private String district;
	@Column(name = "STATE_NAME ")
	private String stateName;
	@Column(name = "PINCODE ")
	private Integer pincode;
	@Column(name = "COUNTRY_ID")
	private Integer countryId;
	@Column(name = "STATE_ID")
	private Integer stateId;
	@Column(name = "DISTRICT_ID")
	private Integer districtId;
	@Column(name = "CITY_ID")
	private String cityId;
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
