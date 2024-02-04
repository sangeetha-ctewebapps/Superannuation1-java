//package com.lic.epgs.payout.PdfHelper;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Element;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.FontFactory;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.PageSize;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.Phrase;
//import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.lic.epgs.payout.PdfDto.AdjustmentVoucherDetailDto;
//import com.lic.epgs.payout.PdfDto.ClaimForwardingLetterDto;
//import com.lic.epgs.payout.PdfDto.DcFundDocumentDto;
//import com.lic.epgs.payout.PdfDto.LifeCoverDetailsDto;
//import com.lic.epgs.payout.PdfDto.LifeInsuranceDetailDto;
//import com.lic.epgs.payout.PdfDto.SpPaymentPdfDto;
//import com.lic.epgs.payout.PdfDto.SpPdfDto;
//import com.lic.epgs.payout.PdfDto.SupplementaryAdjustmentDto;
//
//public class PayoutPdfHelper {
//
//	private static String urlLocal = "http://localhost:4202";
//
//	@Autowired
//	static Environment environment;
//
//	public static byte[] DcFundDocument(DcFundDocumentDto dcFundDocumentDto) throws DocumentException {
//		LocalDate dateObj = LocalDate.now();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//		String date = dateObj.format(formatter);
//		SimpleDateFormat formattedata = new SimpleDateFormat("dd-MM-yyyy");
//
////		Date annualRenewlDate = masterPolicyEnty.getAnnualRenewlDate();
////		String format = formattedata.format(annualRenewlDate);
//
//		Document document = new Document();
//
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//		PdfWriter.getInstance(document, baos);
//
//		document.open();
//
//		Font fontTittle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
//		fontTittle.setSize(9);
//
//		Paragraph dcFund = new Paragraph("Life Insurance Corporation of India", fontTittle);
//		dcFund.setAlignment(dcFund.ALIGN_CENTER);
//
//		Paragraph dcFund1 = new Paragraph("Pension and Group Schemes Dept", fontTittle);
//		dcFund1.setAlignment(dcFund1.ALIGN_CENTER);
//
//		Paragraph dcFund2 = new Paragraph(dcFundDocumentDto.getUnitAddress(), fontTittle);
//		dcFund2.setAlignment(dcFund2.ALIGN_CENTER);
//
//		Paragraph dcFund3 = new Paragraph(dcFundDocumentDto.getTrusteName(), fontTittle);
//		dcFund3.setAlignment(dcFund3.ALIGN_LEFT);
//
//		Paragraph dcFund4 = new Paragraph(dcFundDocumentDto.getTrusteAddressLine1(), fontTittle);
//		dcFund4.setAlignment(dcFund4.ALIGN_LEFT);
//
//		Paragraph dcFund5 = new Paragraph(dcFundDocumentDto.getTrusteStreet(), fontTittle);
//		dcFund5.setAlignment(dcFund5.ALIGN_LEFT);
//
//		Paragraph dcFund6 = new Paragraph(dcFundDocumentDto.getTrusteState(), fontTittle);
//		dcFund6.setAlignment(dcFund6.ALIGN_LEFT);
//
//		Paragraph dcFund7 = new Paragraph(dcFundDocumentDto.getTrustePincode(), fontTittle);
//		dcFund7.setAlignment(dcFund7.ALIGN_LEFT);
//
//		Font fontTittle1 = FontFactory.getFont(FontFactory.TIMES_BOLD);
//		fontTittle1.setSize(9);
//		fontTittle1.setStyle(Font.UNDERLINE);
//
//		Paragraph dcFundempty = new Paragraph("                                                    ", fontTittle);
//		dcFundempty.setAlignment(dcFundempty.ALIGN_LEFT);
//		dcFundempty.setIndentationLeft(36);
//
//		Paragraph dcFund8 = new Paragraph("Re: Master Policy No. GSCA /NGSCA /" + dcFundDocumentDto.getMasterPolicyNo()
//				+ " - Certificate of Balance in Fund As On <31/03/2022>\n", fontTittle1);
//		dcFund8.setAlignment(dcFund8.ALIGN_LEFT);
//
//		Paragraph dcFund9 = new Paragraph(
//				"We have pleasure in informing you that the interest Rates for the year have been declared and for the year"
//						+ dcFundDocumentDto.getYear() + " the interest rate applicable for your fund is "
//						+ dcFundDocumentDto.getInterest() + ".\n\n",
//				fontTittle);
//		dcFund9.setAlignment(dcFund9.ALIGN_LEFT);
//
//		Paragraph dcFund10 = new Paragraph(
//				"The Balance in your Running Account as on " + dcFundDocumentDto.getBalanceRunningAccountDate()
//						+ " is as under : " + dcFundDocumentDto.getBalanceRunningAccountAmount(),
//				fontTittle);
//		dcFund10.setAlignment(dcFund10.ALIGN_LEFT);
//
//		Paragraph dcFund11 = new Paragraph("Opening Balance As On " + dcFundDocumentDto.getOpeningBalanceDate() + ": "
//				+ dcFundDocumentDto.getOpeningBalanceAmount(), fontTittle);
//		dcFund11.setAlignment(dcFund11.ALIGN_LEFT);
//
//		Paragraph dcFund12 = new Paragraph("LESS: Opening balance as on " + dcFundDocumentDto.getExitCases()
//				+ " for exit cases :" + dcFundDocumentDto.getExitCasesAmount(), fontTittle);
//		dcFund12.setAlignment(dcFund12.ALIGN_LEFT);
//
//		Paragraph dcFund13 = new Paragraph("(details in Annexure I)", fontTittle);
//		dcFund13.setAlignment(dcFund13.ALIGN_LEFT);
//
//		Paragraph dcFund14 = new Paragraph("Net Opening balance as on " + dcFundDocumentDto.getNetOpeningBalanceDate()
//				+ ":" + dcFundDocumentDto.getNetOpeningBalanceAmount(), fontTittle);
//		dcFund14.setAlignment(dcFund14.ALIGN_LEFT);
//
//		Paragraph dcFund15 = new Paragraph("Purification On " + dcFundDocumentDto.getPurificationOn() + "", fontTittle);
//		dcFund15.setAlignment(dcFund15.ALIGN_LEFT);
//
//		Paragraph dcFund16 = new Paragraph("Purification Off " + dcFundDocumentDto.getPurificationOff() + "\r\n"
//				+ "Revised Opening Balance                                                      "
//				+ dcFundDocumentDto.getRevisedOpeningBalance() + " \n\n", fontTittle);
//		dcFund16.setAlignment(dcFund16.ALIGN_LEFT);
//
//		Paragraph dcFund17 = new Paragraph("Total contribution received during the year [ Ann-II ] :          "
//				+ dcFundDocumentDto.getTotalContributionReceived() + "\r"
//				+ "ADD: Equitable interest received during the year [Ann III]                "
//				+ dcFundDocumentDto.getEquitableInterestReceived() + "" + "\r"
//				+ "LESS : Contribution for exit cases [ Ann-IV] :                   "
//				+ dcFundDocumentDto.getContributionExitCases() + "\n\n", fontTittle);
//		dcFund17.setAlignment(dcFund17.ALIGN_LEFT);
//
//		Paragraph dcFund18 = new Paragraph("NET Contribution during the year :                      "
//				+ dcFundDocumentDto.getNETContributionDuringTheYear() + "", fontTittle);
//		dcFund18.setAlignment(dcFund18.ALIGN_LEFT);
//
//		Paragraph dcFund19 = new Paragraph("ADD : Interest Credited for the                :                        "
//				+ dcFundDocumentDto.getAddInterest() + "  \r\n"
//				+ "Year for existing member for the policy as a Whole\n\n", fontTittle);
//		dcFund19.setAlignment(dcFund19.ALIGN_LEFT);
//
//		Paragraph dcFund20 = new Paragraph("Closing Balance As On " + dcFundDocumentDto.getClosingBalanceDate() + ":"
//				+ dcFundDocumentDto.getClosingBalanceAmount(), fontTittle);
//		dcFund20.setAlignment(dcFund20.ALIGN_LEFT);
//
//		Paragraph dcFund21 = new Paragraph(
//				"Any discrepancy in the statement may be brought to our notice immediately\r\n" + "Thanking You,\n\n",
//				fontTittle);
//		dcFund21.setAlignment(dcFund21.ALIGN_LEFT);
//
//		Paragraph dcFund22 = new Paragraph(" Yours faithfully  \n\n", fontTittle);
//		dcFund22.setAlignment(dcFund22.ALIGN_LEFT);
//
//		Paragraph dcFund23 = new Paragraph("Manager (P&GS) \n\n", fontTittle);
//		dcFund23.setAlignment(dcFund23.ALIGN_LEFT);
//
//		Paragraph dcFund24 = new Paragraph("Encl : Annexures I to IV and member wise statement", fontTittle);
//		dcFund24.setAlignment(dcFund24.ALIGN_LEFT);
//
//		document.add(dcFund);
//		document.add(dcFund1);
//		document.add(dcFund2);
//		document.add(dcFund3);
//		document.add(dcFund4);
//		document.add(dcFund5);
//		document.add(dcFund6);
//		document.add(dcFund7);
//		document.add(dcFundempty);
//		document.add(dcFund8);
//		document.add(dcFundempty);
//		document.add(dcFund9);
//		document.add(dcFund10);
//		document.add(dcFundempty);
//		document.add(dcFund11);
//		document.add(dcFund12);
//		document.add(dcFund13);
//		document.add(dcFund14);
//		document.add(dcFund15);
//		document.add(dcFund16);
//		document.add(dcFund17);
//		document.add(dcFund18);
//		document.add(dcFundempty);
//		document.add(dcFund19);
//		document.add(dcFund20);
//		document.add(dcFundempty);
//		document.add(dcFund21);
//		document.add(dcFund22);
//		document.add(dcFund23);
//		document.add(dcFund24);
//		document.close();
//		byte[] bytes = baos.toByteArray();
//
//		return bytes;
//	}
//
//	public static byte[] claimForwardingLetter(ClaimForwardingLetterDto claimForwardingLetterDto)
//			throws DocumentException {
//		LocalDate dateObj = LocalDate.now();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//		String date = dateObj.format(formatter);
//		SimpleDateFormat formattedata = new SimpleDateFormat("dd-MM-yyyy");
//		Document document = new Document();
//
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//		PdfWriter.getInstance(document, baos);
//
//		document.open();
//
//		Font fontTittle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
//		fontTittle.setSize(10);
//
//		Paragraph claimFrd = new Paragraph("LIFE INSURANCE CORPORATION OF INDIA\r\n" + "P & GS UNIT", fontTittle);
//		claimFrd.setAlignment(claimFrd.ALIGN_LEFT);
//
//		Paragraph claimFrd1 = new Paragraph(claimForwardingLetterDto.getProcessingUnitAddressLine1(), fontTittle);
//		claimFrd1.setAlignment(claimFrd1.ALIGN_LEFT);
//
//		Paragraph claimFrd2 = new Paragraph(claimForwardingLetterDto.getProcessingUnitTelephoneandFax(), fontTittle);
//		claimFrd2.setAlignment(claimFrd2.ALIGN_LEFT);
//
//		Paragraph claimFrd3 = new Paragraph(claimForwardingLetterDto.getProcessingUnitEmail(), fontTittle);
//		claimFrd3.setAlignment(claimFrd3.ALIGN_LEFT);
//
//		Paragraph line = new Paragraph(
//				"--------------------------------------------------------------------------------------------------------------------------",
//				fontTittle);
//
//		Paragraph line1 = new Paragraph("------------", fontTittle);
//
//		Paragraph claimFrd4 = new Paragraph(claimForwardingLetterDto.getMemberName(), fontTittle);
//		claimFrd4.setAlignment(claimFrd4.ALIGN_LEFT);
//
//		Paragraph claimFrd5 = new Paragraph(claimForwardingLetterDto.getMemberAddressLine1(), fontTittle);
//		claimFrd5.setAlignment(claimFrd5.ALIGN_LEFT);
//
//		Paragraph claimFrd6 = new Paragraph(claimForwardingLetterDto.getMemberAddressLine2(), fontTittle);
//		claimFrd6.setAlignment(claimFrd6.ALIGN_LEFT);
//
//		Paragraph claimFrd7 = new Paragraph(claimForwardingLetterDto.getMemberAddressLine3(), fontTittle);
//		claimFrd7.setAlignment(claimFrd7.ALIGN_LEFT);
//
//		Paragraph claimFrd8 = new Paragraph(claimForwardingLetterDto.getMemberState(), fontTittle);
//		claimFrd8.setAlignment(claimFrd8.ALIGN_LEFT);
//
//		Paragraph claimFrd9 = new Paragraph("Dear Sirs,\n\n", fontTittle);
//		claimFrd9.setAlignment(claimFrd9.ALIGN_LEFT);
//
//		Paragraph claimFrd10 = new Paragraph(
//				"Re: Master Policy No GSCA / " + claimForwardingLetterDto.getPolicyNo() + "\r\n" + "\n\n", fontTittle);
//		claimFrd10.setAlignment(claimFrd10.ALIGN_LEFT);
//
//		Paragraph claimFrd11 = new Paragraph("Mode of Exit : " + claimForwardingLetterDto.getModeOfExit()
//				+ "		Name of Member : " + claimForwardingLetterDto.getNameOfMember() + "\r\n" + "", fontTittle);
//		claimFrd11.setAlignment(claimFrd11.ALIGN_LEFT);
//
//		Paragraph claimFrd12 = new Paragraph("=================================================================",
//				fontTittle);
//		claimFrd12.setAlignment(claimFrd12.ALIGN_LEFT);
//
//		Paragraph claimFrd13 = new Paragraph("=====", fontTittle);
//		claimFrd13.setAlignment(claimFrd13.ALIGN_LEFT);
//
//		Paragraph claimFrd14 = new Paragraph("We have pleasure in informing you that we have commenced payment of "
//				+ "Annuity,\r\n" + "due to you Vide Annuity No " + claimForwardingLetterDto.getAnnuityNumber() + "\r\n"
//				+ "\r\n" + "Please Note to mention the above mentioned Annuity No in all future correspondence.\r\n"
//				+ "\r\n" + "First Annuity Instalment due " + claimForwardingLetterDto.getDueDate() + " Annuity Amount: "
//				+ claimForwardingLetterDto.getAnnuityAmount() + "   Mode of Annuity: "
//				+ claimForwardingLetterDto.getModeOfAnnuity() + "\r\n" + "\r\n"
//				+ "Purchase Price: (In Case of ROC) Rs. " + claimForwardingLetterDto.getPurchasePrice() + "\n\n"
//				+ "\r\n" + "Nominee Name: " + claimForwardingLetterDto.getNameOfNominee() + "	Type of Annuity: "
//				+ claimForwardingLetterDto.getTypeOfAnnuity() + "	Date of Birth:" + ""
//				+ claimForwardingLetterDto.getDOBOfNominee() + " \n\n" + "\r\n"
//				+ "Amount payable to annuitants will be decided at the time of claim\r\n" + "IFSC Code "
//				+ claimForwardingLetterDto.getIFSCCode() + "\r\n" + "Commutation Amount: "
//				+ claimForwardingLetterDto.getCommutationAmount1() + "\r\n"
//				+ "Income Tax Deducted from commuted value Paid: "
//				+ claimForwardingLetterDto.getTDSAppliedOnCommutation() + "\n\n" + "", fontTittle);
//		claimFrd14.setAlignment(claimFrd14.ALIGN_LEFT);
//
//		Paragraph claimFrd15 = new Paragraph("Encl:NEFT /Cheque No:" + claimForwardingLetterDto.getNEFTandChequeNo()
//				+ "			dated: " + claimForwardingLetterDto.getDateOfNEFTandCheque() + " for Rs "
//				+ claimForwardingLetterDto.getCommutationAmount2() + "\r\n" + "Favoring: "
//				+ claimForwardingLetterDto.getMemberName1() + "\n\n" + "", fontTittle);
//		claimFrd15.setAlignment(claimFrd15.ALIGN_LEFT);
//
//		Paragraph claimFrd16 = new Paragraph("	Yours faithfully\n\n", fontTittle);
//		claimFrd16.setAlignment(claimFrd16.ALIGN_RIGHT);
//		claimFrd16.setIndentationRight(50);
//
//		Paragraph claimFrd17 = new Paragraph(claimForwardingLetterDto.getAuthorisedPersonName(), fontTittle);
//		claimFrd17.setAlignment(claimFrd17.ALIGN_RIGHT);
//
//		Paragraph claimFrd18 = new Paragraph("Cc to :" + claimForwardingLetterDto.getMPHName(), fontTittle);
//		claimFrd18.setAlignment(claimFrd18.ALIGN_LEFT);
//
//		Paragraph claimFrd19 = new Paragraph(claimForwardingLetterDto.getTrusteeName(), fontTittle);
//		claimFrd19.setAlignment(claimFrd19.ALIGN_LEFT);
//
//		Paragraph claimFrd20 = new Paragraph(claimForwardingLetterDto.getTrusteeAddressLine1(), fontTittle);
//		claimFrd20.setAlignment(claimFrd20.ALIGN_LEFT);
//
//		Paragraph claimFrd21 = new Paragraph(claimForwardingLetterDto.getTrusteeAddressLine2(), fontTittle);
//		claimFrd21.setAlignment(claimFrd21.ALIGN_LEFT);
//
//		Paragraph claimFrd22 = new Paragraph(claimForwardingLetterDto.getTrusteeAddressLine3(), fontTittle);
//		claimFrd22.setAlignment(claimFrd22.ALIGN_LEFT);
//
//		Paragraph claimFrd23 = new Paragraph(claimForwardingLetterDto.getTrusteeAddressLine4(), fontTittle);
//		claimFrd23.setAlignment(claimFrd23.ALIGN_LEFT);
//
//		document.add(claimFrd);
//		document.add(claimFrd1);
//		document.add(claimFrd2);
//		document.add(claimFrd3);
//		document.add(line);
//		document.add(line1);
//		document.add(claimFrd4);
//		document.add(claimFrd5);
//		document.add(claimFrd6);
//		document.add(claimFrd7);
//		document.add(claimFrd8);
//		document.add(claimFrd9);
//		document.add(claimFrd10);
//		document.add(claimFrd11);
//		document.add(claimFrd12);
//		document.add(claimFrd13);
//		document.add(claimFrd14);
//		document.add(claimFrd15);
//		document.add(claimFrd16);
//		document.add(claimFrd17);
//		document.add(claimFrd18);
//		document.add(claimFrd19);
//		document.add(claimFrd20);
//		document.add(claimFrd21);
//		document.add(claimFrd22);
//		document.add(claimFrd23);
//		document.close();
//		byte[] bytes = baos.toByteArray();
//
//		return bytes;
//
//	}
//
//	public static byte[] SpPaymentDocument1(SpPaymentPdfDto spPaymentPdfDto) throws DocumentException, IOException {
//
//		Document document = new Document();
//
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//		PdfWriter.getInstance(document, baos);
//
//		document.open();
//
//		Font fontTittle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
//		fontTittle.setSize(9);
//
//		PdfPTable mainTables = new PdfPTable(3);
//		mainTables.setWidthPercentage(100.0f);
//
//		PdfPCell firstTableCells = new PdfPCell();
//		firstTableCells.setBorder(Rectangle.NO_BORDER);
//		mainTables.addCell(firstTableCells);
//
//		PdfPCell secondTableCells = new PdfPCell();
//		secondTableCells.setBorder(Rectangle.NO_BORDER);
//
////			Image image = Image.getInstance("" +urlLocal+"/assets/img/logo.png");
//		Image image = Image.getInstance(environment.getProperty("LIC_LOGO_PATH"));
//		image.scaleAbsolute(100f, 50f); // image width,height
//		image.setAlignment(Element.ALIGN_CENTER);
//		secondTableCells.addElement(image);
//		mainTables.addCell(secondTableCells);
//
//		PdfPCell thirdTableCells = new PdfPCell();
//		thirdTableCells.setBorder(Rectangle.NO_BORDER);
//		Font fontH13 = FontFactory.getFont(FontFactory.TIMES);
//		PdfPTable table13 = new PdfPTable(1);
//		float[] columnWidths13 = { 4.5f };
//		table13.setWidths(columnWidths13);
//		table13.setWidthPercentage(100f);
//		table13.setTotalWidth(PageSize.A4.getWidth());
//		table13.addCell(new PdfPCell(new Phrase("PAYMENT VOUCHER", fontH13)));
//		table13.setHorizontalAlignment(Element.ALIGN_BOTTOM);
//		fontH13.setSize(12);
//		thirdTableCells.setVerticalAlignment(Element.ALIGN_BOTTOM);
//		thirdTableCells.addElement(table13);
//
//		mainTables.addCell(thirdTableCells);
//
//		PdfPTable table01 = new PdfPTable(2);
//		float[] columnWidths01 = { 3f, 1f };
//		table01.setWidthPercentage(100f);
//		table01.setWidths(columnWidths01);
//
//		Font fontH1 = FontFactory.getFont(FontFactory.TIMES);
//		PdfPCell ph01 = new PdfPCell(new Phrase("P & GS Dept. ", fontH1));
//		ph01.setBorder(Rectangle.NO_BORDER);
//		table01.addCell(ph01);
//		PdfPCell ph02 = new PdfPCell(new Phrase("Voucher No. " + spPaymentPdfDto.getVouchernum(), fontH1));
//		ph02.setBorder(Rectangle.NO_BORDER);
//		table01.addCell(ph02);
//		PdfPCell ph03 = new PdfPCell(new Phrase("Divl.Name: " + spPaymentPdfDto.getDivisionalName(), fontH1));
//		ph03.setBorder(Rectangle.NO_BORDER);
//		table01.addCell(ph03);
//		PdfPCell ph04 = new PdfPCell(new Phrase("Date: " + spPaymentPdfDto.getDate(), fontH1));
//		ph04.setBorder(Rectangle.NO_BORDER);
//		table01.addCell(ph04);
//
//		Paragraph renewal3 = new Paragraph("To Cash / Banking Section", fontTittle);
//		renewal3.setAlignment(Element.ALIGN_CENTER);
//
//		Paragraph renewal4 = new Paragraph("P & GS Dept.", fontTittle);
//		renewal4.setAlignment(Element.ALIGN_CENTER);
//
//		Paragraph renewal5 = new Paragraph(
//				"Please adjust/Issue Crossed/Order Cheque/Cash / M.O / Postage Stamps favouring :", fontTittle);
//		renewal5.setAlignment(Element.ALIGN_LEFT);
//
//		Paragraph renewal6 = new Paragraph("For " + spPaymentPdfDto.getAmountInWords(), fontTittle);
//		renewal6.setAlignment(Element.ALIGN_LEFT);
//
//		Paragraph renewal7 = new Paragraph(
//				"Policy No.:  " + spPaymentPdfDto.getPolicyNumber()
//						+ "                                       Scheme Name :" + spPaymentPdfDto.getSchemeName(),
//				fontTittle);
//		renewal7.setAlignment(Element.ALIGN_LEFT);
//
//		Paragraph paragraph1 = new Paragraph("\n\n", fontTittle);
//		paragraph1.setAlignment(Element.ALIGN_CENTER);
//		paragraph1.setIndentationLeft(36);
//
//		PdfPTable table1 = new PdfPTable(4);
//		float[] columnWidths = { 3f, 1f, 1.5f, 1.5f };
//		table1.setWidthPercentage(100f);
//		table1.setWidths(columnWidths);
//		table1.setTotalWidth(PageSize.A4.getWidth());
//		table1.addCell(new PdfPCell(new Phrase("Head of Account", fontH1)));
//		table1.addCell(new PdfPCell(new Phrase("Code", fontH1)));
//		PdfPCell ph1 = new PdfPCell(new Phrase("Debit Amount", fontH1));
//		ph1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		table1.addCell(ph1);
//		PdfPCell ph2 = new PdfPCell(new Phrase("Credit Amount", fontH1));
//		ph2.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		table1.addCell(ph2);
//		table1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		fontH1.setSize(9);
//
//		Font fontH2 = FontFactory.getFont(FontFactory.TIMES);
//		PdfPTable table2 = new PdfPTable(4);
//		float[] columnWidths1 = { 3f, 1f, 1.5f, 1.5f };
//		table2.setWidths(columnWidths1);
//		table2.setWidthPercentage(100f);
//		table2.setTotalWidth(PageSize.A4.getWidth());
//		Double credit = 0.0;
//		Double debit = 0.0;
//		for (AdjustmentVoucherDetailDto adjustmentVoucherDetailDto : spPaymentPdfDto.getVoucherDetail()) {
//
//			credit = credit + adjustmentVoucherDetailDto.getCredit();
//			debit = debit + adjustmentVoucherDetailDto.getDebit();
//			table1.addCell(new PdfPCell(new Phrase(adjustmentVoucherDetailDto.getHeadOfAccount(), fontH1)));
//			table1.addCell(new PdfPCell(new Phrase(adjustmentVoucherDetailDto.getCode(), fontH1)));
//			PdfPCell p1 = new PdfPCell(
//					new Phrase(String.format("%,.2f", adjustmentVoucherDetailDto.getDebit()), fontH1));
//			p1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			table1.addCell(p1);
//			PdfPCell p2 = new PdfPCell(
//					new Phrase(String.format("%,.2f", adjustmentVoucherDetailDto.getCredit()), fontH1));
//			p2.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			table1.addCell(p2);
//
//		}
//		table2.setHorizontalAlignment(Element.ALIGN_CENTER);
//		fontH2.setSize(9);
//
//		Font fontH3 = FontFactory.getFont(FontFactory.TIMES);
//		PdfPTable table3 = new PdfPTable(3);
//		float[] columnWidths2 = { 4f, 1.5f, 1.5f };
//		table3.setWidths(columnWidths2);
//		table3.setWidthPercentage(100f);
//		table3.setTotalWidth(PageSize.A4.getWidth());
//		table3.addCell(new PdfPCell(new Phrase("Total", fontH3)));
//
//		PdfPCell p1 = new PdfPCell(new Phrase(String.format("%,.2f", debit), fontH1));
//		p1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		table3.addCell(p1);
//		PdfPCell p2 = new PdfPCell(new Phrase(String.format("%,.2f", credit), fontH1));
//		p2.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		table3.addCell(p2);
//		table3.setHorizontalAlignment(Element.ALIGN_CENTER);
//		fontH3.setSize(9);
//
//		Font fontH4 = FontFactory.getFont(FontFactory.TIMES);
//		PdfPTable table5 = new PdfPTable(1);
//		float[] columnWidths3 = { 5f };
//		table5.setWidths(columnWidths3);
//		table5.setWidthPercentage(100f);
//		table5.setTotalWidth(PageSize.A4.getWidth());
//		table5.addCell(new PdfPCell(new Phrase("\n\nVoucher No      :" + spPaymentPdfDto.getVoucherNumber() + "\r\n"
//				+ "Voucher Date   :" + spPaymentPdfDto.getVoucherDate() + "\r\n" + "Prepared By       : "
//				+ spPaymentPdfDto.getPreparedBy() + "\r\n\n", fontH4)));
//		table5.setHorizontalAlignment(Element.ALIGN_CENTER);
//		fontH4.setSize(9);
//
//		Paragraph renewal13 = new Paragraph("As per the following details \n\n", fontTittle);
//		renewal13.setAlignment(Element.ALIGN_LEFT);
//
//		Paragraph renewal14 = new Paragraph(" ", fontTittle);
//		renewal14.setAlignment(Element.ALIGN_LEFT);
//
//		Paragraph paragraph = new Paragraph();
//
//		PdfPTable mainTable = new PdfPTable(2);
//		mainTable.setWidthPercentage(100.0f);
//
//		PdfPCell firstTableCell = new PdfPCell(new Phrase("  ", fontH1));
//		firstTableCell.setBorder(Rectangle.NO_BORDER);
//		Font fontH7 = FontFactory.getFont(FontFactory.TIMES);
//		PdfPTable table8 = new PdfPTable(1);
//		table8.setWidthPercentage(50f);
//		float[] columnWidths7 = { 2f };
//		table8.setWidths(columnWidths7);
//		table8.setTotalWidth(PageSize.A4.getWidth());
//		PdfPCell pf1 = new PdfPCell(new Phrase("Prepared By:" + "\r\n\n" + "Checked  By:" + "\r\n\n" + "Date:"
//				+ spPaymentPdfDto.getEndDate() + "\r\n" + "Cheque No:" + spPaymentPdfDto.getChequeNumber() + "\r\n"
//				+ "Drawn No:" + spPaymentPdfDto.getDrawn(), fontH3));
//		pf1.setBorder(Rectangle.NO_BORDER);
//		table8.addCell(pf1);
//		table8.setHorizontalAlignment(Element.ALIGN_LEFT);
//		fontH7.setSize(9);
//		firstTableCell.addElement(table8);
//		mainTable.addCell(firstTableCell);
//
//		PdfPCell secondTableCell = new PdfPCell();
//		secondTableCell.setBorder(Rectangle.NO_BORDER);
//		Font fontH5 = FontFactory.getFont(FontFactory.TIMES);
//		PdfPTable table6 = new PdfPTable(2);
//		float[] columnWidths4 = { 3f, 3f };
//		table6.setWidths(columnWidths4);
//		table6.setTotalWidth(PageSize.A4.getWidth());
//		table6.addCell(new PdfPCell(new Phrase(
//				"                PAY\n" + "Sign:\n\n" + "Design:\n\n" + "Date:" + spPaymentPdfDto.getPayDate(),
//				fontH3)));
//		table6.addCell(new PdfPCell(new Phrase(
//				"                PAID\n" + "Sign:\n" + "\n\n\n" + "Date:" + spPaymentPdfDto.getPaidDate(), fontH3)));
//		table6.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		fontH3.setSize(9);
//
//		PdfPCell thirdTableCell = new PdfPCell(new Phrase("Initials of Officers Signing cheques.", fontH3));
//		thirdTableCell.setColspan(2);
//		table6.addCell(thirdTableCell);
//
//		secondTableCell.addElement(table6);
//		mainTable.addCell(secondTableCell);
//
//		paragraph.add(mainTable);
//
//		document.add(mainTables);
//		document.add(table01);
//		document.add(renewal3);
//		document.add(renewal4);
//		document.add(renewal5);
//		document.add(renewal6);
//		document.add(renewal7);
//		document.add(renewal14);
//		document.add(table1);
//		document.add(table2);
//		document.add(table3);
//		document.add(paragraph1);
//		document.add(renewal13);
//		document.add(table5);
//		document.add(renewal14);
//		document.add(paragraph);
//		paragraph.add(mainTable);
//
//		document.close();
//		byte[] bytes = baos.toByteArray();
//		return bytes;
//
//	}
//
////			private static String urlLocal = "http://localhost:4202";
//
//	public static byte[] spDocument(SpPdfDto sppdfdto) throws DocumentException, IOException {
//
//		Document document = new Document();
//
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//		PdfWriter.getInstance(document, baos);
//
//		document.open();
//
//		Font fontTittle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
//		fontTittle.setSize(9);
//
//		PdfPTable mainTables = new PdfPTable(3);
//		mainTables.setWidthPercentage(100.0f);
//
//		PdfPCell firstTableCells = new PdfPCell();
//		firstTableCells.setBorder(Rectangle.NO_BORDER);
//		mainTables.addCell(firstTableCells);
//
//		PdfPCell secondTableCells = new PdfPCell();
//		secondTableCells.setBorder(Rectangle.NO_BORDER);
////		Image image = Image.getInstance("" + urlLocal + "/assets/img/logo.png");
//		
//		Image image = Image.getInstance(environment.getProperty("LIC_LOGO_PATH"));
//
//		image.scaleAbsolute(100f, 50f); // image width,height
//		image.setAlignment(Element.ALIGN_CENTER);
//		secondTableCells.addElement(image);
//		mainTables.addCell(secondTableCells);
//
//		PdfPCell thirdTableCells = new PdfPCell();
//		thirdTableCells.setBorder(Rectangle.NO_BORDER);
//		Font fontH13 = FontFactory.getFont(FontFactory.TIMES);
//		PdfPTable table13 = new PdfPTable(1);
//		float[] columnWidths13 = { 4.5f };
//		table13.setWidths(columnWidths13);
//		table13.setWidthPercentage(100f);
//		table13.setTotalWidth(PageSize.A4.getWidth());
//		table13.addCell(new PdfPCell(new Phrase("ADJUSTMENT VOUCHER", fontH13)));
//		table13.setHorizontalAlignment(Element.ALIGN_BOTTOM);
//		fontH13.setSize(12);
//		thirdTableCells.setVerticalAlignment(Element.ALIGN_BOTTOM);
//		thirdTableCells.addElement(table13);
//
//		mainTables.addCell(thirdTableCells);
//
//		PdfPTable table01 = new PdfPTable(2);
//		float[] columnWidths01 = { 3f, 1f };
//		table01.setWidthPercentage(100f);
//		table01.setWidths(columnWidths01);
//
//		Font fontH1 = FontFactory.getFont(FontFactory.TIMES);
//		PdfPCell ph01 = new PdfPCell(new Phrase("P & GS Dept. ", fontH1));
//		ph01.setBorder(Rectangle.NO_BORDER);
//		table01.addCell(ph01);
//		PdfPCell ph02 = new PdfPCell(new Phrase("Voucher No. " + sppdfdto.getVouchernum(), fontH1));
//		ph02.setBorder(Rectangle.NO_BORDER);
//		table01.addCell(ph02);
//		PdfPCell ph03 = new PdfPCell(new Phrase("Divl.Name: " + sppdfdto.getDivisionalName(), fontH1));
//		ph03.setBorder(Rectangle.NO_BORDER);
//		table01.addCell(ph03);
//		PdfPCell ph04 = new PdfPCell(new Phrase("Date: " + sppdfdto.getDate(), fontH1));
//		ph04.setBorder(Rectangle.NO_BORDER);
//		table01.addCell(ph04);
//
//		Paragraph renewal3 = new Paragraph("To Cash / Banking Section", fontTittle);
//		renewal3.setAlignment(Element.ALIGN_CENTER);
//
//		Paragraph renewal4 = new Paragraph("P & GS Dept.", fontTittle);
//		renewal4.setAlignment(Element.ALIGN_CENTER);
//
//		Paragraph renewal5 = new Paragraph(
//				"Please adjust/Issue Crossed/Order Cheque/Cash / M.O / Postage Stamps favouring :", fontTittle);
//		renewal5.setAlignment(Element.ALIGN_LEFT);
//
//		Paragraph renewal6 = new Paragraph("For " + sppdfdto.getAmountInWords(), fontTittle);
//		renewal6.setAlignment(Element.ALIGN_LEFT);
//
//		Paragraph renewal7 = new Paragraph("Policy No.:  " + sppdfdto.getPolicyNumber()
//				+ "                                       Scheme Name :" + sppdfdto.getSchemeName(), fontTittle);
//		renewal7.setAlignment(Element.ALIGN_LEFT);
//
//		Paragraph paragraph1 = new Paragraph("\n\n", fontTittle);
//		paragraph1.setAlignment(Element.ALIGN_CENTER);
//		paragraph1.setIndentationLeft(36);
//
//		PdfPTable table1 = new PdfPTable(4);
//		float[] columnWidths = { 3f, 1f, 1.5f, 1.5f };
//		table1.setWidthPercentage(100f);
//		table1.setWidths(columnWidths);
//		table1.setTotalWidth(PageSize.A4.getWidth());
//		table1.addCell(new PdfPCell(new Phrase("Head of Account", fontH1)));
//		table1.addCell(new PdfPCell(new Phrase("Code", fontH1)));
//		PdfPCell ph1 = new PdfPCell(new Phrase("Debit Amount", fontH1));
//		ph1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		table1.addCell(ph1);
//		PdfPCell ph2 = new PdfPCell(new Phrase("Credit Amount", fontH1));
//		ph2.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		table1.addCell(ph2);
//		table1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		fontH1.setSize(9);
//
//		Font fontH2 = FontFactory.getFont(FontFactory.TIMES);
//		PdfPTable table2 = new PdfPTable(4);
//		float[] columnWidths1 = { 3f, 1f, 1.5f, 1.5f };
//		table2.setWidths(columnWidths1);
//		table2.setWidthPercentage(100f);
//		table2.setTotalWidth(PageSize.A4.getWidth());
//		Double credit = 0.0;
//		Double debit = 0.0;
//		for (AdjustmentVoucherDetailDto adjustmentVoucherDetailDto : sppdfdto.getVoucherDetail()) {
//
//			credit = credit + adjustmentVoucherDetailDto.getCredit();
//			debit = debit + adjustmentVoucherDetailDto.getDebit();
//			table1.addCell(new PdfPCell(new Phrase(adjustmentVoucherDetailDto.getHeadOfAccount(), fontH1)));
//			table1.addCell(new PdfPCell(new Phrase(adjustmentVoucherDetailDto.getCode(), fontH1)));
//			PdfPCell p1 = new PdfPCell(
//					new Phrase(String.format("%,.2f", adjustmentVoucherDetailDto.getDebit()), fontH1));
//			p1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			table1.addCell(p1);
//			PdfPCell p2 = new PdfPCell(
//					new Phrase(String.format("%,.2f", adjustmentVoucherDetailDto.getCredit()), fontH1));
//			p2.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			table1.addCell(p2);
//
//		}
//		table2.setHorizontalAlignment(Element.ALIGN_CENTER);
//		fontH2.setSize(9);
//
//		Font fontH3 = FontFactory.getFont(FontFactory.TIMES);
//		PdfPTable table3 = new PdfPTable(3);
//		float[] columnWidths2 = { 4f, 1.5f, 1.5f };
//		table3.setWidths(columnWidths2);
//		table3.setWidthPercentage(100f);
//		table3.setTotalWidth(PageSize.A4.getWidth());
//		table3.addCell(new PdfPCell(new Phrase("Total", fontH3)));
//
//		PdfPCell p1 = new PdfPCell(new Phrase(String.format("%,.2f", debit), fontH1));
//		p1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		table3.addCell(p1);
//		PdfPCell p2 = new PdfPCell(new Phrase(String.format("%,.2f", credit), fontH1));
//		p2.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		table3.addCell(p2);
//		table3.setHorizontalAlignment(Element.ALIGN_CENTER);
//		fontH3.setSize(9);
//
//		Paragraph renewal13 = new Paragraph("As per the following details \n\n", fontTittle);
//		renewal13.setAlignment(Element.ALIGN_LEFT);
//
//		PdfPTable tablesup = new PdfPTable(1);
//		tablesup.setHorizontalAlignment(Element.ALIGN_LEFT);
//		PdfPCell ps1 = new PdfPCell(new Phrase(
//				"Supplementry   Deposit  Adjustment\r\n" + "Balance Deposit:" + sppdfdto.getBalanceDeposit(), fontH3));
//		ps1.setBorder(Rectangle.NO_BORDER);
//		tablesup.addCell(ps1);
//
//		PdfPTable tablesdown1 = new PdfPTable(3);
//		tablesdown1.setHorizontalAlignment(Element.ALIGN_LEFT);
//		PdfPCell psh01 = new PdfPCell(new Phrase("ARD:" + sppdfdto.getArd(), fontH3));
//		psh01.setBorder(Rectangle.NO_BORDER);
//		tablesdown1.addCell(psh01);
//		PdfPCell psh02 = new PdfPCell(new Phrase("Mode:" + sppdfdto.getMode(), fontH3));
//		psh02.setBorder(Rectangle.NO_BORDER);
//		tablesdown1.addCell(psh02);
//		PdfPCell psh03 = new PdfPCell(new Phrase(" Due For:" + sppdfdto.getDuedate(), fontH3));
//		psh03.setBorder(Rectangle.NO_BORDER);
//		tablesdown1.addCell(psh03);
//
//		PdfPTable tablesup1 = new PdfPTable(5);
//		PdfPCell pstitle = new PdfPCell(new Phrase("\nDetails Adjustment", fontH3));
//		pstitle.setBorder(Rectangle.NO_BORDER);
//		pstitle.setColspan(5);
//		tablesup1.addCell(pstitle);
//
//		PdfPCell psh1 = new PdfPCell(new Phrase("No", fontH3));
//		psh1.setBorder(Rectangle.BOTTOM);
//		tablesup1.addCell(psh1);
//		PdfPCell psh2 = new PdfPCell(new Phrase("Date", fontH3));
//		psh2.setBorder(Rectangle.BOTTOM);
//		tablesup1.addCell(psh2);
//		PdfPCell psh3 = new PdfPCell(new Phrase("Amount", fontH3));
//		psh3.setBorder(Rectangle.BOTTOM);
//		psh3.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		tablesup1.addCell(psh3);
//		PdfPCell psh4 = new PdfPCell(new Phrase("Adjusted", fontH3));
//		psh4.setBorder(Rectangle.BOTTOM);
//		psh4.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		tablesup1.addCell(psh4);
//		PdfPCell psh5 = new PdfPCell(new Phrase("Balance", fontH3));
//		psh5.setBorder(Rectangle.BOTTOM);
//		psh5.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		tablesup1.addCell(psh5);
//
//		Font fontH21 = FontFactory.getFont(FontFactory.TIMES);
//		fontH21.setSize(9);
//		PdfPTable table21 = new PdfPTable(5);
//		float[] columnWidths11 = { 3f, 1f, 1.5f, 1.5f, 1.5f };
//		table21.setWidths(columnWidths11);
//		table21.setWidthPercentage(100f);
//		table21.setTotalWidth(PageSize.A4.getWidth());
//		for (SupplementaryAdjustmentDto addSupplymentaryAdjustmentDto : sppdfdto.getSupplymentary()) {
//			PdfPCell p14 = new PdfPCell(new Phrase(addSupplymentaryAdjustmentDto.getDetailsNo().toString(), fontH21));
//			p14.setBorder(Rectangle.NO_BORDER);
//			tablesup1.addCell(p14);
//			PdfPCell p15 = new PdfPCell(new Phrase(addSupplymentaryAdjustmentDto.getDetailsDate().toString(), fontH21));
//			p15.setBorder(Rectangle.NO_BORDER);
//			tablesup1.addCell(p15);
//			PdfPCell p11 = new PdfPCell(
//					new Phrase(String.format("%,.2f", addSupplymentaryAdjustmentDto.getDetailsAmount()), fontH21));
//			p11.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			p11.setBorder(Rectangle.NO_BORDER);
//			tablesup1.addCell(p11);
//			PdfPCell p12 = new PdfPCell(
//					new Phrase(String.format("%,.2f", addSupplymentaryAdjustmentDto.getDetailsAdjusted()), fontH21));
//			p12.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			p12.setBorder(Rectangle.NO_BORDER);
//			tablesup1.addCell(p12);
//			PdfPCell p13 = new PdfPCell(
//					new Phrase(String.format("%,.2f", addSupplymentaryAdjustmentDto.getDetailsBalance()), fontH21));
//			p13.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			p13.setBorder(Rectangle.NO_BORDER);
//			tablesup1.addCell(p13);
//
//		}
//		tablesup1.setHorizontalAlignment(Element.ALIGN_LEFT);
//		fontH2.setSize(9);
//
//		PdfPTable supContainer = new PdfPTable(1);
//		supContainer.setWidthPercentage(100f);
//		supContainer.setTotalWidth(PageSize.A4.getWidth());
//
//		PdfPCell pc13 = new PdfPCell();
//		pc13.addElement(tablesup);
//		pc13.addElement(tablesdown1);
//		pc13.addElement(tablesup1);
//		fontH3.setSize(9);
//		supContainer.addCell(pc13);
//
//		Paragraph paragraph2 = new Paragraph("\n\n", fontTittle);
//		paragraph2.setAlignment(Element.ALIGN_LEFT);
//		paragraph2.setIndentationLeft(36);
//
//		Paragraph renewal14 = new Paragraph(" ", fontTittle);
//		renewal14.setAlignment(Element.ALIGN_LEFT);
//
//		Paragraph paragraph = new Paragraph();
//
//		PdfPTable mainTable = new PdfPTable(2);
//		mainTable.setWidthPercentage(100.0f);
//
//		PdfPCell firstTableCell = new PdfPCell(new Phrase("  ", fontH1));
//		firstTableCell.setBorder(Rectangle.NO_BORDER);
//		Font fontH7 = FontFactory.getFont(FontFactory.TIMES);
//		PdfPTable table8 = new PdfPTable(1);
//		table8.setWidthPercentage(50f);
//		float[] columnWidths7 = { 2f };
//		table8.setWidths(columnWidths7);
//		table8.setTotalWidth(PageSize.A4.getWidth());
//		PdfPCell pf1 = new PdfPCell(new Phrase(
//				"Prepared By:" + "\r\n\n" + "Checked  By:" + "\r\n\n" + "Date:" + sppdfdto.getEndDate() + "\r\n"
//						+ "Cheque No:" + sppdfdto.getChequeNumber() + "\r\n" + "Drawn No:" + sppdfdto.getDrawn(),
//				fontH3));
//		pf1.setBorder(Rectangle.NO_BORDER);
//		table8.addCell(pf1);
//		table8.setHorizontalAlignment(Element.ALIGN_LEFT);
//		fontH7.setSize(9);
//		firstTableCell.addElement(table8);
//		mainTable.addCell(firstTableCell);
//
//		PdfPCell secondTableCell = new PdfPCell();
//		secondTableCell.setBorder(Rectangle.NO_BORDER);
//		Font fontH5 = FontFactory.getFont(FontFactory.TIMES);
//		PdfPTable table6 = new PdfPTable(2);
//		float[] columnWidths4 = { 3f, 3f };
//		table6.setWidths(columnWidths4);
//		table6.setTotalWidth(PageSize.A4.getWidth());
//		table6.addCell(new PdfPCell(new Phrase(
//				"                PAY\n" + "Sign:\n\n" + "Design:\n\n" + "Date:" + sppdfdto.getPayDate(), fontH3)));
//		table6.addCell(new PdfPCell(new Phrase(
//				"                PAID\n" + "Sign:\n" + "\n\n\n" + "Date:" + sppdfdto.getPaidDate(), fontH3)));
//		table6.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		fontH3.setSize(9);
//
//		PdfPCell thirdTableCell = new PdfPCell(new Phrase("Initials of Officers Signing cheques.", fontH3));
//		thirdTableCell.setColspan(2);
//		table6.addCell(thirdTableCell);
//
//		secondTableCell.addElement(table6);
//		mainTable.addCell(secondTableCell);
//
//		paragraph.add(mainTable);
//
//		document.add(mainTables);
//		document.add(table01);
//		document.add(renewal3);
//		document.add(renewal4);
//		document.add(renewal5);
//		document.add(renewal6);
//		document.add(renewal7);
//		document.add(renewal14);
//		document.add(table1);
//		document.add(table2);
//		document.add(table3);
//		document.add(paragraph1);
//		document.add(renewal13);
//		document.add(supContainer);
//		document.add(paragraph2);
//		document.add(renewal14);
//		document.add(paragraph);
//		paragraph.add(mainTable);
//
//		document.close();
//		byte[] bytes = baos.toByteArray();
//		return bytes;
//	}
//	
//	
//	public static byte[] downloadSampleRenewal(LifeCoverDetailsDto parentDTOs) throws DocumentException {
//		
//		LocalDate dateObj = LocalDate.now();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//		String date = dateObj.format(formatter);
//
//		SimpleDateFormat formattedata = new SimpleDateFormat("dd-MM-yyyy");
//
//		Document document = new Document();
//
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		PdfWriter.getInstance(document, baos);
//
//		document.open();
//
//		Font fontTittle = FontFactory.getFont(FontFactory.HELVETICA);
//		fontTittle.setSize(10);
//
//		Paragraph superAni = new Paragraph(
//				"Life Insurance Corporation Of India\r\n" + "Pension & Group Schemes Dept\n P&Gs Dept Mdo-I",
//				fontTittle);
//		superAni.setAlignment(superAni.ALIGN_CENTER);
//
//		Paragraph superAni2 = new Paragraph(
//				"Ref : P&&GS/ NGSCA /UUser /706000463                                                                               Date "
//						+ date + "",
//				fontTittle);
//		superAni2.setAlignment(superAni.ALIGN_LEFT);
//
//		Paragraph superAni3 = new Paragraph("HINDLEVER PENSION FUND\r\n" + "", fontTittle);
//		superAni3.setAlignment(superAni.ALIGN_LEFT);
//
//		Paragraph superAni4 = new Paragraph(
//				parentDTOs.getCusName() + parentDTOs.getAddress1() + ",\n" + parentDTOs.getAddress2()
//						+ parentDTOs.getAddress3() + " ," + parentDTOs.getAddress4() + " \n" + parentDTOs.getAddress5(),
//				fontTittle);
//		superAni3.setAlignment(superAni.ALIGN_LEFT);
//
//		Paragraph superAni5 = new Paragraph("Dear Sirs,\r\n" + "", fontTittle);
//		superAni3.setAlignment(superAni.ALIGN_LEFT);
//
//		Paragraph superAni6 = new Paragraph("Re: Master Policy No :" + parentDTOs.getMasterPolicyNo()
//				+ "\n Mode of Exit :" + parentDTOs.getModeofExit(), fontTittle);
//
//		superAni6.setAlignment(superAni.ALIGN_CENTER);
//
//		Paragraph superAni7 = new Paragraph("We are enclosing herewith -------- ( ) Cheques in payment of "
//				+ "claims due to withdrawal of the member from the scheme, as per particulars given" + " below :\r\n"
//				+ "", fontTittle);
//		superAni3.setAlignment(superAni.ALIGN_LEFT);
//
//		Paragraph superAni8 = new Paragraph(
//				"   Encl:            Cheque No :                            dated :                                     for Rs : ",
//				fontTittle);
//		superAni3.setAlignment(superAni.ALIGN_LEFT);
//
//		Paragraph superAni9 = new Paragraph("Yours faithfully", fontTittle);
//		superAni9.setAlignment(superAni.ALIGN_RIGHT);
//
//		Paragraph superAni11 = new Paragraph(parentDTOs.getManagerName(), fontTittle);
//		superAni11.setAlignment(superAni.ALIGN_RIGHT);
//
//		Paragraph superAni10 = new Paragraph("Version: 23.02", fontTittle);
//		superAni10.setAlignment(superAni.ALIGN_LEFT);
//
//		PdfPTable table = new PdfPTable(6);
//
//		PdfPCell cell = new PdfPCell();
//
//		cell.setBorder(PdfPCell.TOP | PdfPCell.BOTTOM);
//		cell.setBorderWidth(1f);
//		cell.setBorderColor(BaseColor.BLACK);
//		cell.setPadding(5f);
//		cell.setUseVariableBorders(true);
//		cell.setBorderWidthTop(0.5f);
//		cell.setBorderWidthBottom(0.5f);
//		cell.setBorderColorTop(BaseColor.BLACK);
//		cell.setBorderColorBottom(BaseColor.BLACK);
//
//		
//		  cell.setPhrase(new Phrase("Licid ", fontTittle)); table.addCell(cell);
//		  cell.setPhrase(new Phrase("EmpNo ", fontTittle)); table.addCell(cell);
//		  cell.setPhrase(new Phrase("Name ", fontTittle)); table.addCell(cell);
//		  cell.setPhrase(new Phrase("Amount ", fontTittle)); table.addCell(cell);
//		  cell.setPhrase(new Phrase("Date ", fontTittle)); table.addCell(cell);
//		  cell.setPhrase(new Phrase("Refund Of Premium ", fontTittle));
//		  table.addCell(cell);
//		 
//		table.getDefaultCell().setBorderWidth(0);
//
//		/*
//		 * table.addCell((new Phrase("Licid",fontTittle))); table.addCell((new
//		 * Phrase("EmpNo",fontTittle))); table.addCell((new Phrase("Name",fontTittle)));
//		 * table.addCell((new Phrase("Amount",fontTittle))); table.addCell((new
//		 * Phrase("Date",fontTittle))); table.addCell((new
//		 * Phrase("Refund Of Premium",fontTittle)));
//		 * 
//		 */
//
//		
//
//		// table.setWidths(new float[] { 4, 4, 4, 4, 4, 4, });
//
//		/*
//		 * table.addCell(new PdfPCell(new Phrase(" Licid", fontTittle)));
//		 * 
//		 * table.addCell(new PdfPCell(new Phrase(" EmpNo.", fontTittle)));
//		 * 
//		 * table.addCell(new PdfPCell(new Phrase(" Name", fontTittle)));
//		 * 
//		 * table.addCell(new PdfPCell(new Phrase("  Amount ", fontTittle)));
//		 * 
//		 * table.addCell(new PdfPCell(new Phrase(" Date ", fontTittle)));
//		 * 
//		 * table.addCell(new PdfPCell(new Phrase(" Refund Of Premium ", fontTittle)));
//		 * 
//		 */
//		
//
//		for (LifeInsuranceDetailDto lifeInsuranceDetailDto2 : parentDTOs.getGetLifeInsuranceDetailDto()) {
//
//			// table.addCell(new PdfPCell(new Phrase(lifeInsuranceDetailDto2.getLicid(),
//			// fontTittle)));
//			/*
//			 * table.addCell(new PdfPCell(new Phrase(lifeInsuranceDetailDto2.getLicid(),
//			 * fontTittle))); table.addCell(new PdfPCell(new
//			 * Phrase(lifeInsuranceDetailDto2.getEmpNo(), fontTittle))); table.addCell(new
//			 * PdfPCell(new Phrase(lifeInsuranceDetailDto2.getName(), fontTittle)));
//			 * table.addCell(new PdfPCell(new Phrase(lifeInsuranceDetailDto2.getAmount(),
//			 * fontTittle))); table.addCell(new PdfPCell(new
//			 * Phrase(lifeInsuranceDetailDto2.getDate(), fontTittle))); table.addCell(new
//			 * PdfPCell(new Phrase(lifeInsuranceDetailDto2.getRefundOfPremium(),
//			 * fontTittle)));
//			 */
//
//			table.addCell(new Phrase(lifeInsuranceDetailDto2.getLicid(), fontTittle));
//			table.addCell(new Phrase(lifeInsuranceDetailDto2.getEmpNo(), fontTittle));
//			table.addCell(new Phrase(lifeInsuranceDetailDto2.getName(), fontTittle));
//			table.addCell(new Phrase(lifeInsuranceDetailDto2.getAmount(), fontTittle));
//			table.addCell(new Phrase(lifeInsuranceDetailDto2.getDate(), fontTittle));
//			table.addCell(new Phrase(lifeInsuranceDetailDto2.getRefundOfPremium(), fontTittle));
//
//		}
//
//		document.add(superAni);
//
//		document.add(superAni2);
//
//		document.add(new Paragraph(" "));
//
//		document.add(superAni3);
//
//		document.add(superAni4);
//		document.add(new Paragraph(" "));
//
//		document.add(superAni5);
//		document.add(new Paragraph(" "));
//
//		document.add(superAni6);
//
//		document.add(superAni7);
//
//		document.add(new Paragraph(" "));
//		document.add(table);
//
//		document.add(new Paragraph(" "));
//		document.add(new Paragraph(" "));
//		document.add(new Paragraph(" "));
//		document.add(new Paragraph(" "));
//		document.add(new Paragraph(" "));
//		document.add(new Paragraph(" "));
//		document.add(superAni8);
//		document.add(new Paragraph(" "));
//		document.add(superAni9);
//		document.add(superAni11);
//		document.add(new Paragraph(" "));
//		document.add(superAni10);
//
//		document.close();
//		byte[] bytes = baos.toByteArray();
//
//
//	return bytes;
//}
//}
