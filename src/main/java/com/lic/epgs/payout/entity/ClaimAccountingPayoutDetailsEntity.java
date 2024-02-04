package com.lic.epgs.payout.entity;

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
 * @author Logesh.D 04-04-2023
 *
 */


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CLAIM_ACCOUNTING_PAYOUT_DETAILS")
public class ClaimAccountingPayoutDetailsEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYOUT_DETAILS_SEQ")
	@SequenceGenerator(name = "PAYOUT_DETAILS_SEQ", sequenceName = "PAYOUT_DETAILS_SEQ", allocationSize = 1)
	@Column(name = "PAYOUT_DETAILS_ID")
	private Long payoutDetailsId;

	@Column(name = "REFNO")
	private String refNo;
	
	@Column(name = "BENEFIARY_PAYMENT_ID")
	private Long benefiaryPaymentId;
	
	@Column(name = "PAYMENT_ID_OLD")
	private Long paymentIdOld;
	
	@Column(name = "JOURNAL_VOUCHER_NO")
	private String journalVoucherNo;
	
	
	@Column(name = "CLAIM_ONBOARDING_NUMBER")
	private String claimOnboardingNumber;
	
	
	@Column(name = "STATUS")
	private String status;
	
	
	@Column(name = "USERNAME")
	private String username;
	
	
	@Column(name = "MESSAGE")
	private String message;
	
	
	@Column(name = "REJECTION_REASON")
	private String rejectReason;
	
	
	@Column(name = "PAYMENT_MODE")
	private String paymentMode;
	
	
	@Column(name = "UTR_NO")
	private String utrNo;
	
	
	@Column(name = "PAYMENT_DATE")
	private Date paymentDate;
	
	
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	


}
