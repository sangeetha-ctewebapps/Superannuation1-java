package com.lic.epgs.quotation.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "POLICY_SERVICE_MATRIX_NEW")
public class PolicyServiceMatrixNewEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "CURRENT_SERVICE")
	private String currentService;

	@Column(name = "ONGOING_SERVICE")
	private String ongoingService;

	@Column(name = "IS_ALLOWED")
	private String isAllowed;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "CREATED_BY")
	private String createdBy;
}
