package com.lic.epgs.topupda.entity;

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

/**
*
* @author Dhanush
*
*/

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TOP_UP_DA_NOTES_TEMP")
public class TopupdaNotesTempEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOPUPDA_NOTE_TEMP_SEQUENCE")
	@SequenceGenerator(name = "TOPUPDA_NOTE_TEMP_SEQUENCE", sequenceName = "TOPUPDA_NOTE_TEMP_SEQ", allocationSize = 1)
	@Column(name = "TOPUPDA_NOTE_ID")
	private Long topupdaNoteId;

	@Column(name = "NOTE_CONTENTS")
	private String noteContents;
	
	@Column(name = "TOPUP_ID")
	private Long topupId;
	
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
