package com.lic.epgs.claim.temp.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "TEMP_CLAIM_ONBORADING")
public class TempClaimOnboardingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEMP_CLAIM_ONB_SEQUENCE")
	@SequenceGenerator(name = "TEMP_CLAIM_ONB_SEQUENCE", sequenceName = "TEMP_CLAIM_ONB_SEQUENCE", allocationSize = 1)
	@Column(name = "CLAIM_ONBOARD_ID", length = 20)
	private Long claimOnBoardId;

	@Column(name = "CLAIM_ONBOARD_NO", length = 20)
	private String claimOnBoardNo;

	@Column(name = "INTIMATION_TYPE", length = 5)
	private Integer initimationType;

	@Column(name = "ONBOARDING_DT", updatable = false)
	private Date onboardingDate;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "CREATED_BY", length = 100)
	private String createdBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn;

	@Column(name = "MODIFIED_BY", length = 50)
	private String modifiedBy;

	@Column(name = "INTIMATION_NO", length = 20)
	private String initiMationNo;

	@Column(name = "ONBOARDING_STATUS")
	private Integer onboardingStatus;
	
	@Column(name = "IS_ACTIVE", length = 10)
	private Boolean isActive;

	@OneToMany(mappedBy = "claimOnboarding")
	private List<TempClaimEntity> claim;
	
	@Column(name = "BATCH_ID", length = 20)
	private Long batchId;
}
