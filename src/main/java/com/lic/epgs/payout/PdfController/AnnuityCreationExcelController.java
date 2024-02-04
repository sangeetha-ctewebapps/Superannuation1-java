package com.lic.epgs.payout.PdfController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.payout.PdfService.AnnutiyCreationExcelService;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

/**
 * @author Nathiya
 *
 */

@RestController  
@CrossOrigin("*")
@RequestMapping("/api/annutiyCreation")
		
public class AnnuityCreationExcelController {

	protected final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	AnnutiyCreationExcelService annutiyCreationExcelService;
	
	
	
	
	
	
	
	@GetMapping("/excelDownload")
	public ResponseEntity<InputStreamResource> newAnnutiyCreated(
			@RequestParam  String startDate,
			@RequestParam  String endDate,
			@RequestParam  String unitId) throws IOException {
		logger.info("AnnuityCreationExcelController -- excelDownload --started");
		
		ByteArrayInputStream file = annutiyCreationExcelService.excelDownload(startDate,endDate,unitId);
				
		 HttpHeaders headers = new HttpHeaders();
//		 headers.add("Content-Disposition", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");	
		 headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8"));
		 headers.set("Content-Disposition", "attachment; filename=AnnuityCreation.xlsx");
		logger.info("AnnuityCreationExcelController -- excelDownload --ended");
		
		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(file));
	}
	
	
	
	
	
	
//	@GetMapping("excelDownload")
//	public ResponseEntity<byte[]> downloadSample(@RequestParam  String startDate,
//			@RequestParam  String endDate,
//			@RequestParam  String unitId) {
//		byte[] tt = annutiyCreationExcelService.downloadSample(startDate,endDate,unitId);
//
//		HttpHeaders header = new HttpHeaders();
//		header.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8"));
//		header.setContentLength(tt.length);
//		header.set("Content-Disposition", "attachment; filename=sample.xlsx");
//
//		return new ResponseEntity<>(tt, header, HttpStatus.OK);
//	}
//	
	
	
	
//	@GetMapping("/excelDownload")
//	public ResponseEntity<InputStreamResource> newAnnutiyCreated(
//			@RequestParam  String startDate,
//			@RequestParam  String endDate,
//			@RequestParam  String unitId) throws IOException {
//		logger.info("AnnuityCreationExcelController -- excelDownload --started");
//		
//		ByteArrayInputStream file = annutiyCreationExcelService.excelDownload(startDate,endDate,unitId);
//				
//		 HttpHeaders headers = new HttpHeaders();
//		 headers.add("Content-Disposition", "attachment; filename=Annutiy_Creation.csv");	
//		
//		logger.info("AnnuityCreationExcelController -- excelDownload --ended");
//		
//		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(file));
//	}
	
	
	
	
}
