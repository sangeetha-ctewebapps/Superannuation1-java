package com.lic.epgs.quotation.entity;

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
@Table(name = "QUO_MBR_ADRS_TEMP")
public class QuotationMemberAddressTempEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUO_MBR_ADRS_TEMP_SEQ")
	@SequenceGenerator(name = "QUO_MBR_ADRS_TEMP_SEQ", sequenceName = "QUO_MBR_ADRS_TEMP_SEQ", allocationSize = 1)
	@Column(name = "ADDRESS_ID", length = 10)
	private Long addressId;

	@Column(name = "MEMBER_ID", length = 10)
	private Long memberId;

	@Column(name = "ADDRESS_TYPE")
	private String addressType;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "PINCODE")
	private Integer pinCode;

	@Column(name = "DISTRICT")
	private String district;

	@Column(name = "STATE")
	private String state;
	
	@Column(name = "CITY")
	private String city;

	@Column(name = "ADDRESS_1")
	private String addressLineOne;

	@Column(name = "ADDRESS_2")
	private String addressLineTwo;

	@Column(name = "ADDRESS_3")
	private String addressLineThree;

	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive = Boolean.TRUE;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

}
