/**
 * 
 */
package com.lic.epgs.regularadjustmentcontribution.entity;

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
@Table(name = "REG_AC_BATCH_TEMP")
public class RegularAdjustmentContributionBatchEntity {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REG_AC_BATCH_TEMP_SEQ")
	@SequenceGenerator(name = "REG_AC_BATCH_TEMP_SEQ", sequenceName = "REG_AC_BATCH_TEMP_SEQ", allocationSize = 1, initialValue = 1000)
	@Column(name = "BATCH_ID", length = 10)
	private Long batchId;

	@Column(name = "SUCCESS_COUNT", length = 20)
	private Long successCount = 0L;

	@Column(name = "FAILED_COUNT", length = 20)
	private Long failedCount = 0L;

	@Column(name = "TOTAL_COUNT", length = 20)
	private Long totalCount = 0L;

	@Column(name = "FILE_NAME")
	private String fileName;
	
	
	
	@Lob
	@Column(name = "SUCCESS_METADETA")
	private byte[] successFile;
	
	@Lob
	@Column(name = "RAW_METADETA")
	private byte[] rawFile;

	@Lob
	@Column(name = "FAILED_METADETA")
	private byte[] failedFile;
	
	
	

	@Column(name = "CREATED_BY", length = 50)
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "MODIFIED_BY", length = 50)
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();
	
	
	@Column(name = "IS_ACTIVE", length = 10)
	private Boolean isActive;
	
	@Column(name = "REG_CON_ID")
	private Long regularContributionId;

}
