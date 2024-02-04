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

/**
 * @author pradeepramesh
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "POLICY_SERVICE_DOC")
public class PolicyServiceDocumentEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_DOC_M_SEQUENCE")
	@SequenceGenerator(name = "POL_DOC_M_SEQUENCE", sequenceName = "POL_DOC_M_SEQ", allocationSize = 1)
	@Column(name = "DOCUMENT_ID")
	private Long documentId;
	
	@Column(name = "POLICY_ID")
	private Long policyId;
	
	@Column(name = "SERVICE_ID")
	private Long serviceId;
	
	@Column(name = "SERVICE_NUMBER")
	private String serviceNumber;
	
	
	@Column(name = "REQUIREMENT")
	private String requirement;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "DOCUMENT_INDEX")
	private String documentIndex;
	
	@Column(name = "FOLDER_INDEX", length = 50)
	private Integer folderIndex;
	
	

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
