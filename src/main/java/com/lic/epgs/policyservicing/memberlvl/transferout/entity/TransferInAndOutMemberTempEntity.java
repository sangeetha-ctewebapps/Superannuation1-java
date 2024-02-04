/**
 * 
 */
package com.lic.epgs.policyservicing.memberlvl.transferout.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name = "TRANSFER_IN_OUT_MEMBER_TEMP")
public class TransferInAndOutMemberTempEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRNSFR_IN_OUT_MBR_TEMP_SEQ")
	@SequenceGenerator(name = "TRNSFR_IN_OUT_MBR_TEMP_SEQ", sequenceName = "TRNSFR_IN_OUT_MBR_TEMP_SEQ", allocationSize = 1)
	@Column(name = "TRNSFR_MEMBER_ID")
	private Long trnsfrMemberId;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATED_ON")
	private Date createdOn;
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "MEMBER_ID")
	private Long memberId;
	@Column(name = "MEMBER_NO")
	private String memberNo;
	@Column(name = "MEMBER_NAME")
	private String memberName;
	
	@Column(name = "SOURCE_POLICY_ID")
	private Long sourcePolicyId;
	@Column(name = "SOURCE_POLICY_NO")
	private String sourcePolicyNo;
	
	@Column(name = "DEST_POLICY_ID")
	private Long destPolicyId;
	@Column(name = "DEST_POLICY_NO")
	private String destPolicyNo;
	
	@Column(name = "MPH_NAME")
	private String mphName;
	@Column(name = "PRODUCT_ID")
	private Long productId;
	@Column(name = "PROD_VARIANT")
	private Long prodVariant;
	@Column(name = "CATEGORY")
	private Long category;
	
	@Column(name = "EMPLOYER_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal employerContribution;
	@Column(name = "EMPLOYEE_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal employeeContribution;
	@Column(name = "VOLUNTARY_CONTRIBUTION", precision = 20, scale = 8)
	private BigDecimal voluntaryContribution;
	
	@Column(name = "TRNSFR_AMOUNT")
	private Long trnsfrAmount;
	
	@Column(name = "MEMBER_SHIP_ID")
	private String memberShipId;
	
	@Column(name = "PAN", length = 10)
	private String pan;
	@Column(name = "AADHAR_NUMBER", length = 12)
	private Long aadharNumber;
	@Column(name = "PHONE", length = 10)
	private Long phone;
	
	
	@OneToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "TRNSFR_ID")
	private TransferInAndOutMasterTempEntity transferoutTemp;

}
