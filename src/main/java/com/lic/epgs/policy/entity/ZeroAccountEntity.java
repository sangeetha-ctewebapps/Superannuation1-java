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
@Table(name = "ZERO_ACC")
public class ZeroAccountEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Z_ACC_M_SEQUENCE")
	@SequenceGenerator(name = "Z_ACC_M_SEQUENCE", sequenceName = "Z_ACC_M_SEQ", allocationSize = 1)
	@Column(name = "ZERO_ACC_ID ")
	private Long zeroAccId;
	
	@Column(name = "POLICY_ID ")
	private Long policyId;
	
	@Column(name = "ZERO_ID_AMOUNT ")
	private BigDecimal zeroIdAmount = BigDecimal.ZERO;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive = Boolean.TRUE;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();
}
