///**
// * 
// */
//package com.lic.epgs.policyservicing.policylvl.entity.memberaddition;
//
//import java.io.Serializable;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.Set;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.OneToMany;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.Table;
//
//import com.lic.epgs.policyservicing.common.entity.PolicyServiceNotesTempEntity;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
///**
// * @author Karthick M
// *
// */
//
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@Entity
//@Table(name = "POLICY_SRV_MBR_ADD_TEMP")
//public class PolicyServiceMemberAdditionTempEntity implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//
//	
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_SRV_MBR_ADD_TEMP_SEQUENCE")
//	@SequenceGenerator(name = "POL_SRV_MBR_ADD_TEMP_SEQUENCE", sequenceName = "POL_SRV_MBR_ADD_TEMP_SEQUENCE", allocationSize = 1)
//	@Column(name="MEMBER_ADDITION_ID")
//	private Long memberAdditionId;
//	
//
//	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//	@JoinColumn(name = "MEMBER_ADDITION_ID", referencedColumnName = "MEMBER_ADDITION_ID")
//	private Set<PolicyServiceMemberTempEntity> policyServiceMemberTemp = new HashSet<>();
//	
//	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//	@JoinColumn(name = "MEMBER_ADDITION_ID", referencedColumnName = "MEMBER_ADDITION_ID")
//	private Set<PolicyServiceNotesTempEntity> policyServiceNotesTemp = new HashSet<>();
//	
//	
//	@Column(name = "MEMBER_ADDITION_STATUS")
//	private String memberAdditionStatus;
//	
//	
//	
//	
//	@Column(name = "SERVICE_ID")
//	private Long serviceId;
//	
//	@Column(name = "SERVICE_NUMBER")
//	private String serviceNumber;
//		
//	@Column(name = "SERVICE_STATUS")
//	private String serviceStatus;
//	
//	
//
//
//	@Column(name = "POLICY_ID")
//	private Long policyId;
//	
//	@Column(name = "POLICY_NUMBER")
//	private String policyNumber;
//	
//	@Column(name = "POLICY_STATUS")
//	private String policyStatus;
//	
//	
//	
//	
//	@Column(name = "MPH_CODE")
//	private String mphCode;
//
//	@Column(name = "MPH_NAME")
//	private String mphName;
//	
//	@Column(name = "PRODUCT")
//	private String product;
//	
//	@Column(name = "LINE_OF_BUSINESS")
//	private String lineOfBusiness;
//	
//	
//	@Column(name = "EMPLOYEE_CONTRIBUTION ")
//	private Long employeeContribution;
//	
//	@Column(name = "EMPLOYER_CONTRIBUTION")
//	private Long employerContribution;
//
//	@Column(name = "VOLUNTARY_CONTRIBUTION")
//	private Long voluntaryContribution;
//	
//	@Column(name = "TOTAL_CONTRIBUTION ")
//	private Long totalContribution;
//	
//	
//	
//	@Column(name = "MODIFIED_BY")
//	private String modifiedBy;
//
//	@Column(name = "MODIFIED_ON")
//	private Date modifiedOn = new Date();
//
//	@Column(name = "CREATED_BY")
//	private String createdBy;
//
//	@Column(name = "CREATED_ON")
//	private Date createdOn = new Date();
//
//	@Column(name = "IS_ACTIVE")
//	private Boolean isActive;
//	
//	@Column(name = "UNIT_CODE")
//	private String unitCode;
//
//	@Column(name = "REJECTION_REASON_CODE")
//	private String rejectionReasonCode;
//	
//	@Column(name = "REJECTION_REMARKS")
//	private String rejectionRemarks;
//	
//	
//	
//	@Column(name = "POLICY_MBR_NO")
//	private String policyMemberNo;
//
//}
