/**
 * 
 */
package com.lic.epgs.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Karthick M
 *
 */


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "SAMPLE_FILE_MASTER")
public class SampleFileMasterEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAMPLE_FILE_MASTER_SEQ")
	@SequenceGenerator(name = "SAMPLE_FILE_MASTER_SEQ", sequenceName = "SAMPLE_FILE_MASTER_SEQ", allocationSize = 1)
	@Column(name = "FILE_ID", length = 20)
	private Long fileId;
	
	
	@Column(name = "MODULE_NAME")
	private String moduleName;
	
	@Lob
	@Column(name = "ACTUAL_FILE")
	private byte[] actualFile;
	

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;
	
	
	@Column(name = "IS_ACTIVE", length = 10)
	private Boolean isActive;

	
}
