package com.lic.epgs.payout.temp.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "TEMP_PAYOUT")
public class TempPayoutEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEMP_PAYOUT_ID_SEQUENCE")
	@SequenceGenerator(name = "TEMP_PAYOUT_ID_SEQUENCE", sequenceName = "TEMP_PAYOUT_ID_SEQUENCE", allocationSize = 1)
	@Column(name = "PAYOUT_ID", length = 10)
	private Long payoutId;

	@Column(name = "CLAIM_ID", length = 20)
	private Long claimId;
	
	@Column(name = "POLICY_ID", length = 10)
	private Long policyId;

	@Column(name = "CLAIM_NO", length = 20)
	private String claimNo;
	
	@Column(name = "INTIMATION_NO", length = 20)
	private String initiMationNo;
	
	@Column(name = "PAYOUT_NO", length = 20)
	
	private String payoutNo;

	@Column(name = "PAYOUT_STATUS", length = 5)
	private Integer status;

	@Column(name = "IS_ACTIVE", length = 10)
	private Boolean isActive;

	@Column(name = "DT_OF_EXIT")
	private Date dtOfExit;

	@Column(name = "MODE_OF_EXIT", length = 2)
	private Integer modeOfExit;

	@Column(name = "REASON_EXIT", length = 10)
	private Long reasonExit;

	@Column(name = "OTHER_REASON", length = 200)
	private String otherReason;

	@Column(name = "PLACE_OF_EVENT", length = 100)
	private String placeOfEvent;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "CREATED_BY", length = 100)
	private String createdBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@Column(name = "MODIFIED_BY", length = 50)
	private String modifiedBy;

	@Column(name = "MPH_CODE", length = 50)
	private String mphCode;

	@Column(name = "MPH_NAME", length = 50)
	private String mphName;

	@Column(name = "PRODUCT", length = 50)
	private String product;

	@Column(name = "VARIANT", length = 50)
	private String variant;

	@Column(name = "LINE_OF_BUSINESS", length = 50)
	private String lineOfBusiness;

	@Column(name = "UNIT_CODE", length = 50)
	private String unitCode;

	@Column(name = "MASTER_POLICY_NO", length = 50)
	private String masterPolicyNo;

	@Column(name = "MASTER_POLICY_STATUS", length = 50)
	private String masterpolicyStatus;

	@Column(name = "WORK_FLOW_STATUS", length = 50)
	private String workflowStatus;
	
	@Column(name = "CHECKER_CODE", length = 50)
	private String checkerCode;

	@Column(name = "MAKER_CODE", length = 50)
	private String makerCode;

	@Column(name = "REPUDATION_REASON", length = 1000)
	private String repudationReason;
	
	@Column(name = "DATE_OF_DEATH")
	private Date dateOfDeath;
	
	@OneToOne(mappedBy = "payout", cascade = CascadeType.ALL)
	private TempPayoutMbrEntity payoutMbr;
	
	@Column(name = "POLICY_TYPE", length = 1000)
	private String policyType;
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "payout")
	private List<TempPayoutNotesEntity> payoutNotes = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "payout")
	private List<TempPayoutDocumentDetail> payoutDocDetails = new ArrayList<>();


}
