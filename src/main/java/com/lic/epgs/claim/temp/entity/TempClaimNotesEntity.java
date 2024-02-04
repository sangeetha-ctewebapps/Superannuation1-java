package com.lic.epgs.claim.temp.entity;

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
@Table(name = "TEMP_CLAIM_NOTES")
public class TempClaimNotesEntity {

	@Id
	@Column(name = "CLAIM_NOTE_ID", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEMP_CLAIM_NOTE_SEQUENCE")
	@SequenceGenerator(name = "TEMP_CLAIM_NOTE_SEQUENCE", sequenceName = "TEMP_CLAIM_NOTE_SEQUENCE", allocationSize = 1)
	private Long id;

	@Column(name = "CLAIM_NO", nullable = false, length = 20)
	private String claimNo;

	@Column(name = "NOTE_CONTENTS", length = 1000)
	private String noteContents;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "CREATED_BY", length = 100)
	private String createdBy;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
}
