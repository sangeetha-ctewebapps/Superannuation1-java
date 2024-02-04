package com.lic.epgs.common.entity;

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
 * @author imthiyaz
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "SA_APP_LOGS_ERROR")
public class AppLogErrorEntity {

	/**
	 *
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMON_APP_LOG_GEN")
	@SequenceGenerator(name = "COMMON_APP_LOG_GEN", sequenceName = "COMMON_APP_LOG_DT_SEQ", allocationSize = 1)
	@Column(name = "reference_id", length = 20)
	private Long referenceId;

	@Column(name = "TXN_TYPE", length = 255)
	private String txnType;

	@Column(name = "TXN_ID")
	private Long txnId;

	@Column(name = "module", length = 100)
	private String module;

	@Column(name = "URI", length = 500)
	private String uri;

	@Column(name = "TXN_NUMBER", length = 100)
	private String txnNumber;

	@Column(name = "error_message", length = 4000)
	private String errorMessage;

	@Column(name = "remarks", length = 4000)
	private String remarks;

	@Column(name = "CREATED_ON")
	private Date createdOn;

}
