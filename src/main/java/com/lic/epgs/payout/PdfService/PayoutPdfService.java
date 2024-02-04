package com.lic.epgs.payout.PdfService;

import java.io.IOException;

public interface PayoutPdfService {


	String generateReport(String referenceNo, String reportType,String forwardTo) throws IOException;

	String premiumreceipt(Long masterpolicyId,String reportType) throws IOException;

	String getGlCodesSheetpdf(String claimOnBoardNo)throws IOException;
}
