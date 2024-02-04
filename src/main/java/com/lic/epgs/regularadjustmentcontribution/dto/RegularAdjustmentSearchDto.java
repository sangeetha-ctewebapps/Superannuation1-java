package com.lic.epgs.regularadjustmentcontribution.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;

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
public class RegularAdjustmentSearchDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String policyNumber;
	
	private String mphCode;
	private String mphName;
	private String from;
	private String to;
	private String role;
	private String product;
	private String lineOfBusiness;
	private String policyStatus;
	private String variant;
	private String unitCode;
	private String proposalNumber;
	private String pageName;
	private String userName;

}
