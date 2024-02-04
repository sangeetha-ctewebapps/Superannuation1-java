package com.lic.epgs.claim.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClaimMbrDto {

	private Long memberId;

	private String memberShipId;

	private Long policyMemberId;

	private String claimNo;

	private String licId;

	private String memberStatus;

	private String firstName;

	private String middleName;

	private String lastName;

	private String fatherName;

	private String spouseName;

	private String dateOfBirth;

	private String gender;

	private String pan;

	private Long aadharNumber;

	private String dateOfJoining;

	private String dateOfJoiningScheme;

	private String dateOfRetirement;

	private String designation;

	private String role;

	private String membershipNumber;

	private String typeOfMembershipNo;

	private Long phone;

	private String emailId;

	private String languagePreference;

	private String communicationPreference;

	private Integer category;

	private Double employerContribution;

	private Double employeeContribution;

	private Double totalContribution;

	private String annuityOption;

	private Double totalInterestedAccured;

	private Integer dbCalcValue;

	private Double dbPension;

	private Double dbPurchasePrice;

	private Integer frequency;

	private Double voluntaryContribution;

	private Double totalInterestAccured;

	private Double corpusPercentage;

	private List<ClaimMbrNomineeDto> claimMbrNomineeDtls;

	private List<ClaimMbrAppointeeDto> claimMbrAppointeeDtls;

	private List<ClaimCommutationCalcDto> claimCommutationCalc;

	private List<ClaimAnnuityCalcDto> claimAnuityCalc;

	private List<ClaimMbrAddressDto> claimMbrAddresses;

	private List<ClaimMbrBankDetailDto> claimMbrBankDetails;

	private List<ClaimFundValueDto> claimFundValue;

	private List<ClaimMbrFundValueDto> claimMbrFundValue;

	private List<ClaimPayeeBankDetailsDto> claimPayeeTempBank;

	private String createdBy;

	private String modifiedBy;

}
