package com.lic.epgs.policy.entity;
/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.util.Date;

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
@Table(name = "MEMBER_NOMINEE_TEMP")
public class MemberNomineeTempEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "P_MBR_N_T_SEQUENCE")
	@SequenceGenerator(name = "P_MBR_N_T_SEQUENCE", sequenceName = "P_MBR_N_T_SEQ", allocationSize = 1)
	@Column(name = "NOMINEE_ID")
	private Long nomineeId;

	@Column(name = "MEMBER_ID")
	private Long memberId;

	@Column(name = "NOMINEE_TYPE")
	private String nomineeType;

	@Column(name = "NOMINEE_NAME")
	private String nomineeName;

	@Column(name = "RELATION_SHIP")
	private String relationShip;

	@Column(name = "NOMINEE_AADHAR_NUMBER")
	private Long aadharNumber;

	@Column(name = "NOMINEE_PHONE")
	private Long phone;

	@Column(name = "NOMINEE_EMAIL_ID")
	private String emailId;

	@Column(name = "ADDRESS_ONE")
	private String addressOne;

	@Column(name = "ADDRESS_TWO")
	private String addressTwo;

	@Column(name = "ADDRESS_THREE")
	private String addressThree;

	@Column(name = "NOMINEE_COUNTRY")
	private String country;

	@Column(name = "NOMINEE_PINCODE")
	private Long pinCode;

	@Column(name = "NOMINEE_DISTRICT")
	private String district;

	@Column(name = "NOMINEE_STATE")
	private String state;
	
	@Column(name = "PERCENTAGE")
	private String percentage;
	
	@Column(name = "AGE")
	private Integer age;
	
	@Column(name = "DATE_OF_BIRTH")
	private Date dateOfBirth;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive = Boolean.TRUE;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_ON")
	private Date modifiedOn = new Date();	

}
