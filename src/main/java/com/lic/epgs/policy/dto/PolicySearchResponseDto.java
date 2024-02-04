package com.lic.epgs.policy.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicySearchResponseDto  implements Serializable {
   
	private static final long serialVersionUID = 1L;
	private String mphCode;
	private String mphName;
	private String proposalNumber;
	private Long policyId;
	private String policyNumber;
	private String policyStatus;
	private String product;
	private String unitOffice;
	private Long mphId;
	private String unitCode;
}
