package com.lic.epgs.quotation.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class QuotationMemberDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long memberId;
	private String memberShipId;
	private Integer quotationId;
	private String licId;
	private String memberStatus;
	private String maritalStatus;
	private String firstName;
	private String middleName;
	private String lastName;
	private String fatherName;
	private String spouseName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfBirth;

	private String gender;
	private String pan;
	private Long aadharNumber;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfJoining;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfJoiningScheme;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date dateOfRetirement;

	private String designation;
	private String role;
	private String membershipNumber;
	private String typeOfMembershipNo;
	private Long phone;
	private String emailId;
	private String languagePreference;
	private String communicationPreference;
	private Integer category;
	private BigDecimal employerContribution;
	private BigDecimal employeeContribution;
	private BigDecimal totalContribution;
	private String annuityOption;
	private BigDecimal totalInterestedAccured;
	private Integer frequency;
	
	private String modifiedBy;
	private String createdBy;
	private Date modifiedOn;
	private Date createdOn;
	
	private List<QuotationMemberAddressDto> quotationMemberAddresses = new ArrayList<>();
	private List<QuotationMemberBankDetailDto> quotationMemberBankDetails = new ArrayList<>();
	private List<QuotationMemberAppointeeDto> quotationMemberAppointees = new ArrayList<>();
	private List<QuotationMemberNomineeDto> quotationMemberNominees = new ArrayList<>();

	private BigDecimal voluntaryContribution;
	private String componentLabel;
	
	
}
