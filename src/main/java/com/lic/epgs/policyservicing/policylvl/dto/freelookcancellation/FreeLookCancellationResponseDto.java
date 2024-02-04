package com.lic.epgs.policyservicing.policylvl.dto.freelookcancellation;

/**
 * @author pradeepramesh
 *
 */
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceDocumentDto;
import com.lic.epgs.policyservicing.common.dto.PolicyServiceNotesDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FreeLookCancellationResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long freeLookId;
	
	private FreeLookCancellationDto freeLookCancellationDto;
	private List<FreeLookCancellationDto> freeLookCancellationDtos;
	
	private PolicyServiceDocumentDto freeLookCancellationDocumentDto;
	private List<PolicyServiceDocumentDto> freeLookCancellationDocumentDtos;
	
	private PolicyServiceNotesDto freeLookCancellationNotesDto;
	private List<PolicyServiceNotesDto> freeLookCancellationNotesDtos;
	
	private String transactionStatus;
	private String transactionMessage;
}
