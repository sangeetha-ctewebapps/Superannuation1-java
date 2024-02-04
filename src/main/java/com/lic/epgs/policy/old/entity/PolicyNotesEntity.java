package com.lic.epgs.policy.old.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@Entity
//@Table(name = "POL_NOTE")
public class PolicyNotesEntity implements Serializable {

	private static final long serialVersionUID = 1L;
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_NOTE_M_SEQUENCE")
//	@SequenceGenerator(name = "POL_NOTE_M_SEQUENCE", sequenceName = "POL_NOTE_M_SEQ", allocationSize = 1)
	@Column(name = "POLICY_NOTE_ID")
	private Long policyNoteId;

	@Column(name = "POLICY_ID")
	private Long policyId;

	@Column(name = "NOTE_CONTENTS")
	private String noteContents;
	
	@Column(name = "SERVICE_ID")
	private Long serviceId;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive= Boolean.TRUE;

}
