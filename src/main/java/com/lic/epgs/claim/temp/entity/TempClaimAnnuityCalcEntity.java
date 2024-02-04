package com.lic.epgs.claim.temp.entity;

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
@Table(name = "TEMP_CLAIM_ANNUITY_CALC")
public class TempClaimAnnuityCalcEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEMP_CLAIM_ANNUITY_SEQ")
	@SequenceGenerator(name = "TEMP_CLAIM_ANNUITY_SEQ", sequenceName = "TEMP_CLAIM_ANNUITY_SEQ", allocationSize = 1)
	@Column(name = "ANNUITY_ID", length = 10)
	private Long annuityId;

	@Column(name = "PURCHASE_PRICE", length = 20, scale = 2)
	private Double purchasePrice;

	@Column(name = "AMT_PAID_TO", length = 10)
	private Long amtPaidTo;

	@Column(name = "NOMINEE_CODE", length = 50)
	private String nomineeCode;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "CREATED_BY", length = 100)
	private String createdBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@Column(name = "PAN")
	private String pan;

	@Column(name = "MODIFIED_BY", length = 50)
	private String modifiedBy;

	@Column(name = "PENSION", precision = 20, scale = 2)
	private Double pension;

	@Column(name = "ANNUITY_OPTION", length = 50)
	private String annuityOption;

	@Column(name = "SPOUSE_NAME", length = 50)
	private String spouseName;

	@Column(name = "DATE_OF_BIRTH", length = 50)
	private Date dateOfBirth;

	@Column(name = "ANNUITY_MODE", length = 50)
	private String anuityMode;

	@Column(name = "IS_JOINT_LIVE_REQ", length = 5)
	private Boolean isJointLiveRequired;

	@Column(name = "IS_GST_APPLICABLE", length = 5)
	private Boolean isGSTApplicable;

	@Column(name = "CAPTURE_CERTAIN_NUMBER")
	private Integer captureCertainNumber;

	@Column(name = "NET_PURCHASE_PRICE", length = 20, scale = 2)
	private Double netPurchasePrice;

	@Column(name = "GST_BORNE_BY", length = 50)
	private Integer gstBondBy;

	@Column(name = "GST_AMOUNT", length = 50)
	private Double gstAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_MEMBER_ID", nullable = false)
	private TempClaimMbrEntity claimMbrEntity;
//	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "CLAIM_NOMINEE_ID", nullable = true)
//	private TempClaimMbrNomineeEntity claimMbrNomineeEntity;

	@Column(name = "RATE", length = 50)
	private Double rate;

	@Column(name = "RATE_TYPE", length = 50)
	private String rateType;

	@Column(name = "INCENTIVE_RATE", length = 50)
	private Double incentiveRate;

	@Column(name = "DISINCENTIVE_RATE", length = 50)
	private Double disIncentiveRate;

	@Column(name = "N_FACTOR", length = 50)
	private Double nFactor;

	@Column(name = "UNIT_CODE", length = 50)
	private String unitCode;

	@Column(name = "UNIT_NAME", length = 50)
	private String unitName;

	@Column(name = "UNIT_ID", length = 50)
	private String unitId;

	@Column(name = "ARREARS", length = 50)
	private Integer arrears;

	@Column(name = "SHORT_RESERVE")
	private Double shortReserve;

	@Column(name = "ANNUITY_PAYABLE_TO")
	private String annuityPayableTo;

	@Column(name = "GST_RATE", length = 50)
	private Double gstRate;

	@Column(name = "EMAILID")
	private String emailid;

	@Column(name = "MOBILE_NO")
	private Long mobileNo;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
}
