package com.lic.epgs.payout.dto;

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
public class PayoutMemberSearchResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long payoutId;

	private String memberId;

	private String memberShipNo;

	private String memberName;

	private String mphCode;

	private String mphName;

	private Integer category;

	private Integer product;

	private Integer age;

	private String masterPolicyNo;

	private Integer masterPolicyStatus;

	private String unitCode;
}
