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

import com.lic.epgs.payout.PdfService.FundStatementPdfService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/fund/pdf")
public class FundStatementPdfController {
	
	@Autowired
	FundStatementPdfService fundStatementPdfService;
	
	
	@GetMapping("/fundStatementPdfGeneration")
	public void fundStatementPdfGeneration(@RequestParam String referenceNo, @RequestParam String reportType,
			@RequestParam(defaultValue = "") String forwardTo,HttpServletRequest request, HttpServletResponse response,
			@RequestParam Long policyId,@RequestParam String financialYear)
			throws IOException {
		String pdfFilePath = fundStatementPdfService.fundStatementPdfGeneration(referenceNo,reportType,forwardTo,policyId,financialYear);

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
		String headerValue = String.format("attachment; filename=\"%s\"", ".pdf");
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

}
