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

/**
 *
 * @author KrunalGothiwala
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "COMMON_DOCS_TEMP")
public class CommonDocsTempEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMONID_DOCS_TSEQUENCE")
	@SequenceGenerator(name = "COMMONID_DOCS_TSEQUENCE", sequenceName = "COMMONID_DOCS_TSEQUENCE", allocationSize = 1)
	@Column(name = "DOC_ID", length = 10)
	private Integer docId;

	@Column(name = "REQUIREMENT", length = 255)
	private String requirement;

	@Column(name = "QUOTATION_ID", length = 10)
	private Integer quotationId;

	@Column(name = "STATUS", length = 10)
	private String status;

	@Column(name = "COMMENTS", length = 2000)
	private String comments;

	@Column(name = "MODIFIED_BY", length = 50)
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();

	@Column(name = "CREATED_BY", length = 50)
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "DOCUMENT_INDEX")
	private String documentIndex;

	@Column(name = "FOLDER_INDEX", length = 50)
	private Integer folderIndex;

	@Column(name = "MERGE_ID", length = 10)
	private Long mergeId;

	@Column(name = "SURRENDER_ID", length = 10)
	private Long surrenderId;
	
	@Column(name = "DOCUMENT_NUMBER", length = 10)
	private Integer documentNumber;
	
	@Column(name = "IS_ACTIVE", length = 10)
	private Boolean isActive;
	
	@Column(name = "CONV_ID", length = 10)
	private Long conversionId;
	
}
