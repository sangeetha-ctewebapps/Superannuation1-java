package com.lic.epgs.policy.entity;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.math.BigDecimal;
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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "POLICY_DEPOSIT")
public class PolicyDepositEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_DEPO_M_SEQUENCE")
	@SequenceGenerator(name = "POL_DEPO_M_SEQUENCE", sequenceName = "POL_DEPO_M_SEQ", allocationSize = 1)
	@Column(name = "DEPOSIT_ID")
	private Long depositId;

	@Column(name = "POLICY_ID")
	private Long policyId;
	@Column(name = "TEMP_POLICY_ID")
	private Long tempPolicyId;
	
	@Column(name = "ADJ_CON_ID")
	private Long adjustmentContributionId;
	@Column(name = "REG_CON_ID")
	private Long regularContributionId;
	
	@Column(name = "CONTRIBUTION_TYPE ")
	private String contributionType;

	@Column(name = "COLLECTION_NO")
	private String collectionNo;

	@Column(name = "COLLECTION_DATE")
	private Date collectionDate;

	@Column(name = "COLLECTION_STATUS")
	private String collectionStatus;

	@Column(name = "CHALLAN_NO")
	private String challanNo;

	@Column(name = "DEPOSIT_AMOUNT", precision = 20, scale = 8)
	private BigDecimal depositAmount = BigDecimal.ZERO;

	@Column(name = "ADJUSTMENT_AMOUNT", precision = 20, scale = 8)
	private BigDecimal adjustmentAmount = BigDecimal.ZERO;

	@Column(name = "AVAILABLE_AMOUNT", precision = 20, scale = 8)
	private BigDecimal availableAmount = BigDecimal.ZERO;

	@Column(name = "TRANSACTION_MODE")
	private String transactionMode;

	@Column(name = "CHEQUE_REALISATION_DATE")
	private Date chequeRealisationDate;

	@Column(name = "REMARK")
	private String remark;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "ZERO_ID")
	private Boolean zeroId = Boolean.FALSE;
	
	@Column(name = "IS_DEPOSIT ")
	private Boolean isDeposit = Boolean.FALSE;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive = Boolean.TRUE;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();
	
	@Column(name = "VOUCHER_NO")
	private String voucherNo;
	
	@Column(name = "VOUCHER_DATE")
	private Date voucherDate;

}
