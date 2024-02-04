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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "POLICY_FREQUENCY_DETAILS_TEMP")
public class PolicyFrequencyDetailsTempEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_FREQ_T_SEQUENCE")
	@SequenceGenerator(name = "POL_FREQ_T_SEQUENCE", sequenceName = "POL_FREQ_T_SEQ", allocationSize = 1)
	@Column(name = "FREQUENCY_ID")
	private Long frequencyId;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	
	@Column(name = "FREQUENCY")
	private String frequency;

	@Column(name = "FREQUENCY_DATES")
	private Date frequencyDates;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "PROGRESS")
	private String progress;
	
	@Column(name = "POLICY_COMMENCEMENT_DATE")
	private Date policyCommencementDate;
	
	@Column(name = "POLICY_END_DATE")
	private Date policyEndDate;

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
