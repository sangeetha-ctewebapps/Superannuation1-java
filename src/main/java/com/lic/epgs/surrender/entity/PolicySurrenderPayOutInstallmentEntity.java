package com.lic.epgs.surrender.entity;

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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "SUR_PAYOUT_INSTALLMENTS")
public class PolicySurrenderPayOutInstallmentEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYOUT_INSTALLMENTS_SEQUENCE")
	@SequenceGenerator(name = "PAYOUT_INSTALLMENTS_SEQUENCE", sequenceName = "PAYOUT_INSTALLMENTS_SEQUENCE", allocationSize = 1)
	@Column(name = "INSTALLMT_ID", length = 10)
	private Long installmtId;
	@Column(name = "PAYOUT_ID", length = 10)
	private Long payoutId;
	@Column(name = "POLICY_ID", length = 10)
	private Long policyId;
	@Column(name = "INSTALLMT_NO", length = 20)
	private String installmtNo;
	@Column(name = "INSTALLMT_DT")
	private Date installmtDt;
	@Column(name = "FUND_PAYABLE", length = 20)
	private Long fundPayable;
	@Column(name = "ACCRUED_INT", length = 20)
	private Long accruedInt;
	@Column(name = "TOTAL_PAYABLE", length = 20)
	private Long totalPayable;
	@Column(name = "CREATED_BY", length = 10)
	private String createdBy;
	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();
	@Column(name = "MODIFIED_BY", length = 10)
	private String modifiedBy;
	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
}
