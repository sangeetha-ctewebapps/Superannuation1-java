package com.lic.epgs.common.entity;

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
 * @author Logesh.D  07-04-2023
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ICODE_MASTER")
public class IcodeMasterEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ICODE_ID_SEQ")
	@SequenceGenerator(name = "ICODE_ID_SEQ", sequenceName = "ICODE_ID_SEQ", allocationSize = 1)
	@Column(name = "ICODE_ID")
	private Long icodeId;
	
	@Column(name = "VARIANT")
	private String variant;
	
	@Column(name = "PRODUCT_NAME")
	private String product;
	
	@Column(name = "ICODE_FOR_LOB")
	private Integer icodeForLob;
	
	@Column(name = "ICODE_FOR_PRODUCT_LINE")
	private Integer icodeForProductLine;

	@Column(name = "ICODE_FOR_VARIANT")
	private String icodeForVariant;

	@Column(name = "ICODE_FOR_BUSINESS_TYPE")
	private Integer icodeForBusinessType;
	
	@Column(name = "ICODE_FOR_PARTICIPATING_TYPE")
	private Integer icodeForParticipating;

	@Column(name = "ICODE_FOR_BUSINESS_SEGMENT")
	private Integer icodeForBusinessSegment;

	@Column(name = "ICODE_FOR_INVESTMENT_PORTFOLIO")
	private Integer icodeForInvestmentPortFolio;


	
	
	
	
	
	



}
