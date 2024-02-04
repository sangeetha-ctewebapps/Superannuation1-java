package com.lic.epgs.payout.PdfController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lic.epgs.claim.service.ClaimService;
import com.lic.epgs.payout.PdfService.PayoutPdfService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/payout/pdf/")

public class PayoutPdfController {

	@Autowired
	private PayoutPdfService payoutPdfService;
	

//	@Autowired
//	private PayoutRepository payoutRepository;

	@GetMapping("downloads/report")
	public void generateReport(@RequestParam String referenceNo, @RequestParam String reportType,
			@RequestParam(defaultValue = "") String forwardTo, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String pdfFilePath = payoutPdfService.generateReport(referenceNo, reportType, forwardTo);

		ServletContext context = request.getServletContext();

		// construct the complete absolute path of the file
		File downloadFile = new File(pdfFilePath);
		FileInputStream inputStream = new FileInputStream(downloadFile);

		// get MIME type of the file
		String mimeType = context.getMimeType(pdfFilePath);
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", reportType + referenceNo + ".pdf");
		response.setHeader(headerKey, headerValue);

		// get output stream of the response
		OutputStream outStream = response.getOutputStream();

		byte[] buffer = new byte[4096];
		int bytesRead = -1;

		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		inputStream.close();
		outStream.close();

		if (new File(pdfFilePath).exists()) {
			new File(pdfFilePath).delete();
		}
	}

	@GetMapping(value = { "Adjustmentvoucher/premiumreceipt" })
	public void quotationValuationReport(@RequestParam("masterpolicyId") Long masterpolicyId,@RequestParam("reportType") String reportType,
			HttpServletRequest request, HttpServletResponse response) throws IOException {

		String premiumreceipt = payoutPdfService.premiumreceipt(masterpolicyId,reportType);

		ServletContext context = request.getServletContext();
		// construct the complete absolute path of the file
		File downloadFile = new File(premiumreceipt);
		FileInputStream inputStream = new FileInputStream(downloadFile);
		// get MIME type of the file
		String mimeType = context.getMimeType(premiumreceipt);
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", masterpolicyId + ".pdf");
		response.setHeader(headerKey, headerValue);
		// get output stream of the response
		OutputStream outStream = response.getOutputStream();
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
		inputStream.close();
		outStream.close();
		if (new File(premiumreceipt).exists()) {
			// new File(pdfDocumentValuationReport).delete();
		}
	}
	
	
	@GetMapping(value = { "/generateGlCodesPdf" })
	public void getcandbsheetpdf(
			@RequestParam(value="claimOnBoardNo") String claimOnBoardNo, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String getGlCodesheetpdf = payoutPdfService.getGlCodesSheetpdf(claimOnBoardNo);

		ServletContext context = request.getServletContext();
		// construct the complete absolute path of the file
		File downloadFile = new File(getGlCodesheetpdf);
		FileInputStream inputStream = new FileInputStream(downloadFile);
		// get MIME type of the file
		String mimeType = context.getMimeType(getGlCodesheetpdf);
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", claimOnBoardNo + ".pdf");
		response.setHeader(headerKey, headerValue);
		// get output stream of the response
		OutputStream outStream = response.getOutputStream();
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
		inputStream.close();
		outStream.close();
		if (new File(getGlCodesheetpdf).exists()) {
			// new File(pdfDocumentValuationReport).delete();
		}
	}
	
}
