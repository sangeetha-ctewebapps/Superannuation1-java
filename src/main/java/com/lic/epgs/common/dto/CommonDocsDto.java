package com.lic.epgs.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CommonDocsDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer docId;
	private String requirement;
	private String status;
	private String comments;
	private String documentIndex;
	private Integer folderIndex;
	private Integer quotationId;
	private Long mergeId;
	private Long surrenderId;
	private Integer documentNumber;
	private Boolean isActive;
	private Long conversionId;
	private String createdBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date createdOn = new Date();
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "asia/kolkata")
	private Date modifiedOn = new Date();
	
}
