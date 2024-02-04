package com.lic.epgs.payout.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "PAYOUT_NOTES")
public class PayoutNotesEntity {

	@Id
	@Column(name = "PAYOUT_NOTE_ID", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYOUT_NOTE_SEQUENCE")
	@SequenceGenerator(name = "PAYOUT_NOTE_SEQUENCE", sequenceName = "PAYOUT_NOTE_SEQUENCE", allocationSize = 1)
	private Long id;

	@Column(name = "PAYOUT_NO", length = 20)
	private String payoutNo;

	@Column(name = "NOTE_CONTENTS", length = 1000)
	private String noteContents;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "CREATED_BY", length = 100)
	private String createdBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PAYOUT_ID", nullable = false)
	private PayoutEntity payout;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
}
