package com.lic.epgs.payout.PdfHelper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AnnuityCreationExcelDownloadHelper {

	protected final Logger logger = LogManager.getLogger(getClass());
	
	public void createHeader(Workbook workbook, Sheet sheet, boolean isSample) {
		
		
		Row header = sheet.createRow(0);
		CellStyle headerStyle = workbook.createCellStyle();
		
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFPalette palette = hwb.getCustomPalette();
		
		try {
			hwb.close();
		} catch (IOException e) {
			//do nothing
		}
		
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		HSSFColor fontColor = palette.findSimilarColor(0,0,0);
		font.setColor(fontColor.getIndex());
		font.setBold(false);
		headerStyle.setFont(font);
		
		Cell headerCell = header.createCell(0);
		int i=0;
		
		for (String col : headerTexts()) {
			if (isSample && col.equals("FAILED REASON")) continue;
			headerCell = header.createCell(i++);
			headerCell.setCellValue(col);
			headerCell.setCellStyle(headerStyle);
		}
	
	}
	
	
	public List<String> headerTexts(){
		return Arrays.asList("UNIT_CODE", "UNIT", "ANNUITY_ORIGIN", "CREATED_DATE",
				"AN_NO", "NAME_OF_THE_ANNUITANT", "DATE_OF_BIRTH", "POLICY_NO", "DATE_OF_VESTING",
				"AN_DUE_DATE", "PURCHASE_PRICE", "BASIC_PENSION", "ANNUITY_OPTION_DESC",
				"PAYMENTMODE", "STATUS");
	}


	public void createHeaderforRowAnnuity(Workbook workbook, Sheet sheet, List<Object[]> acObjectList) {
		
		int rowNumber = 1;
		int colNumber = 0;
		Row header = null;
		Cell headerCell = null;
		
		if (acObjectList.size() == 0) {
			colNumber = 0;
			header = sheet.createRow(rowNumber++);
			
			headerCell = header.createCell(colNumber++);
			headerCell.setCellValue("There is no errors associated with this upload");
		}
		
		
		for (Object[] obj : acObjectList) {
			colNumber = 0;
			header = sheet.createRow(rowNumber++);
			
			for (int i = 0; i < obj.length; i++) {
//				if (i==0) continue;
				if (obj[i] == null) {
					colNumber++;
				} else {
					logger.info("AdjustmentContributionServiceImpl :: newcitrieaSearch :: Start---"+obj[i].toString());
					headerCell = header.createCell(colNumber++);headerCell.setCellValue(obj[i].toString());
				}
			}
			logger.info("AdjustmentContributionServiceImpl :: newcitrieaSearch");			
			
//			MemberBulkErrorEntity memberBulkErrorEntity = errornousInfo.stream()
//					.filter(error -> obj[0].equals(error.getStagingId()))
//					.findAny().orElse(null);
			headerCell = header.createCell(colNumber++);
//			headerCell.setCellValue(memberBulkErrorEntity.getError());
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	List<String> header = Arrays.asList("UNIT_CODE", "UNIT", "ANNUITY_ORIGIN", "CREATED_DATE",
//			"AN_NO", "NAME_OF_THE_ANNUITANT", "DATE_OF_BIRTH", "POLICY_NO", "DATE_OF_VESTING",
//			"AN_DUE_DATE", "PURCHASE_PRICE", "BASIC_PENSION", "ANNUITY_OPTION_DESC",
//			"PAYMENTMODE", "STATUS");
	

}
