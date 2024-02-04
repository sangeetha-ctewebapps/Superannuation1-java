/**
 * 
 */
package com.lic.epgs.adjustmentcontribution.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "AC_BULK_ERROR_TEMP")
public class AdjustmentContributionBulkErrorEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AC_BULK_ERROR_TEMP_SEQ")
	@SequenceGenerator(name = "AC_BULK_ERROR_TEMP_SEQ", sequenceName = "AC_BULK_ERROR_TEMP_SEQ", allocationSize = 1)
	@Column(name = "ERROR_ID", length = 10)
	private Long errorId;
	
	@Column(name = "BATCH_ID")
	private Long batchId;
	
	@Column(name = "MEMBERSHIP_ID")
	private String membershipId;
	
	@Column(name = "LIC_ID")
	private String licId;
	
	@Column(name = "ERROR",length = 1000)
	private String error;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();


	@Column(name = "IS_ACTIVE")
	private Boolean isActive = Boolean.TRUE;
	
	

}
