package com.lic.epgs.policyservicing.common.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pradeepramesh
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PolicyServiceStatusResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private transient List<?> responseData;
	private String transactionStatus;
	private String transactionMessage;

}
