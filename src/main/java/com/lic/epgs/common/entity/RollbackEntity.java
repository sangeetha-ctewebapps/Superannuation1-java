/**
 * 
 */
package com.lic.epgs.common.entity;

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
 * @author Karthick M
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ROLLBACK_MASTER")
public class RollbackEntity {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLLBACK_MASTER_SEQ")
	@SequenceGenerator(name = "ROLLBACK_MASTER_SEQ", sequenceName = "ROLLBACK_MASTER_SEQ", allocationSize = 1)
	@Column(name = "ROLLBACK_ID", length = 20)
	private Long rollbackId;

	@Column(name = "MODULE_NAME")
	private String moduleName;

	@Column(name = "REFERENCE_NO")
	private String referenceNo;

	@Column(name = "ACCOUNTING_STATUS")
	private String accountingStatus;

	@Column(name = "ANNUITY_STATUS")
	private String annuityStatus;

	@Column(name = "FUND_STATUS")
	private String fundStatus;

	@Column(name = "INTERNAL_MODULE_STATUS")
	private String internalModuleStatus;

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
