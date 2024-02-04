/**
 * 
 */
package com.lic.epgs.policyservicing.policylvl.dto.memberaddition;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Karthick M
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyAomDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Object policy;
	private Object policyMbr;
	private Object notes;
	private Object finalcialData;
	private Object docs;

}
