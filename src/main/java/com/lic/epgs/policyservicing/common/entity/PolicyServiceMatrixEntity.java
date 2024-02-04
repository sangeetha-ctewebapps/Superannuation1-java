package com.lic.epgs.policyservicing.common.entity;

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

/**
 * @author pradeepramesh
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "POLICY_SERVICE_MATRIX")
public class PolicyServiceMatrixEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_SRV_MA_SEQUENCE")
	@SequenceGenerator(name = "POL_SRV_MA_SEQUENCE", sequenceName = "POL_SRV_MA_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "SERVICE_TYPE")
	private String serviceType;
	
	@Column(name = "SERVICE_STATUS")
	private String serviceStatus;
	
	@Column(name = "POLICYCONVERSION")
	private String policyConversion;
	
	@Column(name = "POLICYMERGER")
	private String policyMerger;
	
	@Column(name = "FREELOOKCANCEL")
	private String freeLookCancel;
	
	@Column(name = "POLICYDETAILSCHANGE")
	private String policyDetailChange;
	
	@Column(name = "MEMBEROFADDITION")
	private String memberOfAddition;
	
	@Column(name = "MEMBERTRASFERINOUT")
	private String memberTransferInOut;
	
	@Column(name = "CLAIM")
	private String claim;
	
	@Column(name = "SERVICE_ACCEPT")
	private String serviceAccept;

	@Column(name = "SERVICE_EFFECTIVE_DATE")
	private Date serviceEffectiveDate;
//	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinColumn(name = "SERVICE_TYPE", referencedColumnName = "SERVICE_TYPE")
//	private Set<PolicyServiceMatrixEntity> serviceMap = new HashSet<>();

}
