package com.lic.epgs.policyservicing.policylvl.dto.conversion;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D Date:20-09-2022
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyConversionSearchDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String mphCode;
	private String mphName;
	private String product;
	private String lineOfBusiness;
	private String status;
	private Integer pageCount;
	private Integer limit;
	private String login;
	private String unitCode;
	private String prevPolicyNo;
	private String newPolicyNo;
	private Long serviceId;
	

}
