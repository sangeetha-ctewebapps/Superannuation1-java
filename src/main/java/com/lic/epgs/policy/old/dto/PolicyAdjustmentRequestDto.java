package com.lic.epgs.policy.old.dto;
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
public class PolicyAdjustmentRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long policyId;
	private Long depositId;
	private String zeroActId;
	private String role;
	private String variantType;
}
