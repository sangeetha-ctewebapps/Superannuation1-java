package com.lic.epgs.payout.PdfServiceImpl;

//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Arrays;
//import java.util.List;
//
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVPrinter;
//import org.apache.commons.csv.QuoteMode;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
////import org.apache.poi.ss.usermodel.Cell;
////import org.apache.poi.ss.usermodel.ClientAnchor;
////import org.apache.poi.ss.usermodel.CreationHelper;
////import org.apache.poi.ss.usermodel.Drawing;
////import org.apache.poi.ss.usermodel.Row;
////import org.apache.poi.ss.usermodel.Sheet;
////import org.apache.poi.ss.usermodel.Workbook;
////import org.apache.poi.ss.util.CellRangeAddress;
////import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
////
////
//import com.lic.epgs.payout.PdfService.AnnutiyCreationExcelService;
//import com.lic.epgs.payout.repository.AnnuityCreationReqAndResRepository;
//
//
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Arrays;
//import java.util.List;
//
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVPrinter;
//import org.apache.commons.csv.QuoteMode;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
////import org.apache.poi.ss.usermodel.Cell;
////import org.apache.poi.ss.usermodel.ClientAnchor;
////import org.apache.poi.ss.usermodel.CreationHelper;
////import org.apache.poi.ss.usermodel.Drawing;
////import org.apache.poi.ss.usermodel.Row;
////import org.apache.poi.ss.usermodel.Sheet;
////import org.apache.poi.ss.usermodel.Workbook;
////import org.apache.poi.ss.util.CellRangeAddress;
////import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.lic.epgs.payout.PdfHelper.AnnuityCreationExcelDownloadHelper;
//import com.lic.epgs.payout.PdfService.AnnutiyCreationExcelService;
//import com.lic.epgs.payout.repository.AnnuityCreationReqAndResRepository;
//
//
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.apache.spark.sql.Dataset;
//import org.apache.spark.sql.Row;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.List;









import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.ClientAnchor;
//import org.apache.poi.ss.usermodel.CreationHelper;
//import org.apache.poi.ss.usermodel.Drawing;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lic.epgs.payout.PdfService.AnnutiyCreationExcelService;
import com.lic.epgs.payout.repository.AnnuityCreationReqAndResRepository;






/**
 * @author Nathiya
 *
 */

@Service
public class AnnutiyCreationExcelServiceImpl implements AnnutiyCreationExcelService{

