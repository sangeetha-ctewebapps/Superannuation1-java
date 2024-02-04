package com.lic.epgs.adjustmentcontribution.entity;
import java.io.Serializable;
/**
 * @author pradeepramesh
 *
 */
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
@Table(name = "AC_NOTES_TEMP")
public class AdjustmentContributionNotesTempEntity  implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AC_N_ID_T_SEQUENCE")
	@SequenceGenerator(name = "AC_N_ID_T_SEQUENCE", sequenceName = "AC_N_ID_T_SEQ", allocationSize = 1)
	@Column(name = "NOTES_ID", nullable = false, updatable = false)
	private Long notesId;
	
	@Column(name = "ADJ_CON_ID")
	private Long adjustmentContributionId;

	@Column(name = "NOTE_CONTENTS", length = 2000)
	private String noteContents;

	@Column(name = "CREATED_ON")
	private Date createdOn = new Date();

	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "POLICY_ID")
	private Long policyId;

}
