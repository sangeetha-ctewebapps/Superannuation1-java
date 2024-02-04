package com.lic.epgs.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FUNDED_COMMON_MASTER")
public class FundedCommonMasterEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FUNDED_COMMON_MASTER_SEQUENCE")
	@SequenceGenerator(name = "FUNDED_COMMON_MASTER_SEQUENCE", sequenceName = "FUNDED_COMMON_MASTER_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "VALUE")
	private String value;
	
	@Column(name = "CODE_ID")
	private Long codeId;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "STREAM")
	private String stream;
	
	@Column(name = "DISPLAY_VALUE")
	private String displayValue;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "MODE_OF_EXIT")
	private String modeOfExit;
	
	

}
