package com.lic.epgs.payout.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PayoutMbrDto {

	private Long memberId;

	private String memberShipId;

	private Long policyMemberId;

	private String payoutNo;

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

	private List<PayoutMbrNomineeDto> payoutMbrNomineeDtls;

	private List<PayoutMbrAppointeeDto> payoutMbrAppointeeDtls;

	private List<PayoutCommutationCalcDto> payoutCommutationCalc;

	private List<PayoutAnnuityCalcDto> payoutAnuityCalc;

	private List<PayoutMbrAddressDto> payoutMbrAddresses;

	private List<PayoutMbrBankDetailDto> payoutMbrBankDetails;

	private List<PayoutFundValueDto> payoutFundValue;

	private List<PayoutMbrFundValueDto> payoutMbrFundValue;

	private List<PayoutPayeeBankDetailsDto> payoutPayeeBank;
	
	private String createdBy;
	
	private String modifiedBy;

}
