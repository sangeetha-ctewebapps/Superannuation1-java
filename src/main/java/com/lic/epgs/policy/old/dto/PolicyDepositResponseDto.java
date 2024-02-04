package com.lic.epgs.policy.old.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.util.List;

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
public class PolicyDepositResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<PolicyDepositOldDto> deposits;
	private Long proposalNo;
	private String transactionStatus;
	private String transactionMessage;
}
