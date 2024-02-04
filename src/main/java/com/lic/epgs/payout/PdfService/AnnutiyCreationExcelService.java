package com.lic.epgs.payout.PdfService;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author Nathiya
 *
 */

public interface AnnutiyCreationExcelService {

	ByteArrayInputStream excelDownload(String startDate, String endDate, String unitId) throws IOException;

//	byte[] downloadSample(String startDate, String endDate, String unitId);
	
}
