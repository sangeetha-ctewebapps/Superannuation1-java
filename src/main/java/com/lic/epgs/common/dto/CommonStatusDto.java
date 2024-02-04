/**
 * 
 */
package com.lic.epgs.common.dto;

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
public class CommonStatusDto {
	private String id;
	private String code;
	private String type;
	private String description;
	private String description1;
	private String name;
	private String isActive;
}
