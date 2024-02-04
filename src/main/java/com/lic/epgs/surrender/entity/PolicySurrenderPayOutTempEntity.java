package com.lic.epgs.surrender.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@Table(name = "SUR_PAYOUT_TMP")
public class PolicySurrenderPayOutTempEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POLICY_PAYOUT_TEMP_SEQUENCE")
	@SequenceGenerator(name = "POLICY_PAYOUT_TEMP_SEQUENCE", sequenceName = "POLICY_PAYOUT_TEMP_SEQUENCE", allocationSize = 1)
	@Column(name = "PAYOUT_ID", length = 10)
	private Long payoutId;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "PAYOUT_ID", referencedColumnName = "PAYOUT_ID")
	private Set<PolicySurrenderPayOutInstallmentTempEntity> payoutInstallments = new HashSet<>();
	@Column(name = "POLICY_ID", length = 10)
	private Long policyId;
	@Column(name = "SURRENDER_ID", length = 10)
	private Long surrenderId;
	@Column(name = "SURRENDER_AMT_OLD", length = 20)
	private Long surrenderAmtCld;
	@Column(name = "SURRENDER_AMT_NEW", length = 20)
	private Long surrenderAmtNew;
	@Column(name = "SURRENDER_AMT_COMMENT", length = 200)
	private String surrenderAmtComment;
	@Column(name = "SURRENDER_CHRG_OLD", length = 20)
	private Long surrenderChrgOld;
	@Column(name = "SURRENDER_CHRG_NEW", length = 20)
	private Long surrenderChrgNew;
	@Column(name = "SURRENDER_CHRG_COMMENT", length = 200)
	private String surrenderChrgComment;
	@Column(name = "FIN_SURRENDER_OLD", length = 20)
	private Long finSurrenderOld;
	@Column(name = "FIN_SURRENDER_NEW", length = 20)
	private Long finSurrenderNew;
	@Column(name = "FIN_SURRENDER_COMMENT", length = 200)
	private String finSurrenderComment;
	@Column(name = "DT_OF_SURRENDER")
	private Date dtOfSurrender;
	@Column(name = "DT_DOC_SUBMIT")
	private Date dtDocSubmit;
	@Column(name = "SETTLEMENT_MODE", length = 3)
	private Long settlementMode;
	@Column(name = "TOT_MEMBERS", length = 10)
	private Long totMembers;
	@Column(name = "SURRENDER_AMT", length = 20)
	private Long surrenderAmt;
	@Column(name = "SURRENDER_CHARGES", length = 20)
	private Long surrenderCharges;
	@Column(name = "MARKET_VALUE_ADJ", length = 20)
	private Long marketValueAdj;
	@Column(name = "FIN_SURRENDER_AMT", length = 20)
	private Long finSurrenderAmt;
	@Column(name = "MODE_OF_PAY", length = 3)
	private String modeOfPay;
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
