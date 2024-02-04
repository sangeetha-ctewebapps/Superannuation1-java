package com.lic.epgs.quotation.entity;

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

/**
 *
 * @author KrunalGothiwala
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "QUO_LIABILITY_DETAIL")
public class QuotationLiabilityEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUOTATION_LIABILITY_SEQ")
	@SequenceGenerator(name = "QUOTATION_LIABILITY_SEQ", sequenceName = "QUOTATION_LIABILITY_SEQ", allocationSize = 1)
	@Column(name = "LIABILITY_ID", length = 10)
	private Integer liabilityId;

	@Column(name = "LIABILITY")
	private String liability;

	@Column(name = "INTERNAL_VALUATION")
	private BigDecimal internalValuation= BigDecimal.ZERO;

	@Column(name = "EXTERNAL_VALUATION")
	private BigDecimal externalaluation= BigDecimal.ZERO;


	@Column(name = "VALIDITY")
	private Integer validity;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

}
