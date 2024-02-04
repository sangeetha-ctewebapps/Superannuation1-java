package com.lic.epgs.policy.old.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@Entity
//@Table(name = "POL_DEPOSIT")
public class PolicyDepositEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_DEPO_M_SEQUENCE")
//	@SequenceGenerator(name = "POL_DEPO_M_SEQUENCE", sequenceName = "POL_DEPO_M_SEQ", allocationSize = 1)
	@Column(name = "DEPOSIT_ID")
	private Long depositId;

	@Column(name = "POLICY_ID")
	private Long policyId;

	@Column(name = "COLLECTION_NO")
	private String collectionNo;

	@Column(name = "COLLECTION_DT")
	private Date collectionDate;

	@Column(name = "AMOUNT", precision = 20, scale = 8)
	private BigDecimal amount = BigDecimal.ZERO;

	@Column(name = "ADJESTMENT_AMOUNT", precision = 20, scale = 8)
	private BigDecimal adjestmentAmount = BigDecimal.ZERO;

	@Column(name = "AVAILABLE_AMOUNT", precision = 20, scale = 8)
	private BigDecimal availableAmount = BigDecimal.ZERO;

	@Column(name = "TRANSACTION_MODE")
	private String transactionMode;

	@Column(name = "COLLECTION_STATUS")
	private String collectionStatus;

	@Column(name = "CHEQUE_REALISATION_DT")
	private Date chequeRealisationDate;

	@Column(name = "REMARK")
	private String remark;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "ZERO_ID")
	private Boolean zeroId = Boolean.FALSE;

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
