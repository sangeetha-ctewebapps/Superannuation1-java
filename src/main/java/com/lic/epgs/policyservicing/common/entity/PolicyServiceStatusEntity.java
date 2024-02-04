package com.lic.epgs.policyservicing.common.entity;

import java.io.Serializable;

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
@Table(name = "POLICY_SERVICE_STATUS")
public class PolicyServiceStatusEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POL_SRV_STATUS_ID_SE")
	@SequenceGenerator(name = "POL_SRV_STATUS_ID_SE", sequenceName = "POL_SRV_STATUS_ID_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private String id;
	@Column(name = "CODE")
	private String code;
	@Column(name = "TYPE")
	private String type;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "DESCRIPTION1")
	private String description1;
	@Column(name = "NAME")
	private String name;
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

}
