package com.lic.epgs.policy.entity;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "POLICY_FREQUENCY_EFFCT_TEMP")
public class PolicyFrequencyEffectiveTempEntity implements Serializable{

	
 
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_F_E_T_SEQ")
	@SequenceGenerator(name = "POL_F_E_T_SEQ", sequenceName = "POL_F_E_T_SEQ", allocationSize = 1)
	@Column(name = "POL_FREQUENCY_ID")
	private Long polFrequencyId;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	
	@Column(name = "FREQUENCY")
	private String frequency;

	@Column(name = "EFFECTIVE_DATE")
	private Date effectiveDate;
	@Column(name = "STATUS")
	private String status;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "IS_ACTIVE")
	private Boolean isActive = Boolean.TRUE;
}
