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

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "DOCUMENT_MASTER")
public class CommonDocumentMasterEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COM_DOC_M_SEQUENCE")
	@SequenceGenerator(name = "COM_DOC_M_SEQUENCE", sequenceName = "COM_DOC_M_SEQ", allocationSize = 1)
	@Column(name = "DOCUMENT_ID")
	private Long documentId;

	@Column(name = "PRODUCT_ID")
	private Long productId;

	@Column(name = "VARIANT_ID")
	private Long variantId;

	@Column(name = "DOCUMENT_CATEGORY")
	private String documentCategory;

	@Column(name = "DOCUMENT_SUB_CATEGORY")
	private String documentSubCategory;

	@Column(name = "DOCUMENT_TYPE")
	private String documentType;

	@Column(name = "IS_MANDATORY")
	private Boolean isMandatory;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

}
