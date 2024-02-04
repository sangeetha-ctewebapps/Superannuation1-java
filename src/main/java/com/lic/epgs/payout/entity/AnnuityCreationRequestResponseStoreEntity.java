package com.lic.epgs.payout.entity;

import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "FUND_API_REQUEST_RESPONSE_LOG")
public class AnnuityCreationRequestResponseStoreEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FUND_API_REQ_RES_GEN")
	@SequenceGenerator(name = "FUND_API_REQ_RES_GEN", sequenceName = "FUND_API_REQ_RES_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

	@Column(name = "reference_id")
	private String referenceId;

	@Column(name = "policy_number")
	private String policyNumber;

	@Column(name = "member_id")
	private String memberId;

	@Column(name = "variant")
	private String variant;

	@Column(name = "username")
	private String userName;

	@Column(name = "api_request")
	private Blob apiRequest;

	@Column(name = "api_request_string", length = 4000)
	private String apiRequestString;

	@Column(name = "api_response")
	private Blob apiResponse;

	@Column(name = "api_response_string", length = 4000)
	private String apiResponseString;

	@Column(name = "request_date")
	private Date requestDate;

	@Column(name = "response_date")
	private Date responseDate;

	@Column(name = "type")
	private String type;

	@Column(name = "api_url")
	private String apiUrl;

	@Column(name = "status")
	private String status;

	@Column(name = "remark")
	private String remark;

	@Column(name = "request_method")
	private String requestMethod;

	@Column(name = "error_response", length = 4000)
	private String errorResponse;

	@Column(name = "content_type")
	private String contentType;

	@Column(name = "business_Corelation_Id")
	private String businessCorelationId;

	@Column(name = "corelation_Id")
	private String corelationId;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
}
