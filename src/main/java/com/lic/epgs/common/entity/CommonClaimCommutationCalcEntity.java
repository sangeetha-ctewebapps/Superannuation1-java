package com.lic.epgs.common.entity;

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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="COMMON_CLAIM_COMMUTATION_CALC")

public class CommonClaimCommutationCalcEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "claim_Commutation_Calc_Seq")
	@SequenceGenerator(name = "claim_Commutation_Calc_Seq", sequenceName = "CLAIM_COMMUTATION_CALC_SEQ", allocationSize = 1)	
	@Column(name = "ID")
	private String id;
	@Column(name = "CODE")
	private String code;
	@Column(name = "DESCRIPTION")
	private String description;	
	@Column(name = "IS_Mandatory")
	private Boolean isMandatory;
	@Column(name = "Status")
	private Boolean status; 
}