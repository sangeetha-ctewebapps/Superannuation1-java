package com.lic.epgs.payout.PdfService;

import java.io.IOException;

public interface FundStatementPdfService {
	
	String fundStatementPdfGeneration(String referenceNo, String reportType, String forwardTo,Long policyId,String financialYear)  throws IOException;

}
