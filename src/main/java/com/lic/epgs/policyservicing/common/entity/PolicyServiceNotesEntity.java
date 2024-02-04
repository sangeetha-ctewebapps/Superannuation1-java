package com.lic.epgs.policyservicing.common.entity;

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
@Table(name = "POLICY_SERVICE_NOTE")
public class PolicyServiceNotesEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_SRV_NOTE_M_SEQUENCE")
	@SequenceGenerator(name = "POL_SRV_NOTE_M_SEQUENCE", sequenceName = "POL_SRV_NOTE_M_SEQ", allocationSize = 1)
	@Column(name = "SERVICE_NOTE_ID")
	private Long serviceNoteId;
	
	@Column(name = "SERVICE_ID")
	private Long serviceId;

	@Column(name = "POLICY_ID")
	private Long policyId;
	
	@Column(name = "NOTE_CONTENTS")
	private String noteContents;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "CONVERSION_ID")
	private Long conversionId;
	
	@Column(name = "MERGE_ID")
	private Long mergeId;

	@Column(name="MEMBER_ADDITION_ID")
	private Long memberAdditionId;
	
	@Column(name = "POLICY_DTLS_ID")
	private Long policyDtlsId;
	
	@Column(name = "FREELOOK_ID")
	private Long freeLookId;
	
	@Column(name = "TRNSFR_ID")
	private Long trnsfrId;
}
