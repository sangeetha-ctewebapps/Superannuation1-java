package com.lic.epgs.payout.temp.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "TEMP_PAYOUT_DOCUMENT_DETAIL")
public class TempPayoutDocumentDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEMP_PAYOUT_DOCU_SEQ")
	@SequenceGenerator(name = "TEMP_PAYOUT_DOCU_SEQ", sequenceName = "TEMP_PAYOUT_DOCU_SEQ", allocationSize = 1)
	@Column(name = "DOCUMENT_ID", length = 10)
	private Long documentId;

	@Column(name = "DOCUMENT_NO", length = 20)
	private String documentNo;

	@Column(name = "DOCUMENT_NAME", length = 90)
	private String documentName;

	@Column(name = "DOCUMENT_TYPE", length = 10)
	private Long documentType;

	@Column(name = "ISSUED_BY", length = 10)
	private String issuedBy;

	@Column(name = "ISSUED_DATE")
	private Date issuedDate;

	@Column(name = "PAYOUT_NO", length = 20)
	private String payoutNo;

	@Column(name = "DOC_STATUS", length = 5)
	private Integer docStatus;

	@Column(name = "REQUIREMENT", length = 255)
	private String requirement;

	@Column(name = "DOCUMENT_INDEX", length = 255)
	private String documentIndex;

	@Column(name = "FOLDER_INDEX", length = 50)
	private Integer folderIndex;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "CREATED_BY", length = 100)
	private String createdBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@Column(name = "MODIFIED_BY", length = 50)
	private String modifiedBy;

	@Column(name = "MERGE_ID", length = 10)
	private Long mergeId;

	@Column(name = "IS_DELETED", length = 5)
	private Boolean isDeleted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PAYOUT_ID", nullable = false)
	private TempPayoutEntity payout;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
}