	protected final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	AnnuityCreationReqAndResRepository annuityCreationReqAndResRepository;
	
	
	
//	@Override
//	public byte[] downloadSample(String startDate, String endDate, String unitId) {
//		
//		List<Object[]> acObjectList = annuityCreationReqAndResRepository.getAnnutiyCreation(startDate,endDate,unitId);
//		
//		Workbook workbook = new XSSFWorkbook();
//		Sheet sheet = workbook.createSheet("Sheet1");
//		AnnuityCreationExcelDownloadHelper annuityCreationExcelDownloadHelper = new AnnuityCreationExcelDownloadHelper();
//		annuityCreationExcelDownloadHelper.createHeader(workbook, sheet, true);
//		annuityCreationExcelDownloadHelper.createHeaderforRowAnnuity(workbook, sheet, acObjectList);
//		
//		
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		
//		try {
//			workbook.write(out);
//			out.close();
//			return out.toByteArray();
//		} catch (IOException e) {
//			return null;
//		}
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public ByteArrayInputStream excelDownload(String startDate,String endDate,String unitId) throws IOException {

		
		

		
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("sheet1");
		
		Row row = null;
		Cell cell = null;
		
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("UNIT CODE");
		
		cell = row.createCell(1);
		cell.setCellValue("UNIT");
		
//		cell = row.createCell(2);
//		cell.setCellValue("UNIT CODE");
		
		cell = row.createCell(2);
		cell.setCellValue("ANNUITY ORIGIN");
		
		cell = row.createCell(3);
		cell.setCellValue("CREATED DATE");
		
		cell = row.createCell(4);
		cell.setCellValue("AN NO");
		
		cell = row.createCell(5);
		cell.setCellValue("NAME OF THE ANNUITANT");
		
		cell = row.createCell(6);
		cell.setCellValue("DATE OF BIRTH");
		
		cell = row.createCell(7);
		cell.setCellValue("POLICY NO");
		
		cell = row.createCell(8);
		cell.setCellValue("DATE OF VESTING");
		
		cell = row.createCell(9);
		cell.setCellValue("AN DUE DATE");
		
		cell = row.createCell(10);
		cell.setCellValue("PURCHASE PRICE");
		
		cell = row.createCell(11);
		cell.setCellValue("BASIC_PENSION");
		
		cell = row.createCell(12);
		cell.setCellValue("ANNUITY OPTION DESC");
		
		cell = row.createCell(13);
		cell.setCellValue("PAYMENTMODE");
		
		cell = row.createCell(14);
		cell.setCellValue("STATUS");
		
		
		
		
		int rowCount =1;
		
		List<Object[]> acObjectList = annuityCreationReqAndResRepository.getAnnutiyCreation(startDate,endDate,unitId);
		
		if(acObjectList !=null && !acObjectList.isEmpty()) {
			
			for(Object[] obj : acObjectList) {
				
				row = sheet.createRow(rowCount++);
				cell = row.createCell(0);
				cell.setCellValue((obj[0] !=null)?obj[0].toString():null);
				
				cell = row.createCell(1);
				cell.setCellValue((obj[1] !=null)?obj[1].toString():null);
				
				cell = row.createCell(2);
				cell.setCellValue((obj[2] !=null)?obj[2].toString():null);
				
				cell = row.createCell(3);
				cell.setCellValue((obj[3] !=null)?obj[3].toString():null);
				
				cell = row.createCell(4);
				cell.setCellValue((obj[4] !=null)?obj[4].toString():null);
				
				cell = row.createCell(5);
				cell.setCellValue((obj[5] !=null)?obj[5].toString():null);
				
				cell = row.createCell(6);
				cell.setCellValue((obj[6] !=null)?obj[6].toString():null);
				
				cell = row.createCell(7);
				cell.setCellValue((obj[7] !=null)?obj[7].toString():null);
				
				cell = row.createCell(8);
				cell.setCellValue((obj[8] !=null)?obj[8].toString():null);
				
				cell = row.createCell(9);
				cell.setCellValue((obj[9] !=null)?obj[9].toString():null);
				
				cell = row.createCell(10);
				cell.setCellValue((obj[10] !=null)?obj[10].toString():null);
				
				cell = row.createCell(11);
				cell.setCellValue((obj[11] !=null)?obj[11].toString():null);
				
				cell = row.createCell(12);
				cell.setCellValue((obj[12] !=null)?obj[12].toString():null);
				
				cell = row.createCell(13);
				cell.setCellValue((obj[13] !=null)?obj[13].toString():null);
				
				cell = row.createCell(14);
				cell.setCellValue((obj[14] !=null)?obj[14].toString():null);
				
				
				
			}
			
			
		}
		
		
		
		
		
		
		
//		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 0));
		
//		CreationHelper helper = wb.getCreationHelper();
//		Drawing drawing = sheet.createDrawingPatriarch();
//		ClientAnchor anchor = helper.createClientAnchor();
//		anchor.setCol1(0);
//		anchor.setRow1(0);
//		anchor.setCol2(1);
//		anchor.setRow2(1);
//		
//		Row headerRow = sheet.createRow(0);
		
//		List<Object[]> acObjectList = annuityCreationReqAndResRepository.getAnnutiyCreation(startDate,endDate,unitId);
//		
//		String[] headerCells = { "UNIT_CODE", "UNIT", "ANNUITY_ORIGIN", "CREATED_DATE",
//				"AN_NO", "NAME_OF_THE_ANNUITANT", "DATE_OF_BIRTH", "POLICY_NO", "DATE_OF_VESTING",
//				"AN_DUE_DATE", "PURCHASE_PRICE", "BASIC_PENSION", "ANNUITY_OPTION_DESC",
//				"PAYMENTMODE", "STATUS" };
//		
//		
//		for (int i = 0; i < headerCells.length; i++) {
//			Cell cell = headerRow.createCell(i);
//			cell.setCellValue(headerCells[i]);
//		}
//		int rowNum = 1;
//		for (Object[] obj : acObjectList) {
//			Row row2 = sheet.createRow(rowNum++);
//			for (int i = 0; i <=14; i++) {
//				Cell cell = row2.createCell(i);
//				if (obj[i] != null) {
//					cell.setCellValue(obj[i].toString());
//				}
//			}
//		}
		
		
		 ByteArrayOutputStream stream = new ByteArrayOutputStream();
		 wb.write(stream);
	     return new ByteArrayInputStream(stream.toByteArray());
		
		
		
		
		
		
		
	
		
		
		
		
		
		
		
//		logger.info("AdjustmentContributionServiceImpl -- policyDepositAdjustment --started");
//
//		List<String> data = null;
//		
//		final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
//		
//		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
//		CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);){
//			
////			if (loadTypeId != null) {
//				
//				List<Object[]> acObjectList = annuityCreationReqAndResRepository.getAnnutiyCreation(startDate,endDate,unitId);
//				
//			
//				List<String> header = Arrays.asList("UNIT_CODE", "UNIT", "ANNUITY_ORIGIN", "CREATED_DATE",
//						"AN_NO", "NAME_OF_THE_ANNUITANT", "DATE_OF_BIRTH", "POLICY_NO", "DATE_OF_VESTING",
//						"AN_DUE_DATE", "PURCHASE_PRICE", "BASIC_PENSION", "ANNUITY_OPTION_DESC",
//						"PAYMENTMODE", "STATUS");
//				
//				csvPrinter.printRecord(header);
//			
//				for (Object[] obj : acObjectList) {
//					
//					data = Arrays.asList(
//
//							        (obj[0] != null) ? obj[0].toString() : null,
//							        (obj[1] != null) ? obj[1].toString() : null, (obj[2] != null) ? obj[2].toString() : null,
//									(obj[3] != null) ? obj[3].toString() : null, (obj[4] != null) ? DecimalFormat.getIntegerInstance().format(obj[4].toString()) : null,
//									(obj[5] != null) ? obj[5].toString() : null, (obj[6] != null) ? obj[6].toString() : null,
//									(obj[7] != null) ? obj[7].toString() : null, (obj[8] != null) ? obj[8].toString() : null,
//									(obj[9] L̥!= null) ? obj[9].toString() : null, (obj[10] != null) ? obj[10].toString() : null,
//									(obj[11] != null) ? obj[11].toString() : null,(obj[12] != null) ? obj[12].toString() : null,
//									(obj[13] != null) ? obj[13].toString() : null,(obj[14] != null) ? obj[14].toString() : null);
//		
//					csvPrinter.printRecord(data);
//					
//					
//				}
////			}
//			csvPrinter.flush();
//			logger.info("AdjustmentContributionServiceImpl -- policyDepositAdjustment --ended");
//			return new ByteArrayInputStream(out.toByteArray());
//		} catch (Exception e) {
//			throw new IOException(e);
//		}
	
		
		
		
		
		
		
		
		
		
		
		
		
//		
////		Workbook wb = new XSSFWorkbook();
////		Sheet sheet = wb.createSheet();
////		
////		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 0));
////		
////		CreationHelper helper = wb.getCreationHelper();
////		Drawing drawing = sheet.createDrawingPatriarch();
////		ClientAnchor anchor = helper.createClientAnchor();
////		anchor.setCol1(0);
////		anchor.setRow1(0);
////		anchor.setCol2(1);
////		anchor.setRow2(1);
////		
////		Row headerRow = sheet.createRow(0);
////		
////		List<Object[]> acObjectList = annuityCreationReqAndResRepository.getAnnutiyCreation(startDate,endDate,unitId);
////		
////		String[] headerCells = { "UNIT_CODE", "UNIT", "ANNUITY_ORIGIN", "CREATED_DATE",
////				"AN_NO", "NAME_OF_THE_ANNUITANT", "DATE_OF_BIRTH", "POLICY_NO", "DATE_OF_VESTING",
////				"AN_DUE_DATE", "PURCHASE_PRICE", "BASIC_PENSION", "ANNUITY_OPTION_DESC",
////				"PAYMENTMODE", "STATUS" };
////		
////		
////		for (int i = 0; i < headerCells.length; i++) {
////			Cell cell = headerRow.createCell(i);
////			cell.setCellValue(headerCells[i]);
////		}
////		int rowNum = 1;
////		for (Object[] obj : acObjectList) {
////			Row row2 = sheet.createRow(rowNum++);
////			for (int i = 0; i <=14; i++) {
////				Cell cell = row2.createCell(i);
////				if (obj[i] != null) {
////					cell.setCellValue(obj[i].toString());
////				}
////			}
////		}
////		
////		
////		 ByteArrayOutputStream stream = new ByteArrayOutputStream();
////		 wb.write(stream);
////	     return new ByteArrayInputStream(stream.toByteArray());
////		
////		
////		
////		
////		
//		
////		logger.info("AdjustmentContributionServiceImpl -- policyDepositAdjustment --started");
////
////		List<String> data = null;
////		
////		final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
////		
////		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
////		CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);){
////			
//////			if (loadTypeId != null) {
////				
////				List<Object[]> acObjectList = annuityCreationReqAndResRepository.getAnnutiyCreation(startDate,endDate,unitId
////						);
////				
////			
////				List<String> header = Arrays.asList("UNIT_CODE", "UNIT", "ANNUITY_ORIGIN", "CREATED_DATE",
////						"AN_NO", "NAME_OF_THE_ANNUITANT", "DATE_OF_BIRTH", "POLICY_NO", "DATE_OF_VESTING",
////						"AN_DUE_DATE", "PURCHASE_PRICE", "BASIC_PENSION", "ANNUITY_OPTION_DESC",
////						"PAYMENTMODE", "STATUS");
////				
////				csvPrinter.printRecord(header);
////			
////				for (Object[] obj : acObjectList) {
////					
////					data = Arrays.asList(
////
////							        (obj[0] != null) ? obj[0].toString() : null,
////							        (obj[1] != null) ? obj[1].toString() : null, (obj[2] != null) ? obj[2].toString() : null,
////									(obj[3] != null) ? obj[3].toString() : null, (obj[4] != null) ? obj[4].toString() : null,
////									(obj[5] != null) ? obj[5].toString() : null, (obj[6] != null) ? obj[6].toString() : null,
////									(obj[7] != null) ? obj[7].toString() : null, (obj[8] != null) ? obj[8].toString() : null,
////									(obj[9] != null) ? obj[9].toString() : null, (obj[10] != null) ? obj[10].toString() : null,
////									(obj[11] != null) ? obj[11].toString() : null,(obj[12] != null) ? obj[12].toString() : null,
////									(obj[13] != null) ? obj[13].toString() : null,(obj[14] != null) ? obj[14].toString() : null);
////		
////					csvPrinter.printRecord(data);
////					
////					
////				}
//////			}
////			csvPrinter.flush();
////			logger.info("AdjustmentContributionServiceImpl -- policyDepositAdjustment --ended");
////			return new ByteArrayInputStream(out.toByteArray());
////		} catch (Exception e) {
////			throw new IOException(e);
////		}
////	}
////	
//	
//	
//	
}
}
