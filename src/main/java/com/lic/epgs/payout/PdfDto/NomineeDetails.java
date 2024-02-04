package com.lic.epgs.payout.PdfDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NomineeDetails {

	private String nomineeName;
	private String typeOfAnnuity;
	private String dob;
	private String ifscCode;
	private Double commutationAmount;
	private Double tdsAmount;
}
