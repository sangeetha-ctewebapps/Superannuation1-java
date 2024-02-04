package com.lic.epgs.topupda.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
*
* @author Dhanush
*
*/
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopupdaApiResponseDto {
	
	private Object responseData;
	private String transactionStatus;
	private String transactionMessage;
	private TopupdaDto responseDto;
	private List<TopupdaDto> responseDtos;
	private TopupdaNotesDto responseNoteDto;
	private List<TopupdaNotesDto> responseNoteDtos;
	
}
