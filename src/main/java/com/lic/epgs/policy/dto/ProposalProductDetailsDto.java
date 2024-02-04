package com.lic.epgs.policy.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Logesh.D
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProposalProductDetailsDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer proposalProductId;
	
	private String proposalId;
	
	
	private String lineOfBusiness;
	
	
	private String createdBy;
	
	
	private String createdOn;
	
	
	private String modifiedBy;
	
	
	private String modifiedOn;
	
	private String product;
	
	private String productVariant;
	
	private String productSubVariant;
	
	private String numberOfLives;
	
	private String premiumExpected;
	
	private String uin;
	
	private String marketingLob;
	
	private String subCategory;
	
	private String productCode;
	
	private String productType;
	
	
	
	
	


	

	

}
