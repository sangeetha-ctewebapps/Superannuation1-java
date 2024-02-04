package com.lic.epgs.payout.temp.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "TEMP_PAYOUT_MBR_ADRS")
public class TempPayoutMbrAddressEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_PAYOUT_MBR_ADDR_SEQUENCE")
	@SequenceGenerator(name = "T_PAYOUT_MBR_ADDR_SEQUENCE", sequenceName = "T_PAYOUT_MBR_ADDR_SEQUENCE", allocationSize = 1)
	@Column(name = "ADDRESS_ID", length = 10)
	private Long addressId;

	@Column(name = "ADDRESS_TYPE")
	private String addressType;

	@Column(name = "COUNTRY")
	private String country;
	
	@Column(name = "PAYOUT_NO", length = 30)
	private String payoutNo;

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

	@Column(name = "CREATED_BY", length = 100)
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "MODIFIED_BY", length = 100)
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PAYOUT_MEMBER_ID", nullable = false)
	private TempPayoutMbrEntity payoutMbrEntity;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
}
