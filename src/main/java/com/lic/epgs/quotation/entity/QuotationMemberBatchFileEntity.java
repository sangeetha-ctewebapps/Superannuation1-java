//package com.lic.epgs.quotation.entity;
//
//import java.io.Serializable;
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Lob;
//import javax.persistence.MapsId;
//import javax.persistence.OneToOne;
//import javax.persistence.Table;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@Entity
//@Table(name = "QUO_MBR_BATCH_FILE")
//public class QuotationMemberBatchFileEntity implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	private Long id;
//
//	@OneToOne
//	@MapsId
//	private QuotationMemberBatchEntity batchFile;
//
//	@Lob
//	@Column(name = "SUCCESS_METADETA")
//	private byte[] successFile;
//	
//	@Lob
//	@Column(name = "RAW_METADETA")
//	private byte[] rawFile;
//
//	@Lob
//	@Column(name = "FILED_METADETA")
//	private byte[] failedFile;
//
//	@Column(name = "CREATED_BY")
//	private String createdBy;
//
//	@Column(name = "CREATED_ON")
//	private Date createdOn = new Date();
//
//	@Column(name = "MODIFIED_BY")
//	private String modifiedBy;
//
//	@Column(name = "MODIFIED_ON")
//	private Date modifiedOn = new Date();
//	
//	
//	@Column(name = "IS_ACTIVE")
//	private Boolean isActive;
//	
//	
//	
//}
