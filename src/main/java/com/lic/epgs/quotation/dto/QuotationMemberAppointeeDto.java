package com.lic.epgs.quotation.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationMemberAppointeeDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long appointeeId;
	private Long memberId;
	private String appointeeName;
	private String appointeeCode;
	private String relationship;
	private Long contactNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfBirth;
	private String pan;
	private Long aadharNumber;
	private String ifscCode;
	private String bankName;
	private String accountType;
	private Long accountNumber;
	
	private String modifiedBy;
	private String createdBy;
	private Date modifiedOn;
	private Date createdOn;

}
