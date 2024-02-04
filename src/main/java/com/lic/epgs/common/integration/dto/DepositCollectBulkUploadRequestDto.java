package com.lic.epgs.common.integration.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class DepositCollectBulkUploadRequestDto {
	
	private static final long serialVersionUID = 1L;
	
	private CollectionDetailLegacyModel collectionDetailLegacyModel;
	private CollectionLegacyModel collectionLegacyModel;
	private CollectionNeftDetailLegacyModel collectionNeftDetailLegacyModel;
	private GlTransactionLegacyModel glTransactionLegacyModel;
	
	private String userCode;

}
