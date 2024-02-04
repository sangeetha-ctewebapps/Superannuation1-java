package com.lic.epgs.policy.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyValuationDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long valuationId;
	private Long policyId;
	
	private String valuationType;
	private Integer attritionRate;
	private Integer salaryEscalation;
	private Integer deathRate;
	private Integer disRateIntrest;
	private Integer disRateSalaryEsc;
	
	private Long annuityOptId;
	private Long annuityOptionCode;
	
	private String annuityOption;
	
	private Integer accuralRateFactor;
	private Integer minPension;
	private Integer maxPension;
	private Integer withdrawalRate;
	
	private Boolean isActive;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn;
	private String modifiedBy;
}
