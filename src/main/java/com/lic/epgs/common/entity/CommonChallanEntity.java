package com.lic.epgs.common.entity;

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
@Table(name = "CHELLAN_BASIC_DETAILS")
public class CommonChallanEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMM_CHALLAN_SEQUENCE")
	@SequenceGenerator(name = "COMM_CHALLAN_SEQUENCE", sequenceName = "COMM_CHALLAN_SEQ", allocationSize = 1)
	@Column(name = "CHALLAN_ID", length = 20)
	private Integer challanId;

	@Column(name = "QUOTATION_ID")
	private Long quotationId;

	@Column(name = "CHALLAN_NO", length = 50)
	private String challanNo;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "IS_ACTIVE", length = 10)
	private Boolean isActive;

	@Column(name = "MPH_NAME", length = 255)
	private String mphName;

	@Column(name = "PRODUCT_CODE", length = 50)
	private String productCode;

	@Column(name = "PROPOSAL_NO", length = 50)
	private String proposalNo;

	@Column(name = "VAN_NO", length = 50)
	private String vanNo;

	@Column(name = "CREATED_BY", length = 50)
	private String createdBy;

	@Column(name = "MODIFIED_BY", length = 50)
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();

	@Column(name = "QUOTATION_NO", length = 50)
	private String quotationNo;

	@Column(name = "BANK_BRANCH", length = 90)
	private String bankBranch;

	@Column(name = "BANK", length = 90)
	private String bank;

	@Column(name = "BANK_UNIQUE_ID", length = 50)
	private String bankUniueld;

	@Column(name = "IFSC_CODE")
	private String ifscCode;

	@Column(name = "TRI_NO")
	private String triNo;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name = "PROPOSAL_ID")
	private String proposalId;
	
	@Column(name = "PROPOSAL_NUMBER")
	private String proposalNumber;


}
