package com.lic.epgs.claim.temp.entity;

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
@Table(name = "TEMP_CLAIM_NOMINEE_BANK")
public class ClaimNomineeTempBankEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEMP_CLAIM_NOM_BANK_SEQ")
	@SequenceGenerator(name = "TEMP_CLAIM_NOM_BANK_SEQ", sequenceName = "TEMP_CLAIM_NOM_BANK_SEQ", allocationSize = 1)
	@Column(name ="BANK_ACCOUNT_ID",length=50)
	private Long bankAccountId;
	@Column(name ="ACCOUNT_NUMBER",length=255 )
	private String accountNumber;
	@Column(name ="ACCOUNT_TYPE",length=255 )
	private String accountype;
	@Column(name ="BANK_ADDRESS_1",length=255 )
	private String bankAddress1;
	@Column(name ="BANK_ADDRESS_2",length=255 )
	private String bankAddress2;
	@Column(name ="BANK_ADDRESS_3",length=255 )
	private String bankAddress3;
	@Column(name ="BANK_BRANCH",length=255 )
	private String bankBranch;
	@Column(name ="BANK_NAME",length=255 )
	private String bankName;
	@Column(name ="COUNTRY_CODE",length=255 )
	private String countryCount;
	@Column(name ="CREATED_BY",length=50 )
	private String createdBy;
	@Column(name ="CREATED_ON")
	private Date createdOn;
	@Column(name ="EMAIL_ID",length=255 )
	private String emailId;
	@Column(name ="IFSC_CODE",length=255 )
	private String ifscCode;
	@Column(name ="LANDLINE_NUMBER",length=255 )
	private String landLineNumber;
	@Column(name ="MEMBER_ID",length=10)
	private Long memberId;
	@Column(name ="MODIFIED_BY",length=50 )
	private String modifiedBy;
	@Column(name ="MODIFIED_ON")
	private Date modifiedOn;
	@Column(name ="NOMINEE_ID",length=10)
	private Long nomineeId;
	@Column(name ="STD_CODE",length=255 )
	private String stdCode;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "CLAIM_MEMBER_ID", nullable = true)
////	private TempClaimMbrEntity claimMbrEntity;
	
}
