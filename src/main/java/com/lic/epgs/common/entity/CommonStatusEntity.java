package com.lic.epgs.common.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.*;

/**
 * @author pradeepramesh Date:19.07.2022
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="QUOTATION_STATUS")
public class CommonStatusEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quotation_Status_Seq")
	@SequenceGenerator(name = "quotation_Status_Seq", sequenceName = "QUOTATION_STATUS_ID_SEQ", allocationSize = 1)	
	@Column(name = "ID")
	private String id;
	@Column(name = "CODE")
	private String code;
	@Column(name = "TYPE")
	private String type;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "DESCRIPTION1")
	private String description1;
	@Column(name = "NAME")
	private String name;
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	
	
}
