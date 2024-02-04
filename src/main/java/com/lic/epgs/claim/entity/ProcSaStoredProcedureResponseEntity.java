package com.lic.epgs.claim.entity;

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
@Table(name = "PROC_SA_STORED_PROCEDURE_RESPONSE")
public class ProcSaStoredProcedureResponseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROC_SA_SPR_SEQ")
	@SequenceGenerator(name = "PROC_SA_SPR_SEQ", sequenceName = "PROC_SA_SPR_SEQ", allocationSize = 1)
	@Column(name = "PROC_SA_ID")
	private Long procSaSprId;
	
	
	@Column(name = "CLAIM_ID")
	private Long claimId;
	
	@Column(name = "CLAIM_NO", length = 20)
	private String claimNo;
	
	@Column(name = "GL_CODE_DESC")
	private String glCodeDesc;
	
	@Column(name = "AMOUNT")
	private Double amount;
	
	@Column(name = "ACCOUNT_RULE_CODE")
	private String accountRuleCode;
	
	@Column(name = "ACCOUNT_TYPE")
	private String accountType;
	
	@Column(name = "GL_CODE")
	private String glCode;
	
	@Column(name = "ACCOUNT_RULE_CODE_DESC")
	private String accountRuleCodeDesc;
	
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "CLAIM_ONBOARD_NO", length = 20)
	private String claimOnBoardNo;
	
}
