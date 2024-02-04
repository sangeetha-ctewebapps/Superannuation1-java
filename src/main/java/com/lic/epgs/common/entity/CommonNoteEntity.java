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
@Table(name = "NOTE_DETAILS")
public class CommonNoteEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTEID_MSEQUENCE")
	@SequenceGenerator(name = "NOTEID_MSEQUENCE", sequenceName = "NOTEID_MSEQUENCE", allocationSize = 1)
	@Column(name = "NOTE_ID", length = 10)
	private Integer noteId;

	@Column(name = "NOTE_CONTENTS", length = 2050)
	private String noteContents;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "MODIFIED_BY", length = 50)
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();

	@Column(name = "CREATED_BY", length = 50)
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();
	@Column(name = "MERGE_ID")
	private Long mergeId;
	
	@Column(name = "SURRENDER_ID", length = 10)
	private Long surrenderId;
	
	@Column(name = "TRNSFR_ID", length = 10)
	private Long trnsfrId;
}
