package com.lic.epgs.payout.restapi.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
public class PayoutJointDetailRestApiRequest {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date jointLifeDOB;
	private String jointLifeFirstName;
	private String jointLifeGender;
	private String jointLifeLastName;
	private String jointLifeMiddleName;
	private String jointLifePercent	;
}
