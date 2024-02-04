/**
 * 
 */
package com.lic.epgs.common.entity;

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
 * @author vijayt
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "INTEREST_ERROR_TXN_DETAILS")
public class InterestErrorDetailsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INTEREST_ERROR_TXN_GEN")
	@SequenceGenerator(name = "INTEREST_ERROR_TXN_GEN", sequenceName = "INTEREST_ERROR_TXN_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "ref_number")
	private String refNumber;

	@Column(name = "policy_number")
	private String policyNo;

	@Column(name = "member_id")
	private String memberId;

	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "modified_on")
	private Date modifiedOn;

	@Column(name = "is_fail")
	private Boolean isFail;

	@Column(name = "no_of_attempt")
	private Integer noOfAttempt;

	@Column(name = "txn_type")
	private String txnType;

	@Column(name = "ipAddress")
	private String ipAddress;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "error_description")
	private String errorDescription;

}