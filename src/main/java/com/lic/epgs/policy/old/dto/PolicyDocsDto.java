package com.lic.epgs.policy.old.dto;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data

@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyDocsDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long docId;
	
	private Long policyId;

	private String requirement;

	private String status;

	private String comments;

	private String modifiedBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn = new Date();

	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn = new Date();
	private String serviceNo;
	private Long serviceId;
	private String documentIndex;
	private Integer folderIndex;
	private Long conversionId;
	private Boolean isActive;
}
