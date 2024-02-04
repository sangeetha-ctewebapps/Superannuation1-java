package com.lic.epgs.payout.PdfServiceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.util.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.fasterxml.jackson.databind.JsonNode;
import com.lic.epgs.adjustmentcontribution.entity.AdjustmentContributionEntity;
import com.lic.epgs.adjustmentcontribution.repository.AdjustmentContributionRepository;
import com.lic.epgs.claim.constants.ClaimConstants;
import com.lic.epgs.common.integration.dto.AnnuityModeResponse;
import com.lic.epgs.common.integration.dto.AnnuityOption;
import com.lic.epgs.common.integration.dto.AnnuityOptionalResponse;
import com.lic.epgs.common.integration.dto.Annuitymode;
import com.lic.epgs.common.integration.service.IntegrationService;
import com.lic.epgs.integration.dto.ResponseDto;
import com.lic.epgs.integration.service.AccountingIntegrationService;
import com.lic.epgs.payout.PdfDto.AdjustmentVoucherDetailDto;
import com.lic.epgs.payout.PdfDto.ClaimForwardingLetterDto;
import com.lic.epgs.payout.PdfDto.GlCodesResponseDto;
import com.lic.epgs.payout.PdfDto.LifeCoverDetailsDto;
import com.lic.epgs.payout.PdfDto.LifeInsuranceDetailDto;
import com.lic.epgs.payout.PdfDto.NomineeDetails;
import com.lic.epgs.payout.PdfDto.PremiumAdjustmentVoucherDto;
import com.lic.epgs.payout.PdfDto.SpPaymentPdfDto;
import com.lic.epgs.payout.PdfDto.SpPdfDto;
import com.lic.epgs.payout.PdfDto.SupplementaryAdjustmentDto;
import com.lic.epgs.payout.PdfService.PayoutPdfService;
import com.lic.epgs.payout.constants.PayoutStatus;
import com.lic.epgs.payout.entity.PayoutAnnuityCalcEntity;
import com.lic.epgs.payout.entity.PayoutCommutationCalcEntity;
import com.lic.epgs.payout.entity.PayoutEntity;
import com.lic.epgs.payout.entity.PayoutFundValueEntity;
import com.lic.epgs.payout.entity.PayoutMbrAddressEntity;
import com.lic.epgs.payout.entity.PayoutMbrBankDetailEntity;
import com.lic.epgs.payout.entity.PayoutMbrEntity;
import com.lic.epgs.payout.entity.PayoutMbrNomineeEntity;
import com.lic.epgs.payout.entity.PayoutPayeeBankDetailsEntity;
import com.lic.epgs.payout.repository.PayoutRepository;
import com.lic.epgs.policy.entity.MphAddressEntity;
import com.lic.epgs.policy.entity.MphMasterEntity;
import com.lic.epgs.policy.entity.PolicyContributionEntity;
import com.lic.epgs.policy.entity.PolicyDepositEntity;
import com.lic.epgs.policy.entity.PolicyFrequencyDetailsEntity;
import com.lic.epgs.policy.entity.PolicyMasterEntity;
import com.lic.epgs.policy.repository.MphAddressRepository;
import com.lic.epgs.policy.repository.MphMasterRepository;
import com.lic.epgs.policy.repository.PolicyContributionRepository;
import com.lic.epgs.policy.repository.PolicyContributionSummaryRepository;
import com.lic.epgs.policy.repository.PolicyDepositRepository;
import com.lic.epgs.policy.repository.PolicyFrequencyRepository;
import com.lic.epgs.policy.repository.PolicyMasterRepository;
import com.lic.epgs.regularadjustmentcontribution.entity.RegularAdjustmentContributionEntity;
import com.lic.epgs.regularadjustmentcontribution.repository.RegularAdjustmentContributionRepository;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.DateUtils;
import com.lic.epgs.utils.NumericUtils;
import com.lowagie.text.DocumentException;
import com.lowagie.text.html.HtmlEncoder;
import com.lic.epgs.claim.entity.ClaimEntity;
import com.lic.epgs.claim.entity.ProcSaStoredProcedureResponseEntity;
import com.lic.epgs.claim.repository.ClaimRepository;
import com.lic.epgs.claim.repository.ProcSaStoredProcedureResponseRepository;
import com.lic.epgs.policy.dto.PolicyContributionDto;
import com.lic.epgs.policy.dto.PolicyDepositDto;



@Service
public class PayoutPdfServiceImpl implements PayoutPdfService {

	protected final Logger logger = LogManager.getLogger(getClass());
	@Autowired
	private PayoutRepository payoutRepository;

	@Autowired
	IntegrationService integrationService;

	@Autowired
	PolicyMasterRepository policyMasterRepository;

	@Autowired
	AdjustmentContributionRepository adjustmentContributionRepository;

	@Autowired
	RegularAdjustmentContributionRepository regularAdjustmentContributionRepository;

	@Autowired
	PolicyDepositRepository policyDepositRepository;

	@Autowired
	PolicyContributionRepository policyContributionRepository;
	
	@Autowired 
	PolicyFrequencyRepository policyFrequencyRepository;

	@Autowired
	PolicyContributionSummaryRepository policyContributionSummaryRepository;

	@Autowired
	private MphMasterRepository mphMasterRepository;

	@Autowired
	private MphAddressRepository mphAddressRepository;

	@Autowired
	AccountingIntegrationService accountingIntegrationService;
	@Autowired
	ModelMapper modelMapper;

	@Value("${temp.pdf.location}")
	private String tempPdfLocation;

	@Autowired
	ProcSaStoredProcedureResponseRepository procSaStoredProcedureResponseRepository;
	
	@Autowired
	ClaimRepository claimRepository;
	
	@Override
	@Transactional
	public String generateReport(String referenceNo, String reportType, String forwardTo) throws IOException {
		InputStream islicLogo = new ClassPathResource("liclogo.png").getInputStream();
		// Uncomment below line to add watermark to the pdf
		// InputStream islicWatermark = new
		// ClassPathResource("watermark.png").getInputStream();
		byte[] bytesLogo = IOUtils.toByteArray(islicLogo);
		// Uncomment below line to add watermark to the pdf
		// byte[] bytesWatermark = IOUtils.toByteArray(islicWatermark);

		String licLogo = Base64.getEncoder().encodeToString(bytesLogo);
		// Uncomment below line to add watermark to the pdf
		// String licWatermark = Base64.getEncoder().encodeToString(bytesWatermark);

		boolean showLogo = true;
		String reportBody = "";
		String reportStyles = "";
		String htmlContent2 = "</div></body></html>"; // DO NOT DISTRUB THIS LINE
		if (reportType.equalsIgnoreCase("claimForwardingLetter")) {
			logger.info("claimForwardingLetter ------generateReport---Start");
			reportBody = claimForwardingLetterContent(referenceNo) + htmlContent2;
			showLogo = false;
		} else if (reportType.equalsIgnoreCase("dcFundStatementForwardingLetter")) {
			reportBody = dcFundStatementForwardingLetterContent(referenceNo) + htmlContent2;
			showLogo = false;
		} else if (reportType.equalsIgnoreCase("payoutVoucherWithdrawal")
				|| reportType.equalsIgnoreCase("payoutVoucherMaturity")
				|| reportType.equalsIgnoreCase("payoutVoucherDeath")) {
			reportBody = payoutVoucher(referenceNo, reportType) + htmlContent2;
			reportStyles = payoutVoucherStyles();
			showLogo = false;
		} else if (reportType.equalsIgnoreCase("regularAdjustment")
				|| reportType.equalsIgnoreCase("subsequentAdjustment") || reportType.equalsIgnoreCase("NB")) {
			reportBody = adjustmentVoucher(referenceNo, reportType) + htmlContent2;
			reportStyles = adjustmentVoucherStyles();
			showLogo = false;
		} else if (reportType.equalsIgnoreCase("withdrawalLetter")) {
			reportBody = withdrawalLetter(referenceNo, reportType, forwardTo) + htmlContent2;
			reportStyles = withdrawalLetterStyles();
			showLogo = false;
		}
		String completehtmlContent = "<!DOCTYPE  html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><title>LIC ePGS</title><meta name=\"author\" content=\"LIC PNGS\"/><meta name=\"keywords\" content=\"Customer Communication\"/><meta name=\"description\" content=\"Report/Letter/Contract\"/><style type=\"text/css\"> body{border-width:0px;"
				+ "border-style:solid;\r\n" + "border-color:#586ec5;} * {margin:0; padding:0; text-indent:0; }"
				+ " .s1 { color: #2E5396; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 16pt; }"
				+ " .s2 { color: #2E3599; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " p { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 10pt; margin:0pt; }"
				+ " .a { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " .s3 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " .s5 { color: #2E3599; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ " .s6 { color: #120000; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ " .s7 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " .s8 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; vertical-align: -2pt; }"
				+ " .s9 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " h1 { color: #120000; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ " .s10 { color: #2D3499; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ " .pt10 { padding-top: 10pt; }" + " .pb10 { padding-bottom: 10pt; }"
				+ " .pb50 { padding-bottom: 50pt; }"
				+ " table, tbody, td {vertical-align: top; overflow: visible; color: black; font-family:\\\"Times New Roman\\\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 10pt; margin:0pt; } "
				// Uncomment below line to add watermark to the pdf
				// + " .watermark {\r\n background-image: url(\"data:image/png;base64," +
				// licWatermark + "\");} "
				+ " " + reportStyles + " " + " </style></head><body><div id=\"bg\" class=\"watermark\">"
				+ "<p style=\"padding-left: 5pt;text-indent: 0pt;text-align: left;\"/>";

		if (showLogo) {
			completehtmlContent = completehtmlContent
					+ "<p style=\"text-indent: 0pt;text-align: center;\"><span><table style=\"width:100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr style=\"display:flex;justify-content:space-between;align-items:center\"><td style=\"padding-left:24pt\"><img width=\"100\" height=\"63\" src=\"data:image/jpg;base64,"
					+ licLogo + "\"/></td>" + "</tr></table></span></p>";
		}
		completehtmlContent = completehtmlContent + "<p style=\"text-indent: 0pt;text-align: left;\"><br/></p>"
				+ reportBody + "";

		String htmlFileLoc = tempPdfLocation + "/" + reportType + referenceNo + ".html";

		System.out.println(htmlFileLoc);
		FileWriter fw = new FileWriter(htmlFileLoc);
		fw.write(completehtmlContent);
		fw.close();

		FileOutputStream fileOutputStream = new FileOutputStream(
				tempPdfLocation + "/" + reportType + referenceNo + ".pdf");
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(new File(htmlFileLoc));
		renderer.layout();
		try {
			renderer.createPDF(fileOutputStream, false);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		renderer.finishPDF();

		if (new File(htmlFileLoc).exists()) {
			new File(htmlFileLoc).delete();
		}

		return tempPdfLocation + "/" + reportType + referenceNo + ".pdf";
	}

	private String dcFundStatementForwardingLetterContent(String payoutNo) {

		String reportBody = "<p style=\"text-align:center;\">LIFE INSURANCE CORPORATION OF INDIA</p>"
				+ "<p style=\"text-align:center;\">Pension and Group Schemes Dept</p>"
				+ "<p  class=\"pb10\" style=\"text-align:center;\">&lt;Address of Unit&gt;</p>"
				+ "<p>&lt;TRUSTEES, S H KELKAR &amp; CO. LTD. EMPL. SUPN. FUND&gt;</p>"
				+ "<p>&lt;DEWKARAN MANSION&gt;</p>" + "<p>&lt;36, MANGALDAS ROAD&gt;</p>"
				+ "<p>&lt;MUMBAI- 400002&gt;</p>" + "<p class=\"pb10\">&lt;400002&gt;</p>"
				+ "<p class=\"pb10\">Dear Sir/Madam,</p>" + "<p class=\"pb10\">Re: Master Policy No. GSCA /NGSCA /"
				+ "___________" + "  -   Certificate of Balance in Fund As On &lt;31/03/2022&gt;</p>"
				+ "<p class=\"pb10\">We have pleasure in informing you that the interest Rates for the year have been declared and for the year &lt;2021-2022&gt; the interest rate applicable for your fund is &gt;7.35%&lt;.</p>"
				+ "<p class=\"pb10\">The Balance in your Running Account as on &lt;31/03/2022&gt; is as under:</p>"
				+ "<p>Opening Balance As On &lt;01.04.2021&gt;: __________</p>"
				+ "<p>LESS: Opening balance as on 01/04/2021 for exit cases: ___________</p>"
				+ "<p>(details in Annexure I)</p>" + "<p>Net Opening balance as on 01/04/2021: __________</p>"
				+ "<p>Purification On: ___________</p>" + "<p>Purification Off: __________</p>"
				+ "<p class=\"pb10\">Revised Opening Balance: __________</p>"
				+ "<p>Total contribution received during the year [ Ann-II ]: __________</p>"
				+ "<p>ADD: Equitable interest received  during the year [Ann III]: __________</p>"
				+ "<p class=\"pb10\">LESS: Contribution for exit cases [ Ann-IV]: __________</p>"
				+ "<p class=\"pb10\">NET Contribution during the year: __________</p>"
				+ "<p>ADD: Interest Credited for the</p>"
				+ "<p class=\"pb10\">Year for existing member for the policy as a Whole: __________</p>"
				+ "<p class=\"pb10\">Closing Balance As On &lt;31/03/2022&gt;: __________</p>"
				+ "<p class=\"pb10\">Any discrepancy in the statement may be brought to our notice immediately</p>"
				+ "<p class=\"pb10\">Thanking You,</p>" + "<p class=\"pb50\">Yours faithfully</p>"
				+ "<p class=\"pb10\">Manager (P&amp;GS)</p>"
				+ "<p>Encl : Annexures I to IV and member wise statement</p>";

		return reportBody.replace("\\", "").replaceAll("\t", "");
	}

	private String claimForwardingLetterContent(String payoutNo)
			throws MalformedURLException, ProtocolException, IOException {
		ClaimForwardingLetterDto claimForwardingLetterDto = generateClaimForwardingLetter(payoutNo);

		String reportBody = "<p>LIFE INSURANCE CORPORATION OF INDIA</p>" + "<p>@PROCESSINGUNITNAME Unit</p>"
				+ "<p>@PROCESSINGUNITADDRESSLINE1</p>" + "<p>@PROCESSINGUNITTELEPHONEANDFAX</p>"
				+ "<p class=\"pb10\">@PROCESSINGUNITEMAIL</p>" + "<hr/>" + "<p class=\"pt10\">@MEMBERNAME</p>"
				+ "<p>@MEMBERADDRESSLINE1</p>" + "<p>@MEMBERADDRESSLINE2</p>" + "<p>@MEMBERADDRESSLINE3</p>"
				+ "<p class=\"pb10\">@MEMBERSTATE</p>" + "<p class=\"pb10\">Dear Sir/Madam,</p>"
				+ "<p class=\"pb10\">Re: Master Policy No GSCA / @POLICYNO</p>"
				+ "<table style=\"padding-bottom:10pt;\">"
				+ "<tr><td>Mode of Exit: @MODEOFEXIST 	&nbsp;</td><td>  Name of Member:  @NAMEOFMEMBER</td></tr>"
				+ "</table>" + "<hr/>"
				+ "<p class=\"pt10 pb10\">We have pleasure in informing you that we have commenced payment of Annuity, due to you Vide Annuity No &nbsp; : &nbsp; @ANNUITYNUMBER</p>"
				+ "<p class=\"pb10\">Please Note to mention the above mentioned Annuity No in all future correspondence.</p>"
				+ "<table style=\"padding-bottom:10pt;\">";
		
		if (!claimForwardingLetterDto.getNomineeList().isEmpty()
				&& claimForwardingLetterDto.getModeOfExit() == ClaimConstants.DEATH) {
			for (NomineeDetails nomineeDetails : claimForwardingLetterDto.getNomineeList()) {
				reportBody += 
						"<table  border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
						
							+ "<tr style=\"line-height:22px;\">"
							+ "<td >&nbsp;Nominee Name &nbsp;&nbsp; </td>"
							+ "<td colspan=\"5\">&nbsp; " + nomineeDetails.getNomineeName() + "  &nbsp;&nbsp;</td>"
							+ "</tr>"
							
							+ "<tr style=\"line-height:22px;\" >"
							+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;First Instalment due Date</td> " 
							+ "<td >&nbsp;  @ANNUITYINSTALLMENTDATE  &nbsp;&nbsp;</td>"
							+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;Annuity Amount</td> "
							+ "<td >&nbsp; @ANNUITYAMOUNT  &nbsp;&nbsp;</td>"
							+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;Mode of Annuity</td> "
							+ "<td >&nbsp; @MODEOFANNUITY  &nbsp;&nbsp;</td>"
							+ "</tr>"

							+ "<tr style=\"line-height:22px;\" >"
							+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;Purchase Price &nbsp;&nbsp; </td> "
							+ "<td >&nbsp;  @PURCHASEPRICE  &nbsp;&nbsp;</td>"
							+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;IFSC Code &nbsp;&nbsp; </td>"
							+ "<td >&nbsp;" + nomineeDetails.getIfscCode() + "  &nbsp;&nbsp; </td>"
							+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;Date of Birth &nbsp;&nbsp; </td> "
							+ "<td >&nbsp; " + nomineeDetails.getDob() + " &nbsp;&nbsp;</td>"
							+ "</tr>"
							
							+ "<tr style=\"line-height:22px;\"  >"
							+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;Commutation Amount &nbsp;&nbsp; </td>"
							+ "<td colspan=\"2\" >&nbsp; " + nomineeDetails.getCommutationAmount() + " &nbsp;&nbsp; </td>"
							+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;Type of Annuity </td> " 
							+ "<td  colspan=\"2\"  >&nbsp; " +  nomineeDetails.getNomineeName() + " &nbsp;&nbsp;</td>"
							+ "</tr>"				


							+ "<tr style=\"line-height:22px;\" >"
							+ "<td  colspan=\"4\"  style=\"background-color:#D3D3D3;\" >&nbsp;Income Tax Deducted from commuted value Paid &nbsp;&nbsp; </td>"
							+ "<td  colspan=\"2\"  >&nbsp; " + nomineeDetails.getTdsAmount() + " &nbsp;&nbsp; </td>"
 							+ "</tr>"
							+ "</table>";
						
//						"<table style=\"border: 1px solid black;\" >"
//						+ "<tbody style=\"border: 1px solid black;\" >"
//						
//						+ "<tr style=\"border: 1px solid black;\">"
//						+ "<td style=\"border: 1px solid black;\">&nbsp;Nominee Name &nbsp;&nbsp; </td>"
//					    + "<td style=\"border: 1px solid black;\">&nbsp; " + nomineeDetails.getNomineeName() + " &nbsp;&nbsp;</td>"
//						+ "</tr>"
//					    
//						+ "<tr style=\"border: 1px solid black;\" >"
//						+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;Type of Annuity &nbsp;&nbsp; </td> " 
//						+ "<td style=\"border: 1px solid black;\">&nbsp; " +  nomineeDetails.getNomineeName() + " &nbsp;&nbsp;</td>"
//						+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;Date of Birth &nbsp;&nbsp; </td> "
//						+ "<td  style=\"border: 1px solid black;\">&nbsp; " + nomineeDetails.getDob() + " &nbsp;&nbsp;</td>"
//						+ "</tr>"
//						
//						+ "<tr style=\"border: 1px solid black;\" >"
//						+ "<td style=\"background-color:#D3D3D3;\">&nbsp; Amount payable to annuitants will be decided at the time of claim &nbsp;&nbsp; </td>"
//						+ "<td style=\"border: 1px solid black;\">&nbsp;  &nbsp;&nbsp; </td>"
//						+ "<td style=\"background-color:#D3D3D3;\" >&nbsp; IFSC Code &nbsp;&nbsp; </td>"
//						+ "<td style=\"border: 1px solid black;\">&nbsp;" + nomineeDetails.getIfscCode() + "  &nbsp;&nbsp; </td>"
//						+ "</tr>"
//						
//						+ "<tr style=\"border: 1px solid black;\" >"
//	                    + "<td style=\"background-color:#D3D3D3;\" >&nbsp; Commutation Amount &nbsp;&nbsp; </td>"
//	                    + "<td style=\"border: 1px solid black;\">&nbsp; " + nomineeDetails.getCommutationAmount() + " &nbsp;&nbsp; </td>"
//	                    + "<td style=\"background-color:#D3D3D3;\" >&nbsp; Income Tax Deducted from commuted value Paid &nbsp;&nbsp; </td>"
//	                    + "<td style=\"border: 1px solid black;\">&nbsp; " + nomineeDetails.getTdsAmount() + " &nbsp;&nbsp; </td>"
//					
//						 + "</tr></tbody></table>";
				
//				reportBody += "<table style=\\\"border: 1px solid black;\\\">\\r\\n\"\r\n"
//						+ "					+ \"        <tbody>\\r\\n\"\r\n"
//						+ "					+ \"            <tr style=\\\"line-height:28px;\\\">\\r\\n\"\r\n"
//						+ "					+ \"                <td>&nbsp;Nominee Name &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"                <td colspan=\\\"5\\\">&nbsp; &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"            </tr>\\r\\n\"\r\n"
//						+ "					+ \"            <tr style=\\\"line-height:28px;\\\">\\r\\n\"\r\n"
//						+ "					+ \"                <td style=\\\"background-color:#D3D3D3;\\\">&nbsp;First Instalment due date &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"                <td>&nbsp; &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"                <td style=\\\"background-color:#D3D3D3;\\\">&nbsp;Annuity Amount &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"                <td>&nbsp; &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"                <td style=\\\"background-color:#D3D3D3;\\\">&nbsp;Mode of Annuity &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"                <td>&nbsp;Monthly &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"            </tr>\\r\\n\"\r\n"
//						+ "					+ \"            <tr style=\\\"line-height:28px;\\\">\\r\\n\"\r\n"
//						+ "					+ \"                <td style=\\\"background-color:#D3D3D3;\\\">&nbsp;Purchase Price &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"                <td>&nbsp; &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"                <td style=\\\"background-color:#D3D3D3;\\\">&nbsp;IFSC Code &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"                <td>&nbsp;\\\" + claimForwardingLetterDto.getIFSCCode() + \\\" &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"                <td style=\\\"background-color:#D3D3D3;\\\">&nbsp;Date of Birth &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"                <td>&nbsp;\\\" + claimForwardingLetterDto.getMemberDob() + \\\" &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"            </tr>\\r\\n\"\r\n"
//						+ "					+ \"            <tr style=\\\"line-height:28px;\\\">\\r\\n\"\r\n"
//						+ "					+ \"                <td style=\\\"background-color:#D3D3D3;\\\">&nbsp;Commutation Amount &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"                <td colspan=\\\"2\\\">&nbsp;\\\" + claimForwardingLetterDto.getSumOfCommutationAmount() + \\\" &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"                <td style=\\\"background-color:#D3D3D3;\\\">&nbsp;Type of Annuity &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"                <td colspan=\\\"2\\\">&nbsp;\\\" + claimForwardingLetterDto.getTypeOfAnnuity() + \\\" &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"            </tr>\\r\\n\"\r\n"
//						+ "					+ \"            <tr style=\\\"line-height:28px;\\\">\\r\\n\"\r\n"
//						+ "					+ \"                <td style=\\\"background-color:#D3D3D3;\\\" colspan=\\\"4\\\">&nbsp;Income Tax Deducted from Commuted Value paid\\r\\n\"\r\n"
//						+ "					+ \"                    &nbsp;&nbsp;\\r\\n\"\r\n"
//						+ "					+ \"                </td>\\r\\n\"\r\n"
//						+ "					+ \"                <td colspan=\\\"2\\\">&nbsp;\\\" + claimForwardingLetterDto.getSumOfTdsAmount() + \\\" &nbsp;&nbsp;</td>\\r\\n\"\r\n"
//						+ "					+ \"            </tr>\\r\\n\"\r\n"
//						+ "					+ \"        </tbody>\\r\\n\"\r\n"
//						+ "					+ \"    </table>";
				
				
			}
		}
//		else {
//			reportBody += "<table  style=\"border: 1px solid black;\"><tbody><tr><td>Nominee Name: " + " " + "&nbsp;</td><td>Type of Annuity: "
//					+ claimForwardingLetterDto.getTypeOfAnnuity() + "</td><td>Date of Birth: "
//					+ claimForwardingLetterDto.getMemberDob() + "</td></tr>" + "<tr><td colspan=\"3\">"
//					+ "<p>Amount payable to annuitants will be decided at the time of claim</p>" + "<p>IFSC Code "
//					+ claimForwardingLetterDto.getIFSCCode() + "</p>" + "<p>Commutation Amount: "
//					+ claimForwardingLetterDto.getSumOfCommutationAmount() + "</p>"
//					+ "<p class=\"pb10\">Income Tax Deducted from commuted value Paid: "
//					+ claimForwardingLetterDto.getSumOfTdsAmount() + "</p>" + "</td></tr></tbody></table>";
//		}
		else {
			reportBody += "<table  border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
			
							+ "<tr style=\"line-height:22px;\">"
							+ "<td >&nbsp;Nominee Name &nbsp;&nbsp; </td>"
							+ "<td colspan=\"5\">&nbsp; " + " " + "  &nbsp;&nbsp;</td>"
							+ "</tr>"
							
							+ "<tr style=\"line-height:22px;\" >"
							+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;First Instalment due Date</td> " 
							+ "<td >&nbsp; @ANNUITYINSTALLMENTDATE  &nbsp;&nbsp;</td>"
							+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;Annuity Amount</td> "
							+ "<td >&nbsp; @ANNUITYAMOUNT  &nbsp;&nbsp;</td>"
							+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;Mode of Annuity</td> "
							+ "<td >&nbsp; @MODEOFANNUITY  &nbsp;&nbsp;</td>"
							+ "</tr>"

							+ "<tr style=\"line-height:22px;\" >"
							+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;Purchase Price &nbsp;&nbsp; </td> "
							+ "<td >&nbsp;  @PURCHASEPRICE  &nbsp;&nbsp;</td>"
							+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;IFSC Code &nbsp;&nbsp; </td>"
							+ "<td >&nbsp;" + claimForwardingLetterDto.getIFSCCode()  + "  &nbsp;&nbsp; </td>"
							+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;Date of Birth &nbsp;&nbsp; </td> "
							+ "<td >&nbsp; " + claimForwardingLetterDto.getMemberDob() + " &nbsp;&nbsp;</td>"
							+ "</tr>"
							
							+ "<tr style=\"line-height:22px;\"  >"
							+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;Commutation Amount &nbsp;&nbsp; </td>"
							+ "<td colspan=\"2\" >&nbsp; " + claimForwardingLetterDto.getSumOfCommutationAmount() + " &nbsp;&nbsp; </td>"
							+ "<td style=\"background-color:#D3D3D3;\" >&nbsp;Type of Annuity </td> " 
							+ "<td  colspan=\"2\"  >&nbsp; " +  claimForwardingLetterDto.getTypeOfAnnuity()+ " &nbsp;&nbsp;</td>"
							+ "</tr>"				


							+ "<tr style=\"line-height:22px;\" >"
							+ "<td  colspan=\"4\"  style=\"background-color:#D3D3D3;\" >&nbsp;Income Tax Deducted from commuted value Paid &nbsp;&nbsp; </td>"
							+ "<td  colspan=\"2\"  >&nbsp; " + claimForwardingLetterDto.getSumOfTdsAmount() + " &nbsp;&nbsp; </td>"
 							+ "</tr>"
							+ "</table>";
		}

		reportBody += "</table><table style=\"padding-bottom:10pt;\">"
				+ "<tr><td>Dated: @DATEOFNEFTCHEQUE for Rs @COMMUTATIONAMOUNT</td></tr>"
				+ "<tr><td>Favoring: @MEMBERNAME</td><td></td></tr>" + "</table>"
				+ "<p class=\"pb50\" style=\"text-align: right;\">Yours faithfully</p>"
				+ "<p style=\"text-align: right;\">Manager (P&amp;GS).</p>"
				// + "<p style=\"text-align: right;\">@AUTHORISEDPERSONNAME</p>"
				+ "<p>Cc to: @MPHNAME</p>" + "<p>@TRUSTEENAME</p>" + "<p>@TRUSTEEADDRESSLINE1</p>"
				+ "<p>@TRUSTEEADDRESSLINE2</p>" + "<p>@TRUSTEEADDRESSLINE3</p>" + "<p>@TRUSTEEADDRESSLINE4</p>";

		System.out.println(claimForwardingLetterDto.toString());
		System.out.println("memberAddress1:-" + claimForwardingLetterDto.getMemberAddressLine1());
		System.out.println("memberAddress2:-" + claimForwardingLetterDto.getMemberAddressLine2());
		System.out.println("memberAddress3:-" + claimForwardingLetterDto.getMemberAddressLine3());
		String completehtmlContent1 = reportBody
				.replaceAll("@PROCESSINGUNITADDRESSLINE1",
						claimForwardingLetterDto.getProcessingUnitAddressLine1() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getProcessingUnitAddressLine1())
								: "")
				.replaceAll("@PROCESSINGUNITTELEPHONEANDFAX",
						claimForwardingLetterDto.getProcessingUnitTelephoneandFax() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getProcessingUnitTelephoneandFax())
								: "")
				.replaceAll("@PROCESSINGUNITEMAIL",
						claimForwardingLetterDto.getProcessingUnitEmail() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getProcessingUnitEmail())
								: "")
				.replaceAll("@PROCESSINGUNITNAME",
						claimForwardingLetterDto.getProcessingUnitName() != null
								?HtmlEncoder.encode( claimForwardingLetterDto.getProcessingUnitName())
								: "P & GS")
				.replaceAll("@MEMBERNAME",
						claimForwardingLetterDto.getMemberName() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getMemberName())
								: "MEMBERNAME")
				.replaceAll("@MEMBERADDRESSLINE1",
						claimForwardingLetterDto.getMemberAddressLine1() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getMemberAddressLine1())
								: "")
				.replaceAll("@MEMBERADDRESSLINE2",
						claimForwardingLetterDto.getMemberAddressLine2() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getMemberAddressLine2())
								: "")
				.replaceAll("@MEMBERADDRESSLINE3",
						claimForwardingLetterDto.getMemberAddressLine3() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getMemberAddressLine3())
								: "")
				.replaceAll("@MEMBERSTATE",
						claimForwardingLetterDto.getMemberState() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getMemberState())
								: "")
				.replaceAll("@POLICYNO", HtmlEncoder.encode(claimForwardingLetterDto.getPolicyNo()))
				.replaceAll("@MODEOFEXIST", HtmlEncoder.encode(modeOfExist(claimForwardingLetterDto.getModeOfExit())))
				.replaceAll("@NAMEOFMEMBER",
						claimForwardingLetterDto.getNameOfMember() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getNameOfMember())
								: "NAMEOFMEMBER")
				.replaceAll("@ANNUITYNUMBER",
						claimForwardingLetterDto.getAnnuityNumber() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getAnnuityNumber())
								: "")
				.replaceAll("@DUEDATE", "")
				.replaceAll("@ANNUITYAMOUNT",
						claimForwardingLetterDto.getSumOfAnnuityNetAmount() > 0d ? NumericUtils
								.convertDoubleToString(claimForwardingLetterDto.getSumOfAnnuityNetAmount()) : "-")
				.replaceAll("@MODEOFANNUITY",
						claimForwardingLetterDto.getModeOfAnnuity() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getModeOfAnnuity())
								: "MODEOFANNUITY")
				.replaceAll("@PURCHASEPRICE",
						claimForwardingLetterDto.getSumOfAnnuityPurchasePrice() > 0d ? NumericUtils
								.convertDoubleToString(claimForwardingLetterDto.getSumOfAnnuityPurchasePrice())
								: "PURCHASEPRICE")
				.replaceAll("@NOMINEENAME",
						claimForwardingLetterDto.getNameOfNominee() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getNameOfNominee())
								: "NOMINEENAME")
				.replaceAll("@TYPEOFANNUITY",
						claimForwardingLetterDto.getTypeOfAnnuity() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getTypeOfAnnuity())
								: "TYPEOFANNUITY")
				.replaceAll("@NOMINEEDOB",
						claimForwardingLetterDto.getDobOfNominee() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getDobOfNominee())
								: "NOMINEEDOB")
				.replaceAll("@IFSCCODE",
						claimForwardingLetterDto.getIFSCCode() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getIFSCCode())
								: "")
				.replaceAll("@COMMUTATIONAMOUNT",
						claimForwardingLetterDto.getSumOfCommutationAmount() > 0d
								? HtmlEncoder.encode(NumericUtils
										.convertDoubleToString(claimForwardingLetterDto.getSumOfCommutationAmount()))
								: "COMMUTATIONAMOUNT")
				.replaceAll("@TDSONCOMMUTATIONAMOUNT", claimForwardingLetterDto.getCommutationAmount2() > 0d
						? HtmlEncoder.encode(
								NumericUtils.convertDoubleToString(claimForwardingLetterDto.getCommutationAmount2()))
						: "TDSONCOMMUTATIONAMOUNT")
				.replaceAll("@DATEOFNEFTCHEQUE",
						claimForwardingLetterDto.getDateOfNEFTandCheque() != null
								? claimForwardingLetterDto.getDateOfNEFTandCheque()
								: "DATEOFNEFTCHEQUE")
				.replaceAll("@AUTHORISEDPERSONNAME",
						claimForwardingLetterDto.getFavouring() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getFavouring())
								: "AUTHORISEDPERSONNAME")
				.replaceAll("@MPHNAME",
						claimForwardingLetterDto.getTrusteeAddressLine1() != null
						? HtmlEncoder.encode(claimForwardingLetterDto.getMemberAddressLine1())
				            	: "MPHNAME")
				.replaceAll("@TRUSTEENAME",
						claimForwardingLetterDto.getTrusteeName() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getTrusteeName())
								: "TRUSTEENAME")
				.replaceAll("@TRUSTEEADDRESSLINE1",
						claimForwardingLetterDto.getTrusteeAddressLine1() != ""
								? HtmlEncoder.encode(claimForwardingLetterDto.getTrusteeAddressLine1())
								: "")
				.replaceAll("@TRUSTEEADDRESSLINE2",
						claimForwardingLetterDto.getTrusteeAddressLine2() != ""
								? HtmlEncoder.encode(claimForwardingLetterDto.getTrusteeAddressLine2())
								: "")
				.replaceAll("@TRUSTEEADDRESSLINE3",
						claimForwardingLetterDto.getTrusteeAddressLine3() != ""
								? HtmlEncoder.encode(claimForwardingLetterDto.getTrusteeAddressLine3())
								: "")
				.replaceAll("@ANNUITYINSTALLMENTDATE",
						claimForwardingLetterDto.getFirstAnnuityInstallmentDueDate() != null
								? HtmlEncoder.encode(claimForwardingLetterDto.getFirstAnnuityInstallmentDueDate())
								: "")
				.replaceAll("@TRUSTEEADDRESSLINE4", "");

		logger.info("claimForwardingLetter ------generateReport---end");

		return completehtmlContent1.replace("\\", "").replaceAll("\t", "");
	}

	private ClaimForwardingLetterDto generateClaimForwardingLetter(String payoutNo)
			throws MalformedURLException, ProtocolException, IOException {
		PayoutEntity payoutEntity = payoutRepository.findByPayoutNoAndStatusAndIsActiveTrue(payoutNo,
				PayoutStatus.APPROVE.val());
		PayoutMbrEntity payoutMbrEntity = payoutEntity.getPayoutMbr();

		PayoutMbrAddressEntity payoutMbrAddressEntity = !payoutMbrEntity.getPayoutMbrAddresses().isEmpty()
				? payoutMbrEntity.getPayoutMbrAddresses().get(0)
				: null;
		PayoutMbrNomineeEntity payoutMbrNomineeEntity = !payoutMbrEntity.getPayoutMbrNomineeDtls().isEmpty()
				? payoutMbrEntity.getPayoutMbrNomineeDtls().get(0)
				: null;
		PayoutCommutationCalcEntity payoutCommutationCalcEntity = !payoutMbrEntity.getPayoutCommutationCalc().isEmpty()
				? payoutMbrEntity.getPayoutCommutationCalc().get(0)
				: null;
		PayoutMbrBankDetailEntity payoutMbrBankDetailEntity = !payoutMbrEntity.getPayoutMbrBankDetails().isEmpty()
				? payoutMbrEntity.getPayoutMbrBankDetails().get(0)
				: null;
		PayoutAnnuityCalcEntity payoutAnnuityCalcEntity = !payoutMbrEntity.getPayoutAnuityCalc().isEmpty()
				? payoutMbrEntity.getPayoutAnuityCalc().get(0)
				: null;
		String trustName = "";
		String trustAddressOne = "";
		String trustAddressTwo = "";
		String trustAddressThree = "";

		ClaimForwardingLetterDto claimForwardingLetterDto = new ClaimForwardingLetterDto();

		/*** Note:-getunitDetailsByuniyCode ***/
		ResponseDto responseDto = accountingIntegrationService
				.commonmasterserviceUnitByCode(payoutEntity.getUnitCode());
		if (responseDto != null) {
			claimForwardingLetterDto.setProcessingUnitAddressLine1(responseDto.getAddress1());
			claimForwardingLetterDto.setProcessingUnitEmail(responseDto.getEmailId());
			claimForwardingLetterDto.setProcessingUnitTelephoneandFax(responseDto.getTelephoneNo());
			claimForwardingLetterDto.setProcessingUnitName(responseDto.getCityName());
		} else {
			claimForwardingLetterDto.setProcessingUnitAddressLine1("No Address Details");
			claimForwardingLetterDto.setProcessingUnitEmail("");
			claimForwardingLetterDto.setProcessingUnitTelephoneandFax("");
			claimForwardingLetterDto.setProcessingUnitName("");
		}

		/*** Note:- get--PolicyDetails ***/
		Object policyMaster = policyMasterRepository.fetchPolicyDetaislBypolicyNumber(payoutEntity.getMasterPolicyNo());
		if (policyMaster != null) {
			Object[] obj = (Object[]) policyMaster;
			JsonNode proposalObject = integrationService.getProposalDetailsByProposalNumber(String.valueOf(obj[24]));
			claimForwardingLetterDto.setSchemeName(String.valueOf(obj[5]));
			JsonNode mphBasic = proposalObject.path("responseData").path("mphDetails").path("mphBasicDetails");
			JsonNode mphAddress = proposalObject.path("responseData").path("mphDetails").path("mphBasicDetails").path("mphAddressDetails").get(0);
			String trustId = mphBasic.path("mphCode").textValue();
			if (trustId != null && mphAddress != null) {
				trustName = mphBasic.path("proposerName").textValue();
				trustAddressOne = mphAddress.path("address1").textValue();
				trustAddressTwo = mphAddress.path("address2").textValue();
				trustAddressThree = mphAddress.path("address3").textValue();
			}
		}
		if (payoutEntity != null) {
			/*** Note:INCASE OF WITHDRAW ADDRESS SHOULD BE MPH **--Start */

			//if (payoutEntity.getModeOfExit() == 4) {
				if (policyMaster != null) {
					Object[] objPolicy = (Object[]) policyMaster;
					Object mphDetails = mphMasterRepository
							.findMphDetails(NumericUtils.stringToLong(String.valueOf(objPolicy[25])));
					if (mphDetails != null) {
						Object[] objMph = (Object[]) mphDetails;
						claimForwardingLetterDto
								.setMemberName(String.valueOf(objMph[14]) != null ? String.valueOf(objMph[14]) : "");
					}
					Set<MphAddressEntity> mphAddressList = mphAddressRepository.findAllByMphIdAndIsActiveAddress(
							NumericUtils.stringToLong(String.valueOf(objPolicy[25])), Boolean.TRUE);
					logger.info("MPHADDRESlIST WITH MPID:{}",mphAddressList);
					logger.info("String.valueOf(objPolicy[25]):{}",String.valueOf(objPolicy[25]));
					if (!mphAddressList.isEmpty()) {
						for (MphAddressEntity mphAddressOne : mphAddressList) {
							claimForwardingLetterDto.setMemberAddressLine1(
									mphAddressOne != null && mphAddressOne.getAddressLine1() != null
											? mphAddressOne.getAddressLine1()
											: "No Address Found");
							claimForwardingLetterDto.setMemberAddressLine2(
									mphAddressOne != null && mphAddressOne.getAddressLine2() != null
											? mphAddressOne.getAddressLine2()
											: "");
							claimForwardingLetterDto.setMemberAddressLine3(
									mphAddressOne != null && mphAddressOne.getAddressLine3() != null
											? mphAddressOne.getAddressLine3()
											: "");
							claimForwardingLetterDto
									.setMemberState(mphAddressOne != null && mphAddressOne.getStateName() != null
											? mphAddressOne.getStateName()
											: "");
						}
					} else {
						claimForwardingLetterDto.setMemberAddressLine1("No Address Found");
						claimForwardingLetterDto.setMemberAddressLine2("");
						claimForwardingLetterDto.setMemberAddressLine3("");
						claimForwardingLetterDto.setMemberState("");
					}
				}

			//}
			/*** Note:INCASE OF WITHDRAW ADDRESS SHOULD BE MPH **--end */
			 if (payoutEntity.getModeOfExit() == ClaimConstants.RESIGNATION
					|| payoutEntity.getModeOfExit() == ClaimConstants.RETRIERMENT) {
//				if(payoutCommutationCalcEntity.getAmtPayableTo()!=null) {
//				amountPayablToResignAndRetriment(payoutEntity.getMphCode(), claimForwardingLetterDto,
//						commutationPayable(NumericUtils.stringToInteger(payoutCommutationCalcEntity.getAmtPayableTo())),
//						payoutEntity, null);
//				}
//				else {
				amountPayablToResignAndRetriment(payoutEntity.getMphCode(), claimForwardingLetterDto,
						ClaimConstants.MEMBER, payoutEntity, null);
//				}

//			claimForwardingLetterDto
//					.setMemberName(payoutMbrEntity.getFirstName() != null ? payoutMbrEntity.getFirstName() : "");
//			claimForwardingLetterDto.setMemberAddressLine1(
//					payoutMbrAddressEntity != null && payoutMbrAddressEntity.getAddressLineOne() != null
//							? payoutMbrAddressEntity.getAddressLineOne()
//							: null);
//			claimForwardingLetterDto.setMemberAddressLine2(
//					payoutMbrAddressEntity != null && payoutMbrAddressEntity.getAddressLineTwo() != null
//							? payoutMbrAddressEntity.getAddressLineTwo()
//							: null);
//			claimForwardingLetterDto.setMemberAddressLine3(
//					payoutMbrAddressEntity != null && payoutMbrAddressEntity.getAddressLineThree() != null
//							? payoutMbrAddressEntity.getAddressLineThree()
//							: null);
//			claimForwardingLetterDto
//					.setMemberState(payoutMbrAddressEntity != null && payoutMbrAddressEntity.getState() != null
//							? payoutMbrAddressEntity.getState()
//							: "");
				if(payoutEntity.getPayoutMbr().getPayoutAnuityCalc().get(0)!=null) {
				claimForwardingLetterDto
				.setAnnuityNumber(payoutEntity.getPayoutMbr().getPayoutAnuityCalc().get(0).getAnnuityNumber()!=null?(payoutEntity.getPayoutMbr().getPayoutAnuityCalc().get(0).getAnnuityNumber()):"");
				claimForwardingLetterDto.setFirstAnnuityInstallmentDueDate
				(payoutEntity.getPayoutMbr().getPayoutAnuityCalc().get(0).getFirstAnnuityInstallmentDueDate()!=null?DateUtils.dateToHypenStringDDMMYYYY(payoutEntity.getPayoutMbr().getPayoutAnuityCalc().get(0).getFirstAnnuityInstallmentDueDate()):"");
				}
				} else {
				PayoutCommutationCalcEntity payoutCommucEntity = payoutMbrEntity.getPayoutCommutationCalc().get(0);
				PayoutAnnuityCalcEntity payoutAnnuityEntity = payoutMbrEntity.getPayoutAnuityCalc().get(0);
				if (payoutMbrNomineeEntity.getNomineeCode().equals(payoutCommucEntity.getNomineeCode())
						&& payoutMbrNomineeEntity.getClaimantType().equalsIgnoreCase(ClaimConstants.NOMINEE)) {
					claimForwardingLetterDto.setMemberName(payoutMbrNomineeEntity.getFirstName());
					claimForwardingLetterDto.setMemberAddressLine1(
							payoutMbrNomineeEntity != null && payoutMbrNomineeEntity.getAddressOne() != null
									? payoutMbrNomineeEntity.getAddressOne()
									: "No Address Found");
					claimForwardingLetterDto.setMemberAddressLine2(
							payoutMbrNomineeEntity != null && payoutMbrNomineeEntity.getAddressTwo() != null
									? payoutMbrNomineeEntity.getAddressTwo()
									: "");
					claimForwardingLetterDto.setMemberAddressLine3(
							payoutMbrNomineeEntity != null && payoutMbrNomineeEntity.getAddressThree() != null
									? payoutMbrNomineeEntity.getAddressThree()
									: "");
					claimForwardingLetterDto
							.setMemberState(payoutMbrNomineeEntity != null && payoutMbrNomineeEntity.getState() != null
									? payoutMbrNomineeEntity.getState()
									: "");

				}
				
				if (payoutAnnuityEntity.getNomineeCode().equals(payoutCommucEntity.getNomineeCode())) {
					claimForwardingLetterDto
					.setAnnuityNumber(payoutAnnuityEntity!=null?payoutAnnuityEntity.getAnnuityNumber():"");
					claimForwardingLetterDto.setFirstAnnuityInstallmentDueDate
					(payoutAnnuityEntity.getFirstAnnuityInstallmentDueDate()!=null?DateUtils.dateToHypenStringDDMMYYYY(payoutAnnuityEntity.getFirstAnnuityInstallmentDueDate()):"");
				}

			}
			/*** Note:INCASE OF Without WITHDRAW ADDRESS SHOULD BE MPH **--end */

			claimForwardingLetterDto
					.setPolicyNo(payoutEntity.getMasterPolicyNo() != null ? payoutEntity.getMasterPolicyNo() : "");
			claimForwardingLetterDto.setModeOfExit(payoutEntity.getModeOfExit());
			claimForwardingLetterDto
					.setNameOfMember(payoutMbrEntity.getFirstName() != null ? payoutMbrEntity.getFirstName() : "");
			claimForwardingLetterDto
					.setDueDate(payoutEntity.getMasterPolicyNo() != null ? payoutEntity.getMasterPolicyNo() : "");
			claimForwardingLetterDto.setAnnuityAmount(
					payoutAnnuityCalcEntity != null && payoutAnnuityCalcEntity.getNetPurchasePrice() != null
							? payoutAnnuityCalcEntity.getNetPurchasePrice()
							: 0d);
			// need to call modeOfAnuity
			claimForwardingLetterDto
					.setModeOfAnnuity(payoutAnnuityCalcEntity != null && payoutAnnuityCalcEntity.getAnuityMode() != null
							? getAnnuityMode(payoutAnnuityCalcEntity.getAnuityMode())
							: "");
			claimForwardingLetterDto.setPurchasePrice(
					payoutAnnuityCalcEntity != null && payoutAnnuityCalcEntity.getPurchasePrice() != null
							? payoutAnnuityCalcEntity.getPurchasePrice()
							: 0d);
			claimForwardingLetterDto
					.setNameOfNominee(payoutMbrNomineeEntity != null && payoutMbrNomineeEntity.getFirstName() != null
							? payoutMbrNomineeEntity.getFirstName()
							: "");
			// need to call modeOfAnuity
			claimForwardingLetterDto.setTypeOfAnnuity(
					payoutAnnuityCalcEntity != null && payoutAnnuityCalcEntity.getAnnuityOption() != null
							? getAnnuityoptional(payoutAnnuityCalcEntity.getAnnuityOption())
							: "");
			claimForwardingLetterDto
					.setIFSCCode(payoutMbrBankDetailEntity != null && payoutMbrBankDetailEntity.getIfscCode() != null
							? payoutMbrBankDetailEntity.getIfscCode()
							: "");
			claimForwardingLetterDto.setCommutationAmount1(
					payoutCommutationCalcEntity != null && payoutCommutationCalcEntity.getNetAmount() != null
							? NumericUtils.doubleRoundInMath(payoutCommutationCalcEntity.getNetAmount(), 0)
							: 0d);
			claimForwardingLetterDto.setTdsAppliedOnCommutation(payoutCommutationCalcEntity.getTdsApplicable());
			claimForwardingLetterDto.setNeftAndChequeNo("NEFT");
			claimForwardingLetterDto.setDateOfNEFTandCheque("");
			claimForwardingLetterDto.setCommutationAmount2(
					payoutCommutationCalcEntity != null && payoutCommutationCalcEntity.getCommutationAmt() != null
							? NumericUtils.doubleRoundInMath(payoutCommutationCalcEntity.getCommutationAmt(), 0)
							: 0d);
			claimForwardingLetterDto
					.setMemberName1(payoutEntity.getMasterPolicyNo() != null ? payoutEntity.getMasterPolicyNo() : "");
			claimForwardingLetterDto.setAuthorisedPersonName("");
			claimForwardingLetterDto.setMphName(payoutEntity.getMphName() != null ? payoutEntity.getMphName() : "");
			// need to call trustAddresss
			claimForwardingLetterDto.setTrusteeName(trustName);
			claimForwardingLetterDto.setTrusteeAddressLine1(trustAddressOne);
			claimForwardingLetterDto.setTrusteeAddressLine2(trustAddressTwo);
			claimForwardingLetterDto.setTrusteeAddressLine3(trustAddressThree);
			claimForwardingLetterDto
					.setDobOfNominee(payoutMbrNomineeEntity != null && payoutMbrNomineeEntity.getDob() != null
							? DateUtils.dateToStringDDMMYYYY(payoutMbrNomineeEntity.getDob())
							: "");
			if (payoutCommutationCalcEntity.getCommutationBy() != null) {
				claimForwardingLetterDto
						.setFavouring(commutationPayable(payoutCommutationCalcEntity.getCommutationBy()));
			}

//			claimForwardingLetterDto.setTrusteeName(trustName!=null?trustName:"TRUSTNAME");
//			claimForwardingLetterDto.setTrusteeAddressLine1(trustAddressOne!=null?trustAddressOne:"TRUSTADDRESSONE");
//			claimForwardingLetterDto.setTrusteeAddressLine2(trustAddressTwo!=null?trustAddressTwo:"TRUSTADDRESSTWO");
//			claimForwardingLetterDto.setTrusteeAddressLine3(trustAddressThree!=null?trustAddressThree:"TRUSTADDRESSTHREE");

			/*** Note:-Case Of Death ***/
			Double sumOfAnnuityPurchasePrice = 0d;
			Double sumOfAnnuityNetAmount = 0d;
			Double sumOfCommutationAmount = 0d;
			Double sumOfTdsAmount = 0d;
			Double nomineeCommutationAmount = 0d;
			Double nomineeTdsAmount = 0d;
			String ifsc = "";
			List<NomineeDetails> nomineeDetailList = new ArrayList();
			if (payoutEntity.getModeOfExit() == 1) {
				List<PayoutMbrNomineeEntity> payoutMbrNomineeEntityList = payoutMbrEntity.getPayoutMbrNomineeDtls();
				for (PayoutMbrNomineeEntity payoutMbrNomineeEnt : payoutMbrNomineeEntityList) {
					if (payoutMbrNomineeEnt.getClaimantType().equalsIgnoreCase(ClaimConstants.NOMINEE)) {
						NomineeDetails nomineeDetails = new NomineeDetails();
						nomineeDetails.setNomineeName("");
						nomineeDetails.setDob(DateUtils.dateToHypenStringDDMMYYYY(payoutMbrNomineeEnt.getDob()));
						ifsc = payoutMbrNomineeEnt.getIfscCode();
						nomineeDetails.setIfscCode(ifsc);
						for (PayoutCommutationCalcEntity commu : payoutMbrEntity.getPayoutCommutationCalc()) {
							if (payoutMbrNomineeEnt.getNomineeCode().equals(commu.getNomineeCode())) {
								nomineeCommutationAmount = commu.getNetAmount() != null
										? NumericUtils.doubleRoundInMath(commu.getNetAmount(), 0)
										: 0d;
								nomineeDetails.setCommutationAmount(nomineeCommutationAmount);
								nomineeTdsAmount = commu.getTdsAmount() != null ? commu.getTdsAmount() : 0d;
								nomineeDetails.setTdsAmount(nomineeTdsAmount);
								sumOfCommutationAmount += commu.getNetAmount();
								if (payoutCommutationCalcEntity.getCommutationBy() != null) {
									claimForwardingLetterDto.setFavouring(
											commutationPayable(payoutCommutationCalcEntity.getCommutationBy()));
								}
							}
						}
						nomineeDetailList.add(nomineeDetails);

						for (PayoutAnnuityCalcEntity annuity : payoutMbrEntity.getPayoutAnuityCalc()) {
							if (payoutMbrNomineeEnt.getNomineeCode().equals(annuity.getNomineeCode())) {
//							nomineeDetails.setTdsAmount(
//									annuity.getNetPurchasePrice() != null ? annuity.getNetPurchasePrice() : 0d);
								sumOfAnnuityPurchasePrice += annuity.getPurchasePrice();
								sumOfAnnuityNetAmount += annuity.getPension();
								nomineeDetails.setTypeOfAnnuity(getAnnuityoptional(annuity.getAnnuityOption()));

							}
						}
						amountPayablToResignAndRetriment(payoutEntity.getMphCode(), claimForwardingLetterDto,
								commutationPayable(NumericUtils.convertStringToInteger(
										payoutMbrEntity.getPayoutCommutationCalc().get(0).getAmtPayableTo())),
								payoutEntity, null);
						claimForwardingLetterDto
								.setSumOfAnnuityNetAmount(NumericUtils.doubleRoundInMath(sumOfAnnuityNetAmount, 0));
						claimForwardingLetterDto.setSumOfAnnuityPurchasePrice(
								NumericUtils.doubleRoundInMath(sumOfAnnuityPurchasePrice, 0));
						claimForwardingLetterDto
								.setSumOfCommutationAmount(NumericUtils.doubleRoundInMath(sumOfCommutationAmount, 0));
					}
				}

			} else {
				sumOfAnnuityNetAmount = (payoutAnnuityCalcEntity != null ? payoutAnnuityCalcEntity.getPension() : 0d);
				sumOfAnnuityPurchasePrice = (payoutAnnuityCalcEntity != null
						? payoutAnnuityCalcEntity.getPurchasePrice()
						: 0d);
				claimForwardingLetterDto.setIFSCCode(payoutMbrEntity.getPayoutPayeeBank().get(0).getIfscCode());
				sumOfCommutationAmount = payoutCommutationCalcEntity.getNetAmount();
				sumOfTdsAmount = payoutCommutationCalcEntity.getTdsAmount();
				claimForwardingLetterDto
						.setSumOfAnnuityNetAmount(NumericUtils.doubleRoundInMath(sumOfAnnuityNetAmount, 0));
				claimForwardingLetterDto
						.setSumOfAnnuityPurchasePrice(NumericUtils.doubleRoundInMath(sumOfAnnuityPurchasePrice, 0));
				claimForwardingLetterDto
						.setSumOfCommutationAmount(NumericUtils.doubleRoundInMath(sumOfCommutationAmount, 0));
				claimForwardingLetterDto.setSumOfTdsAmount(NumericUtils.doubleRoundInMath(sumOfTdsAmount, 0));
			}
			claimForwardingLetterDto
					.setMemberDob(DateUtils.dateToHypenStringDDMMYYYY(payoutMbrEntity.getDateOfBirth()));
			claimForwardingLetterDto.setNomineeList(nomineeDetailList);

		}
		claimForwardingLetterDto
				.setDateOfNEFTandCheque(DateUtils.dateToHypenStringDDMMYYYY(payoutEntity.getModifiedOn()));
		;
		return claimForwardingLetterDto;
	}

	/*** getMPHDetails ***/
	public ClaimForwardingLetterDto amountPayablToResignAndRetriment(String mphCode,
			ClaimForwardingLetterDto claimForwardingLetterDto, String amountPayableTo, PayoutEntity payoutEntity,
			SpPaymentPdfDto dto) {
		if (amountPayableTo.equalsIgnoreCase(ClaimConstants.MPH)) {
			logger.info("MphDetails--- fetch--start");
			List<Object> mphMasterEntity = mphMasterRepository.getByMphCodeAndIsActiveTrue(mphCode);
			logger.info("MphDetails--- fetch--end");
			if (mphMasterEntity != null) {
				Object obj = mphMasterEntity.get(0);
				Object[] objNew = (Object[]) obj;
				Set<MphAddressEntity> mphAddressList = mphAddressRepository
						.findAllByMphIdAndIsActive(NumericUtils.stringToLong(objNew[0].toString()), Boolean.TRUE);

				if (dto != null) {
					dto.setFavouring(objNew[1].toString());
				} else {
					claimForwardingLetterDto.setMemberName(objNew[1].toString());
				}

				if (mphAddressList != null && claimForwardingLetterDto != null) {
					for (MphAddressEntity mphAddressOne : mphAddressList) {
						claimForwardingLetterDto
								.setMemberAddressLine1(mphAddressOne != null && mphAddressOne.getAddressLine1() != null
										? mphAddressOne.getAddressLine1()
										: "No Address Found");
						claimForwardingLetterDto
								.setMemberAddressLine2(mphAddressOne != null && mphAddressOne.getAddressLine2() != null
										? mphAddressOne.getAddressLine2()
										: "");
						claimForwardingLetterDto
								.setMemberAddressLine3(mphAddressOne != null && mphAddressOne.getAddressLine3() != null
										? mphAddressOne.getAddressLine3()
										: "");
						claimForwardingLetterDto
								.setMemberState(mphAddressOne != null && mphAddressOne.getStateName() != null
										? mphAddressOne.getStateName()
										: "");

					}
				}
			}
		} else if (amountPayableTo.equalsIgnoreCase(ClaimConstants.MEMBER)
				&& payoutEntity.getModeOfExit() != ClaimConstants.DEATH) {
			PayoutMbrEntity payoutMbr = payoutEntity.getPayoutMbr();
			if (dto != null) {
				dto.setFavouring(payoutMbr.getFirstName());
			} else {
				claimForwardingLetterDto.setMemberName(payoutMbr.getFirstName());
			}
			if (!payoutMbr.getPayoutMbrAddresses().isEmpty()) {
				PayoutMbrAddressEntity payoutMbrAddressEntity = payoutMbr.getPayoutMbrAddresses().get(0);
				if (payoutMbrAddressEntity != null && claimForwardingLetterDto != null) {
					claimForwardingLetterDto.setMemberAddressLine1(
							payoutMbrAddressEntity != null && payoutMbrAddressEntity.getAddressLineOne() != null
									? payoutMbrAddressEntity.getAddressLineOne()
									: "No Address Found");
					claimForwardingLetterDto.setMemberAddressLine2(
							payoutMbrAddressEntity != null && payoutMbrAddressEntity.getAddressLineTwo() != null
									? payoutMbrAddressEntity.getAddressLineTwo()
									: "");
					claimForwardingLetterDto.setMemberAddressLine3(
							payoutMbrAddressEntity != null && payoutMbrAddressEntity.getAddressLineThree() != null
									? payoutMbrAddressEntity.getAddressLineThree()
									: "");
					claimForwardingLetterDto
							.setMemberState(payoutMbrAddressEntity != null && payoutMbrAddressEntity.getState() != null
									? payoutMbrAddressEntity.getState()
									: "");
				}
			}
		}

		return claimForwardingLetterDto;
	}

	/*
	 * **Note:-getAnnuityoptional **
	 */

	public String getAnnuityoptional(String input) {
		String response = "";
		AnnuityOptionalResponse optional = integrationService.getAnnuityOptionReponse();
		if (optional != null) {
			for (AnnuityOption annuityOptOne : optional.getAnnuityOption()) {
				if (NumericUtils.convertIntegerToString(annuityOptOne.getAnnuityOptionId()).equals(input)) {
					response = annuityOptOne.getAnnuityOptionDesc();
					return response;
				}
			}
		}
		return response;

	}

	/*
	 * **Note:-getAnnuityMode **
	 */

	public String getAnnuityMode(String input) {
		String response = "";
		AnnuityModeResponse optional = integrationService.getAnnuityModeReponse();
		if (optional != null) {
			for (Annuitymode annuitymodeOne : optional.getAnnuityMode()) {
				if (NumericUtils.convertIntegerToString(annuitymodeOne.getAnnuityModeId()).equals(input)) {
					response = annuitymodeOne.getDescription();
					return response;
				}
			}
		}
		return response;

	}

//	public CommonResponseDto<ProposalApiDto> getProposalDetailsByProposalNumber(String proposalNumber) {
//		String response = "";
//		CommonResponseDto<ProposalApiDto> optional = integrationService.getProposalDetailsByProposalNumber(proposalNumber);
//		return response;
//
//	}

	/*** Note:-- PaypoutVocher ***/

	private String payoutVoucher(String payoutNo, String reportType) throws IOException {
		InputStream islicLogo = new ClassPathResource("liclogo.png").getInputStream();
		byte[] bytesLogo = IOUtils.toByteArray(islicLogo);
		String licLogo = Base64.getEncoder().encodeToString(bytesLogo);
		String reportBody1 = "<table style=\"width:100%;\">"
				+ "<tr><td style=\"width:25%;\"></td><td style=\"width:50%;text-align:center;\">"
				+ "<img width=\"100\" height=\"63\" src=\"data:image/jpg;base64," + licLogo + "\"/></td>"
				+ "<td style=\"width:25%;vertical-align: bottom;\">"
				+ "<div style=\"padding:3pt;border:1pt;border-style:solid;border-color:#000;text-align:center;\">PAYMENT VOUCHER</div>"
				+ "</td></tr>" + "</table>" + "<table style=\"width:100%;\">"
				+ "<tr><td style=\"width:70%;\">P &amp; GS Dept. @UNITCODE</td><td style=\"width:30%;\">Voucher No. @VOUCHERNO</td></tr>"
				+ "<tr><td>Divl. Name: @UNITNAME</td><td>Date: @VOUCHERDATE</td></tr>" + "</table>"
				+ "<p class=\"pb10\" style=\\\"text-align:center;\\\">To Cash / Banking Section</p>"
				+ "<p class=\"pb10\">Please Pay favouring : @FAVOURING  &nbsp; &nbsp; an  Amount of Rs. @AMOUNTINWORDS  </p>"
				+ "<table style=\"width:100%;\">"
				+ "<tr><td style=\"width:50%;padding-bottom:10pt;\">Policy No.: @POLICYNUMBER &amp;  @SCHEMENAME</td><td style=\"width:50%;\">MPH Name: @MPHNAME</td></tr>"
				+ "</table>"
				+ "<table class=\"tableborder\" callpadding=\"0\" cellspacing=\"0\" style=\"width:100%;min-height:100pt;\">"
				+ "<tr><th>Head of Account</th><th>Code</th><th class=\"tdrightalign\">Debit Amount</th><th class=\"tdrightalign\">Credit Amount</th></tr>";

		double debitTotal = 0;
		double creditTotal = 0;
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		String detailTable = "";
		SpPaymentPdfDto spPaymentPdfDtoRes = getPayoutVocher(payoutNo, reportType);
		System.out.println(spPaymentPdfDtoRes.toString());
		for (AdjustmentVoucherDetailDto adjustmentVoucherDetailDto : spPaymentPdfDtoRes.getVoucherDetail()) {

			detailTable = detailTable + "<tr><td class=\"rightborder\">" + adjustmentVoucherDetailDto.getHeadOfAccount()
					+ "</td><td class=\"rightborder\">" + adjustmentVoucherDetailDto.getCode()
					+ "</td><td class=\"tdrightalign rightborder\">" + adjustmentVoucherDetailDto.getDebitAmount()
					+ "</td><td class=\"tdrightalign\">" + adjustmentVoucherDetailDto.getCreditAmount() + "</td></tr>";
			debitTotal += NumericUtils.convertStringToDouble(adjustmentVoucherDetailDto.getDebitAmount()); // FIXME
			creditTotal += NumericUtils.convertStringToDouble(adjustmentVoucherDetailDto.getCreditAmount()); // FIXME
		}
		// to adjust min height
		for (int i = 1; i <= 5; i++) {
			detailTable = detailTable
					+ "<tr><td class=\"rightborder\"></td><td class=\"rightborder\"></td><td class=\"tdrightalign rightborder\"></td><td class=\"tdrightalign\"></td></tr>";
		}
		detailTable = detailTable + "<tfoot><tr><td colspan=\"2\">Total</td><td class=\"tdrightalign\">"
				+ formatter.format(debitTotal) + "</td><td class=\"tdrightalign\">" + formatter.format(creditTotal)
				+ "</td></tr></tfoot>";

		detailTable = detailTable + "</table>" + "<p style=\"padding-top:10pt;\">As per the following details</p>";

		String summaryTable = "<table class=\"tableborder\" callpadding=\"0\" cellspacing=\"0\" style=\"width:100%;min-height:100pt;\">";
		// to adjust min height
		for (int i = 1; i <= 3; i++) {
			summaryTable = summaryTable + "<tr><td></td></tr>";
		}
		summaryTable = summaryTable
				+ "<tr><td><p>Voucher No: @VOUCHERNO</p><p>Voucher Date: @VOUCHERDATE</p><p>Prepared By: @PREPAREDBY</p></td></tr>";
		// to adjust min height
		for (int i = 1; i <= 3; i++) {
			summaryTable = summaryTable + "<tr><td></td></tr>";
		}
		summaryTable = summaryTable + "</table>";

		String footerTable = "<p class=\"pb10\">&nbsp;</p><table style=\"width:100%;\"><tr><td style=\"width:50%\">"
				+ "<p class=\"signaturepadding\">Prepared By: </p>" + "<p class=\"signaturepadding\">Checked By: </p>"
				+ "<p class=\"pb10\">Date: </p>" + "<p>Drawn on: </p>"
				+ "</td><td><table style=\"width:100%;\"><tr><td>"
				+ "<table class=\"tableborder\" callpadding=\"0\" cellspacing=\"0\" style=\"width:100%;min-height:100pt;\">"
				+ "<tr><th>" + "<p class=\"pb10\" style=\"text-align:center;\">PAY</p>" + "<p class=\"pb10\">Sign:</p>"
				+ "<p class=\"pb10\">Design:</p>" + "<p>Date:</p>" + "</th><th>"
				+ "<p class=\"pb10\" style=\"text-align:center;\">PAID</p>" + "<p class=\"signaturepadding\">Sign:</p>"
				+ "<p>Date:</p>" + "</th></tr>" + "<tr><th colspan=\"2\">"
				+ "<p class=\"pt10\" style=\"text-align:center;\">Initials of Officers Signing cheques</p>"
				+ "</th></tr>" + "</table></td></tr></table>" + "</td></tr>" + "</table>";

		String reportBody = reportBody1 + detailTable + summaryTable + footerTable;
//		ClaimForwardingLetterDto claimForwardingLetterDto = generateClaimForwardingLetter(payoutNo);
//
//		JsonNode response = integrationService
//				.getProductDetailsByProductId(NumericUtils.stringToLong(claimForwardingLetterDto.getSchemeName()));
//		if (response != null) {
//			JsonNode proposeDetails = response.path("responseData");
//			String product = proposeDetails.path("productCode").textValue();
//			claimForwardingLetterDto.setSchemeName(product);
//		}

		reportBody = reportBody.replaceAll("@PNGSDEPT", "PNGSDEPT").replaceAll("@DIVLNAME", "").replaceAll("@FAVOURING",
				spPaymentPdfDtoRes.getFavouring() != null ? HtmlEncoder.encode(spPaymentPdfDtoRes.getFavouring())
						: "MPHNAME")
				
				.replaceAll("@AMOUNTINWORDS", spPaymentPdfDtoRes.getAmountInWords())
				.replaceAll("@POLICYNUMBER", spPaymentPdfDtoRes.getPolicyNumber())
				.replaceAll("@SCHEMENAME",
						spPaymentPdfDtoRes.getSchemeName() != null
								? HtmlEncoder.encode(spPaymentPdfDtoRes.getSchemeName())
								: "SCHEMENAME")
				.replaceAll("@VOUCHERNO",
						spPaymentPdfDtoRes.getVoucherNumber() != null
								? HtmlEncoder.encode(spPaymentPdfDtoRes.getVoucherNumber())
								: "")
				.replaceAll("@VOUCHERDATE",
						spPaymentPdfDtoRes.getVoucherDate() != null ? spPaymentPdfDtoRes.getVoucherDate() : "")
				.replaceAll("@UNITNAME",
						spPaymentPdfDtoRes.getUnitName() != null ? spPaymentPdfDtoRes.getUnitName() : "")
				.replaceAll("@UNITCODE",
						spPaymentPdfDtoRes.getUnitCode() != null ? spPaymentPdfDtoRes.getUnitCode() : "")
				.replaceAll("@MPHNAME", spPaymentPdfDtoRes.getMphName())
				.replaceAll("@PREPAREDBY",
						spPaymentPdfDtoRes.getPreparedBy() != null
								? HtmlEncoder.encode(spPaymentPdfDtoRes.getPreparedBy())
								: "");

		return reportBody.replace("\\", "").replaceAll("\t", "");
	}

	private String payoutVoucherStyles() {
		return ""
				+ " .tableborder, .tableborder tr th, .tableborder tfoot tr td { border-width:1pt;border-style:solid;border-color:#586ec5;padding:5pt;border-spacing:0pt;border-collapse: collapse; }"
				+ " .tableborder tr td { padding:5pt; }" + " .tdrightalign { text-align:right; } "
				+ " .rightborder { border-right-width:1pt;border-right-style:solid;border-color:#586ec5; } "
				+ " .signaturepadding { padding-bottom: 22pt; }";
	}

	/**
	 *
	 ** Note:-Adjustment Voucher**
	 *
	 **/

	private String adjustmentVoucher(String adjustmentId, String reportType) throws IOException {

		SpPdfDto spPdfDto = generateadjustmentVoucher(adjustmentId, reportType);
		InputStream islicLogo = new ClassPathResource("liclogo.png").getInputStream();
		byte[] bytesLogo = IOUtils.toByteArray(islicLogo);
		String licLogo = Base64.getEncoder().encodeToString(bytesLogo);
		String reportBody1 = "<table style=\"width:100%;\">" + "<tr>" + "<td style=\"width:25%;\">" + "</td>"
				+ "<td style=\"width:50%;text-align:center;\">"
				+ "<img width=\"100\" height=\"63\" src=\"data:image/jpg;base64," + licLogo + "\"/>" + "</td>"
				+ "<td style=\"width:25%;vertical-align: bottom;\">"
				+ "<div style=\"padding:3pt;border:1pt;border-style:solid;border-color:#000;text-align:center;\">ADJUSTMENT VOUCHER</div>"
				+ "</td>" + "</tr>" + "</table>" + "<table style=\"width:100%;\">" + "<tr>"
				+ "<td style=\"width:70%;\">P &amp; GS Dept. @PNGSDEPT</td>"
				+ "<td style=\"width:30%;\">Voucher No. @VOUCHERNO</td>" + "</tr>" + "<tr>"
				+ "<td>Divl. Name: @DIVLNAME</td>" + "<td>Date: @VOUCHERDATE</td>" + "</tr>" + "</table>"
				+ "<p class=\"pb10\" style=\\\"text-align:center;\\\">To Cash / Banking Section</p>"
				+ "<p class=\"pb10\">Please adjust / Issue Crossed / Order Cheque / M O / Postage Stamps favouring: @FAVOURING</p>"
				+ "<p class=\"pb10\">@AMOUNTINWORDS</p>" + "<table style=\"width:100%;\">" + "<tr>"
				+ "<td style=\"width:50%;padding-bottom:10pt;\">Policy No.: @POLICYNUMBER</td>"
				+ "<td style=\"width:50%;\">Scheme Name: @SCHEMENAME</td>" + "</tr>" + "</table>"
				+ "<table class=\"tableborder\" callpadding=\"0\" cellspacing=\"0\" style=\"width:100%;min-height:100pt;\">"
				+ "<tr>" + "<th>Head of Account</th>" + "<th>Code</th>" + "<th class=\"tdrightalign\">Debit Amount</th>"
				+ "<th class=\"tdrightalign\">Credit Amount</th></tr>";

		BigDecimal debitTotal = BigDecimal.ZERO;
		BigDecimal creditTotal = BigDecimal.ZERO;
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		String detailTable = "";
		for (AdjustmentVoucherDetailDto adjustmentVoucherDetailDto : spPdfDto.getVoucherDetail()) {
			detailTable += "<tr>" + "<td class=\"rightborder\">" + adjustmentVoucherDetailDto.getHeadOfAccount()
					+ "</td>" + "<td class=\"rightborder\">" + adjustmentVoucherDetailDto.getCode() + "</td>"
					+ "<td class=\"tdrightalign rightborder\">"
					+ formatter.format(adjustmentVoucherDetailDto.getDebitBigdecimal()) + "</td>"
					+ "<td class=\"tdrightalign\">" + formatter.format(adjustmentVoucherDetailDto.getCreditBigDecimal())
					+ "</td>" + "</tr>";
			debitTotal = debitTotal.add(adjustmentVoucherDetailDto.getDebitBigdecimal());
			creditTotal = creditTotal.add(adjustmentVoucherDetailDto.getCreditBigDecimal());
		}

		// to adjust min height
		for (int i = 1; i <= 5; i++) {
			detailTable += "<tr>" + "<td class=\"rightborder\"></td>" + "<td class=\"rightborder\"></td>"
					+ "<td class=\"tdrightalign rightborder\"></td>" + "<td class=\"tdrightalign\"></td>" + "</tr>";
		}
		detailTable += "<tfoot>" + "<tr>" + "<td colspan=\"2\">Total</td>" + "<td class=\"tdrightalign\">"
				+ formatter.format(debitTotal) + "</td>" + "<td class=\"tdrightalign\">" + formatter.format(creditTotal)
				+ "</td>" + "</tr>" + "</tfoot>" + "</table>";

		String summaryTable = "<p style=\"padding-top:10pt;\">As per the following details</p>"
				+ "<table class=\"tableborder\" callpadding=\"0\" cellspacing=\"0\" style=\"width:100%;min-height:100pt;\">";
		// to adjust min height
		for (int i = 1; i <= 1; i++) {
			summaryTable += "<tr><td></td></tr>";
		}

		summaryTable += "<tr><td><p class=\"pb10\"> @ADJUSTMENTTYPELABEL</p><p>Balance Deposit: "
				+ formatter.format(spPdfDto.getBalanceDeposit()) + "</p></td></tr>";
		summaryTable += "<tr><td><table style=\"width:75%;\"><tr>";
		summaryTable += "<td>ARD: @ANNUALRENEWALDATE</td>";
		summaryTable += "<td>Mode: @MODE</td>";
		summaryTable += "<td>Due For: @DUEFOR</td>";
		summaryTable += "</tr></table></td></tr>";
		summaryTable += "<tr><td><p>Details of Adjustment</p></td></tr>";
		summaryTable += "<table style=\"width:100%;\">";
		summaryTable += "<tr><td class=\"bottomborder\">No</td><td class=\"bottomborder\">Date</td><td class=\"bottomborder tdrightalign\">Amount</td><td class=\"bottomborder tdrightalign\">Adjusted</td><td class=\"bottomborder tdrightalign\">Balance</td></tr>";

		for (SupplementaryAdjustmentDto supplementaryAdjustmentDto : spPdfDto.getSupplymentary()) {
			summaryTable += "<tr>" + "<td>" + supplementaryAdjustmentDto.getDetailsNo() + "</td>" + "<td>"
					+ supplementaryAdjustmentDto.getDetailsDate() + "</td>" + "<td class=\"tdrightalign\">"
					+ formatter.format(supplementaryAdjustmentDto.getDetailsAmount()) + "</td>"
					+ "<td class=\"tdrightalign\">" + formatter.format(supplementaryAdjustmentDto.getDetailsAdjusted())
					+ "</td>" + "<td class=\"tdrightalign\">"
					+ formatter.format(supplementaryAdjustmentDto.getDetailsBalance()) + "</td>" + "</tr>";
		}

		summaryTable += "</table>";
		// to adjust min height
		for (int i = 1; i <= 1; i++) {
			summaryTable += "<tr><td></td></tr>";
		}
		summaryTable += "</table>";
		
		
		
		//Deposit Details 
		String	 depositDetails = "<p> &nbsp; </p>"
				+ "    <p>"
				+ "      Deposit Details"
				+ "    </p>"
				+"      <table style=\" border-collapse: collapse; width: 100%; border: 1px solid blue ;\">"
				+ "        <thead>"
				+ "          <tr>"
				+ "            <th style=\" border: 1px solid blue\">&nbsp;Collection No</th>"
				+ "            <th style=\" border: 1px solid blue\">&nbsp;Collection Date</th>"
				+ "            <th style=\" border: 1px solid blue\">&nbsp;Amount</th>"
				+ "            <th style=\" border: 1px solid blue\">&nbsp;Adjusted Amount</th>"
				+ "            <th style=\" border: 1px solid blue\">&nbsp;Available Amount</th>"
				+ "            <th style=\" border: 1px solid blue;\">&nbsp;Deposit Date</th>"
				+ "          </tr>"
				+ "        </thead>"
				+ "        <tbody>"
				+ "          <tr>"
				+ "            <td style=\"border: 1px solid blue;\">"+spPdfDto.getPolicyDepositDto().getCollectionNo()+"</td>"
				+ "            <td style=\"border: 1px solid blue;\">"+DateUtils.dateToStringDDMMYYYY(spPdfDto.getPolicyDepositDto().getCollectionDate())+"</td>"
				+ "            <td style=\"border: 1px solid blue;\">"+spPdfDto.getPolicyDepositDto().getDepositAmount()+"</td>"
				+ "            <td style=\"border: 1px solid blue;\">"+spPdfDto.getPolicyDepositDto().getAdjustmentAmount()+"</td>"
				+ "            <td style=\"border: 1px solid blue;\">"+spPdfDto.getPolicyDepositDto().getAvailableAmount()+"</td>"
				+ "            <td style=\"border: 1px solid blue;\">"+DateUtils.dateToStringDDMMYYYY(spPdfDto.getPolicyDepositDto().getChequeRealisationDate())+"</td>"
				+ "          </tr>"
				+ "        </tbody>"
				+ "</table>";
		
		
		//Past adjustments with this deposit 
				String	pastAdjustmentsDeposit = "<p> &nbsp; </p>"
						+ "    <p>"
						+ "      Past Adjustments with this Deposit"
						+ "    </p>"
						+"      <table style=\" border-collapse: collapse; width: 100%; border: 1px solid blue ;\">"
						+ "        <thead>"
						+ "          <tr>"
						+ "            <th style=\" border: 1px solid blue\">&nbsp;Contribution Reference No</th>"
						+ "            <th style=\" border: 1px solid blue\">&nbsp;Contribution Date</th>"
						+ "            <th style=\" border: 1px solid blue\">&nbsp;Adjustment Due Date</th>"
						+ "            <th style=\" border: 1px solid blue\">&nbsp;Adjusted Amount</th>"
						+ "          </tr>"
						+ "        </thead>";
				for(PolicyContributionDto pCDto:spPdfDto.getPolicyContributionDtoList()) {	
				pastAdjustmentsDeposit +=	 "          <tr>"
						+ "            <td style=\"border: 1px solid blue;\">"+pCDto.getContReferenceNo()+"</td>"
						+ "            <td style=\"border: 1px solid blue;\">"+DateUtils.dateToStringDDMMYYYY(pCDto.getContributionDate())+"</td>"
						+ "            <td style=\"border: 1px solid blue;\">"+spPdfDto.getAdjustmentDueDate()+"</td>"
						+ "            <td style=\"border: 1px solid blue;\">"+pCDto.getTotalContribution()+"</td>"
						+ "          </tr>";
				}
						pastAdjustmentsDeposit +=	 "</table>";

		
		

		String footerTable = "<p class=\"pb10\">&nbsp;</p>" + "<table style=\"width:100%;\">" + "<tr>"
				+ "<td style=\"width:50%\">" + "<p class=\"signaturepadding\">Prepared By: " + spPdfDto.getPreparedBy()
				+ "</p>" + "<p class=\"signaturepadding\">Checked By: " + spPdfDto.getCheckedBy() + "</p>"
				+ "<p class=\"pb10\">Date: " + spPdfDto.getDate() + "</p>" + "<p class=\"pb10\">Cheque No: "
				+ spPdfDto.getChequeNumber() + "</p>" + "<p>Drawn on: " + spPdfDto.getDrawn() + "</p>" + "</td>"
				+ "<td>" + "<table style=\"width:100%;\">" + "<tr>" + "<td>"
				+ "<table class=\"tableborder\" callpadding=\"0\" cellspacing=\"0\" style=\"width:100%;min-height:100pt;\">"
				+ "<tr>" + "<th>" + "<p class=\"pb10\" style=\"text-align:center;\">PAY</p>"
				+ "<p class=\"pb10\">Sign:</p>" + "<p>Date:" + spPdfDto.getPayDate() + "</p>" + "</th>" + "<th>"
				+ "<p class=\"pb10\" style=\"text-align:center;\">PAID</p>" + "<p class=\"signaturepadding\">Sign:</p>"
				+ "<p>Date:" + spPdfDto.getPaidDate() + "</p>" + "</th>" + "</tr>" + "<tr>" + "<th colspan=\"2\">"
				+ "<p class=\"pt10\" style=\"text-align:center;\">Initials of Officers Signing cheques</p>" + "</th>"
				+ "</tr>" + "</table>" + "</td>" + "</tr>" + "</table>" + "</td>" + "</tr>" + "</table>";

		String adjustmenttypeLabel = new String();
		if (reportType.equalsIgnoreCase("regularAdjustment")) {
			adjustmenttypeLabel = "Regular Deposit Adjustment";
		} else if (reportType.equalsIgnoreCase("subsequentAdjustment")) {
			adjustmenttypeLabel = "Supplementary Deposit Adjustment";
		} else if (reportType.equalsIgnoreCase("NB")) {
			adjustmenttypeLabel = "New Business Deposit Adjustment";
		}

		String mode = null;
		if (spPdfDto.getMode() != null) {
			if ("1".equals(spPdfDto.getMode())) {
				mode = "Monthly";
			} else if ("2".equals(spPdfDto.getMode())) {
				mode = "Quarterly";
			} else if ("4".equals(spPdfDto.getMode())) {
				mode = "Half-Yearly";
			} else if ("3".equals(spPdfDto.getMode())) {
				mode = "Yearly";
			}
		} else {
			mode = "";
		}

		String reportBody = reportBody1 + detailTable + depositDetails+pastAdjustmentsDeposit + summaryTable + footerTable;
		reportBody = reportBody.replaceAll("@ADJUSTMENTTYPELABEL", adjustmenttypeLabel)
				.replaceAll("@PNGSDEPT", spPdfDto.getPngsDept() != null ? spPdfDto.getPngsDept() : "PNGSDEPT")
				.replaceAll("@VOUCHERNO", spPdfDto.getVoucherNumber() != null ? spPdfDto.getVoucherNumber() : "")
				.replaceAll("@DIVLNAME", spPdfDto.getDivisionalName() != null ? spPdfDto.getDivisionalName() : "")
				.replaceAll("@VOUCHERDATE", spPdfDto.getVoucherDate() != null ? spPdfDto.getVoucherDate() : "")
				.replaceAll("@FAVOURING", spPdfDto.getFavouringName() != null ? spPdfDto.getFavouringName() : "")
				.replaceAll("@AMOUNTINWORDS", spPdfDto.getAmountInWords() != null ? spPdfDto.getAmountInWords() : "")
				.replaceAll("@POLICYNUMBER", spPdfDto.getPolicyNumber() != null ? spPdfDto.getPolicyNumber() : "")
				.replaceAll("@SCHEMENAME", spPdfDto.getSchemeName() != null ? spPdfDto.getSchemeName() : "")
				.replaceAll("@BALANCEDEPOSIT",
						spPdfDto.getBalanceDeposit() != null
								? NumericUtils.convertBigDecimalToString(spPdfDto.getBalanceDeposit())
								: "")
				.replaceAll("@ANNUALRENEWALDATE", spPdfDto.getArd() != null ? spPdfDto.getArd() : "")
				.replaceAll("@MODE", mode != null ? mode : "")
				.replaceAll("@DUEFOR", spPdfDto.getDuedate() != null ? spPdfDto.getDuedate() : "")
				.replaceAll("@VOUCHERNO", spPdfDto.getVoucherNumber() != null ? spPdfDto.getVoucherNumber() : "")
				.replaceAll("@VOUCHERDATE", spPdfDto.getVoucherDate() != null ? spPdfDto.getVoucherDate() : "")
				.replaceAll("@PREPAREDBY", spPdfDto.getPreparedBy() != null ? spPdfDto.getPreparedBy() : "");
		return reportBody.replace("\\", "").replaceAll("\t", "");
	}

	private SpPdfDto generateadjustmentVoucher(String adjustmentId, String reportType)
			throws MalformedURLException, ProtocolException, IOException {
		SpPdfDto spPdfDto = new SpPdfDto();
		if (reportType.equalsIgnoreCase("subsequentAdjustment")) {
			AdjustmentContributionEntity adjustmentContributionEntity = adjustmentContributionRepository
					.findByAdjustmentContributionIdAndIsActiveTrue(NumericUtils.convertStringToLong(adjustmentId));
			PolicyMasterEntity policyMasterEntity = policyMasterRepository
					.findByPolicyIdAndIsActiveTrue(adjustmentContributionEntity.getPolicyId());
			PolicyContributionEntity policyContributionEntity = policyContributionRepository
					.findByPolicyIdAndAdjustmentContributionId(adjustmentContributionEntity.getPolicyId(),
							adjustmentContributionEntity.getAdjustmentContributionId());
			PolicyDepositEntity policyDepositEntity = policyDepositRepository.findByPolicyIdAndAdjustmentContributionId(
					adjustmentContributionEntity.getPolicyId(),
					adjustmentContributionEntity.getAdjustmentContributionId());
			List<AdjustmentVoucherDetailDto> voucherDetail = new ArrayList<>();
			List<SupplementaryAdjustmentDto> supplymentary = new ArrayList<>();
			spPdfDto.setPngsDept("");
			spPdfDto.setDivisionalName(policyMasterEntity.getUnitOffice());
			spPdfDto.setVoucherNumber(policyDepositEntity.getChallanNo());
			spPdfDto.setVoucherDate(DateUtils.dateToStringDDMMYYYY(new Date()));
			spPdfDto.setDate("");
			MphMasterEntity mphName = mphMasterRepository.findByMphIdAndIsActiveTrue(policyMasterEntity.getMphId());
			spPdfDto.setFavouringName(mphName.getMphName());
			spPdfDto.setPolicyNumber(policyMasterEntity.getPolicyNumber());
			spPdfDto.setSchemeName(NumericUtils.convertLongToString(policyMasterEntity.getProductId()));
			spPdfDto.setArd(DateUtils.dateToStringDDMMYYYY(policyMasterEntity.getArd()));
			spPdfDto.setMode(NumericUtils.convertIntegerToString(policyMasterEntity.getContributionFrequency()));
			spPdfDto.setDuedate(DateUtils.dateToStringDDMMYYYY(adjustmentContributionEntity.getAdjustmentForDate()));
			spPdfDto.setDrawn("");
			spPdfDto.setChequeNumber("");

			spPdfDto.setPayDate(DateUtils.dateToStringDDMMYYYY(adjustmentContributionEntity.getModifiedOn()));
			spPdfDto.setPaidDate(DateUtils.dateToStringDDMMYYYY(policyContributionEntity.getModifiedOn()));
			spPdfDto.setPreparedBy(adjustmentContributionEntity.getCreatedBy());
			spPdfDto.setCheckedBy(adjustmentContributionEntity.getModifiedBy());
			spPdfDto.setTotal(NumericUtils.convertBigDecimalToString(policyDepositEntity.getDepositAmount()));
			spPdfDto.setBalanceDeposit(policyDepositEntity.getAvailableAmount());
//			spPdfDto.setAmountInWords("Rs."+ NumericUtils.convertAmountToWord(NumericUtils.convertStringToLong(spPdfDto.getTotal())) + " Only");
			spPdfDto.setAmountInWords("Rs."
					+ NumericUtils.convertAmountToWord(NumericUtils.convertStringToLong(
							NumericUtils.convertBigDecimalToString(policyDepositEntity.getAdjustmentAmount())))
					+ " Only");

			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto = new AdjustmentVoucherDetailDto();
			if (policyDepositEntity.getDepositAmount() != null
					&& policyDepositEntity.getDepositAmount() != BigDecimal.ZERO) {
				adjustmentVoucherDetailDto.setHeadOfAccount("Deposit Policy ");
				adjustmentVoucherDetailDto.setCode("1301");
				adjustmentVoucherDetailDto.setDebitBigdecimal(policyDepositEntity.getAdjustmentAmount());
				adjustmentVoucherDetailDto.setCreditBigDecimal(BigDecimal.ZERO);
			}
			if (adjustmentVoucherDetailDto.getCreditBigDecimal() != null
					&& adjustmentVoucherDetailDto.getDebitBigdecimal() != null) {
				voucherDetail.add(adjustmentVoucherDetailDto);
			}
			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto1 = new AdjustmentVoucherDetailDto();
			if (adjustmentContributionEntity.getFirstPremium() != null
					&& adjustmentContributionEntity.getFirstPremium() != BigDecimal.ZERO) {
				adjustmentVoucherDetailDto1.setHeadOfAccount("First Premium");
				adjustmentVoucherDetailDto1.setCode("3803");
				adjustmentVoucherDetailDto1.setDebitBigdecimal(BigDecimal.ZERO);
				adjustmentVoucherDetailDto1.setCreditBigDecimal(adjustmentContributionEntity.getFirstPremium());
			}
			if (adjustmentVoucherDetailDto1.getCreditBigDecimal() != null
					&& adjustmentVoucherDetailDto1.getDebitBigdecimal() != null) {
				voucherDetail.add(adjustmentVoucherDetailDto1);
			}
			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto2 = new AdjustmentVoucherDetailDto();
			if (adjustmentContributionEntity.getSinglePremiumFirstYr() != null
					&& adjustmentContributionEntity.getSinglePremiumFirstYr() != BigDecimal.ZERO) {
				adjustmentVoucherDetailDto2.setHeadOfAccount("Single Premium - First Year");
				adjustmentVoucherDetailDto2.setCode("3953");
				adjustmentVoucherDetailDto2.setDebitBigdecimal(BigDecimal.ZERO);
				adjustmentVoucherDetailDto2.setCreditBigDecimal(adjustmentContributionEntity.getSinglePremiumFirstYr());
			}
			if (adjustmentVoucherDetailDto2.getCreditBigDecimal() != null
					&& adjustmentVoucherDetailDto2.getDebitBigdecimal() != null) {
				voucherDetail.add(adjustmentVoucherDetailDto2);
			}

			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto3 = new AdjustmentVoucherDetailDto();
			if (adjustmentContributionEntity.getRenewalPremium() != null
					&& adjustmentContributionEntity.getRenewalPremium() != BigDecimal.ZERO) {
				adjustmentVoucherDetailDto3.setHeadOfAccount("Renewal Premium ");
				adjustmentVoucherDetailDto3.setCode("3903");
				adjustmentVoucherDetailDto3.setDebitBigdecimal(BigDecimal.ZERO);
				adjustmentVoucherDetailDto3.setCreditBigDecimal(adjustmentContributionEntity.getRenewalPremium());
			}
			if (adjustmentVoucherDetailDto3.getCreditBigDecimal() != null
					&& adjustmentVoucherDetailDto3.getDebitBigdecimal() != null) {
				voucherDetail.add(adjustmentVoucherDetailDto3);
			}
			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto4 = new AdjustmentVoucherDetailDto();
			if (adjustmentContributionEntity.getSubsequentSinglePremium() != null
					&& adjustmentContributionEntity.getSubsequentSinglePremium() != BigDecimal.ZERO) {
				adjustmentVoucherDetailDto4.setHeadOfAccount("Subsequent Single Premium ");
				adjustmentVoucherDetailDto4.setCode("3983");
				adjustmentVoucherDetailDto4.setDebitBigdecimal(BigDecimal.ZERO);
				adjustmentVoucherDetailDto4
						.setCreditBigDecimal(adjustmentContributionEntity.getSubsequentSinglePremium());
			}
			if (adjustmentVoucherDetailDto4.getCreditBigDecimal() != null
					&& adjustmentVoucherDetailDto4.getDebitBigdecimal() != null) {
				voucherDetail.add(adjustmentVoucherDetailDto4);
			}
			spPdfDto.setVoucherDetail(voucherDetail);
			SupplementaryAdjustmentDto supplementaryAdjustmentDto = new SupplementaryAdjustmentDto();
			supplementaryAdjustmentDto.setDetailsNo(policyDepositEntity.getChallanNo());
			supplementaryAdjustmentDto
					.setDetailsDate(DateUtils.dateToStringDDMMYYYY(policyContributionEntity.getContributionDate()));
			supplementaryAdjustmentDto.setDetailsAmount(policyDepositEntity.getDepositAmount());
			supplementaryAdjustmentDto.setDetailsAdjusted(policyDepositEntity.getAdjustmentAmount());
			supplementaryAdjustmentDto.setDetailsBalance(policyDepositEntity.getAvailableAmount());
			supplymentary.add(supplementaryAdjustmentDto);
			spPdfDto.setSupplymentary(supplymentary);
			
			
			//Adjustment with Deposit Details 
			//start - sa
			
		List<PolicyDepositEntity>policyDepositEntityList= policyDepositRepository.findByCollectionNo(policyDepositEntity.getCollectionNo());
			
		List<PolicyContributionDto>policyContributionDtoList = new ArrayList<PolicyContributionDto>();


			if(!policyDepositEntityList.isEmpty()) {
				for(PolicyDepositEntity depositEntity:policyDepositEntityList) {
					
					Object contributionEntity = policyContributionRepository.findByContReferenceNo(depositEntity.getChallanNo());
				
				if(contributionEntity!=null) {

					
					Object[] obj=(Object[])contributionEntity;
					
					PolicyContributionDto policyContributionDto = new PolicyContributionDto();
					
					policyContributionDto.setContReferenceNo(String.valueOf(obj[0]));
					policyContributionDto.setContributionDate(DateUtils.convertStringToDate(String.valueOf(obj[1])));
					spPdfDto.setAdjustmentDueDate(String.valueOf(obj[2]));
					policyContributionDto.setTotalContribution(NumericUtils.stringToBigDecimal(String.valueOf(obj[3])));
				
					policyContributionDtoList.add(policyContributionDto);

				}

				}

				spPdfDto.setPolicyContributionDtoList(policyContributionDtoList);

			}
			
			//deposit 
			PolicyDepositDto policyDepositDto = new PolicyDepositDto();
	
			policyDepositDto.setCollectionNo(policyDepositEntity.getCollectionNo());
			policyDepositDto.setCollectionDate(policyDepositEntity.getCollectionDate());
			policyDepositDto.setDepositAmount(policyDepositEntity.getDepositAmount());
			policyDepositDto.setAdjustmentAmount(policyDepositEntity.getAdjustmentAmount());
			policyDepositDto.setAvailableAmount(policyDepositEntity.getAvailableAmount());
			policyDepositDto.setChequeRealisationDate(policyDepositEntity.getChequeRealisationDate());
			
			spPdfDto.setPolicyDepositDto(policyDepositDto);
			
			//end
			

			String schemeName = getSchemaName(policyMasterEntity.getProductId());
			spPdfDto.setSchemeName(schemeName);
		} else if (reportType.equalsIgnoreCase("regularAdjustment")) {

			RegularAdjustmentContributionEntity adjustmentContributionEntity = regularAdjustmentContributionRepository
					.findByRegularContributionIdAndIsActiveTrue(NumericUtils.convertStringToLong(adjustmentId));
			PolicyMasterEntity policyMasterEntity = policyMasterRepository
					.findByPolicyIdAndIsActiveTrue(adjustmentContributionEntity.getPolicyId());
			PolicyContributionEntity policyContributionEntity = policyContributionRepository
					.findByPolicyIdAndRegularContributionId(adjustmentContributionEntity.getPolicyId(),
							adjustmentContributionEntity.getRegularContributionId());
			PolicyDepositEntity policyDepositEntity = policyDepositRepository.findByPolicyIdAndRegularContributionId(
					adjustmentContributionEntity.getPolicyId(),
					adjustmentContributionEntity.getRegularContributionId());
			List<AdjustmentVoucherDetailDto> voucherDetail = new ArrayList<>();
			List<SupplementaryAdjustmentDto> supplymentary = new ArrayList<>();
			spPdfDto.setPngsDept("");
			spPdfDto.setDivisionalName(policyMasterEntity.getUnitOffice());
			spPdfDto.setVoucherNumber(policyDepositEntity.getChallanNo());
			spPdfDto.setVoucherDate(DateUtils.dateToStringDDMMYYYY(new Date()));
			spPdfDto.setDate("");
			MphMasterEntity mphName = mphMasterRepository.findByMphIdAndIsActiveTrue(policyMasterEntity.getMphId());
			spPdfDto.setFavouringName(mphName.getMphName());
			spPdfDto.setPolicyNumber(policyMasterEntity.getPolicyNumber());
			spPdfDto.setSchemeName(NumericUtils.convertLongToString(policyMasterEntity.getProductId()));
			spPdfDto.setArd(DateUtils.dateToStringDDMMYYYY(policyMasterEntity.getArd()));
			spPdfDto.setMode(NumericUtils.convertIntegerToString(policyMasterEntity.getContributionFrequency()));
			spPdfDto.setDuedate(DateUtils.dateToStringDDMMYYYY(adjustmentContributionEntity.getAdjustmentForDate()));
			spPdfDto.setDrawn("");
			spPdfDto.setChequeNumber("");
			spPdfDto.setPayDate(DateUtils.dateToStringDDMMYYYY(adjustmentContributionEntity.getModifiedOn()));
			spPdfDto.setPaidDate(DateUtils.dateToStringDDMMYYYY(policyContributionEntity.getModifiedOn()));
			spPdfDto.setPreparedBy(adjustmentContributionEntity.getCreatedBy());
			spPdfDto.setCheckedBy(adjustmentContributionEntity.getModifiedBy());
			spPdfDto.setTotal(NumericUtils.convertBigDecimalToString(policyDepositEntity.getDepositAmount()));
			spPdfDto.setBalanceDeposit(policyDepositEntity.getAvailableAmount());
//			spPdfDto.setAmountInWords("Rs."+ NumericUtils.convertAmountToWord(NumericUtils.convertStringToLong(spPdfDto.getTotal())) + " Only");

			spPdfDto.setAmountInWords("Rs."
					+ NumericUtils.convertAmountToWord(NumericUtils.convertStringToLong(
							NumericUtils.convertBigDecimalToString(policyDepositEntity.getAdjustmentAmount())))
					+ " Only");

			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto = new AdjustmentVoucherDetailDto();
			if (policyDepositEntity.getDepositAmount() != null
					&& policyDepositEntity.getDepositAmount() != BigDecimal.ZERO) {
				adjustmentVoucherDetailDto.setHeadOfAccount("Deposit Policy ");
				adjustmentVoucherDetailDto.setCode("1301");
				adjustmentVoucherDetailDto.setDebitBigdecimal(policyDepositEntity.getAdjustmentAmount());
				adjustmentVoucherDetailDto.setCreditBigDecimal(BigDecimal.ZERO);
			}
			if (adjustmentVoucherDetailDto.getCreditBigDecimal() != null
					&& adjustmentVoucherDetailDto.getDebitBigdecimal() != null) {
				voucherDetail.add(adjustmentVoucherDetailDto);
			}
			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto1 = new AdjustmentVoucherDetailDto();
			if (adjustmentContributionEntity.getFirstPremium() != null
					&& adjustmentContributionEntity.getFirstPremium() != BigDecimal.ZERO) {
				adjustmentVoucherDetailDto1.setHeadOfAccount("First Premium");
				adjustmentVoucherDetailDto1.setCode("3803");
				adjustmentVoucherDetailDto1.setDebitBigdecimal(BigDecimal.ZERO);
				adjustmentVoucherDetailDto1.setCreditBigDecimal(adjustmentContributionEntity.getFirstPremium());
			}
			if (adjustmentVoucherDetailDto1.getCreditBigDecimal() != null
					&& adjustmentVoucherDetailDto1.getDebitBigdecimal() != null) {
				voucherDetail.add(adjustmentVoucherDetailDto1);
			}
			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto2 = new AdjustmentVoucherDetailDto();
			if (adjustmentContributionEntity.getSinglePremiumFirstYr() != null
					&& adjustmentContributionEntity.getSinglePremiumFirstYr() != BigDecimal.ZERO) {
				adjustmentVoucherDetailDto2.setHeadOfAccount("Single Premium - First Year");
				adjustmentVoucherDetailDto2.setCode("3953");
				adjustmentVoucherDetailDto2.setDebitBigdecimal(BigDecimal.ZERO);
				adjustmentVoucherDetailDto2.setCreditBigDecimal(adjustmentContributionEntity.getSinglePremiumFirstYr());
			}
			if (adjustmentVoucherDetailDto2.getCreditBigDecimal() != null
					&& adjustmentVoucherDetailDto2.getDebitBigdecimal() != null) {
				voucherDetail.add(adjustmentVoucherDetailDto2);
			}
			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto3 = new AdjustmentVoucherDetailDto();
			if (adjustmentContributionEntity.getRenewalPremium() != null
					&& adjustmentContributionEntity.getRenewalPremium() != BigDecimal.ZERO) {
				adjustmentVoucherDetailDto3.setHeadOfAccount("Renewal Premium ");
				adjustmentVoucherDetailDto3.setCode("3903");
				adjustmentVoucherDetailDto3.setDebitBigdecimal(BigDecimal.ZERO);
				adjustmentVoucherDetailDto3.setCreditBigDecimal(adjustmentContributionEntity.getRenewalPremium());
			}
			if (adjustmentVoucherDetailDto3.getCreditBigDecimal() != null
					&& adjustmentVoucherDetailDto3.getDebitBigdecimal() != null) {
				voucherDetail.add(adjustmentVoucherDetailDto3);
			}
			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto4 = new AdjustmentVoucherDetailDto();
			if (adjustmentContributionEntity.getSubsequentSinglePremium() != null
					&& adjustmentContributionEntity.getSubsequentSinglePremium() != BigDecimal.ZERO) {
				adjustmentVoucherDetailDto4.setHeadOfAccount("Subsequent Single Premium ");
				adjustmentVoucherDetailDto4.setCode("3983");
				adjustmentVoucherDetailDto4.setDebitBigdecimal(BigDecimal.ZERO);
				adjustmentVoucherDetailDto4
						.setCreditBigDecimal(adjustmentContributionEntity.getSubsequentSinglePremium());
			}
			if (adjustmentVoucherDetailDto4.getCreditBigDecimal() != null
					&& adjustmentVoucherDetailDto4.getDebitBigdecimal() != null) {
				voucherDetail.add(adjustmentVoucherDetailDto4);
			}
			spPdfDto.setVoucherDetail(voucherDetail);
			SupplementaryAdjustmentDto supplementaryAdjustmentDto = new SupplementaryAdjustmentDto();
			supplementaryAdjustmentDto.setDetailsNo(policyDepositEntity.getChallanNo());
			supplementaryAdjustmentDto
					.setDetailsDate(DateUtils.dateToStringDDMMYYYY(policyContributionEntity.getContributionDate()));
			supplementaryAdjustmentDto.setDetailsAmount(policyDepositEntity.getDepositAmount());
			supplementaryAdjustmentDto.setDetailsAdjusted(policyDepositEntity.getAdjustmentAmount());
			supplementaryAdjustmentDto.setDetailsBalance(policyDepositEntity.getAvailableAmount());
			supplymentary.add(supplementaryAdjustmentDto);
			spPdfDto.setSupplymentary(supplymentary);
			
			
			//Adjustment with Deposit Details 
			//start -ra
			
		List<PolicyDepositEntity>policyDepositEntityList= policyDepositRepository.findByCollectionNo(policyDepositEntity.getCollectionNo());
			
		List<PolicyContributionDto>policyContributionDtoList = new ArrayList<PolicyContributionDto>();


		if(!policyDepositEntityList.isEmpty()) {
			for(PolicyDepositEntity depositEntity:policyDepositEntityList) {
				
				Object contributionEntity = policyContributionRepository.findByContReferenceNo(depositEntity.getChallanNo());
			
			if(contributionEntity!=null) {

				
				Object[] obj=(Object[])contributionEntity;
				
				PolicyContributionDto policyContributionDto = new PolicyContributionDto();
				
				policyContributionDto.setContReferenceNo(String.valueOf(obj[0]));
				
			
				policyContributionDto.setContributionDate(DateUtils.convertStringToDate(String.valueOf(obj[1])));
				spPdfDto.setAdjustmentDueDate(String.valueOf(obj[2]));
				policyContributionDto.setTotalContribution(NumericUtils.stringToBigDecimal(String.valueOf(obj[3])));
			
				policyContributionDtoList.add(policyContributionDto);

			}

			}

			spPdfDto.setPolicyContributionDtoList(policyContributionDtoList);


			}
			
			//Deposit Details -2
			PolicyDepositDto policyDepositDto = new PolicyDepositDto();
			
			policyDepositDto.setCollectionNo(policyDepositEntity.getCollectionNo());
			policyDepositDto.setCollectionDate(policyDepositEntity.getCollectionDate());
			policyDepositDto.setDepositAmount(policyDepositEntity.getDepositAmount());
			policyDepositDto.setAdjustmentAmount(policyDepositEntity.getAdjustmentAmount());
			policyDepositDto.setAvailableAmount(policyDepositEntity.getAvailableAmount());
			policyDepositDto.setChequeRealisationDate(policyDepositEntity.getChequeRealisationDate());
			
			spPdfDto.setPolicyDepositDto(policyDepositDto);
		
			//end
			
			

			String schemeName = getSchemaName(policyMasterEntity.getProductId());
			spPdfDto.setSchemeName(schemeName);
		} else if (reportType.equalsIgnoreCase("NB")) {
//			RegularAdjustmentContributionEntity adjustmentContributionEntity = regularAdjustmentContributionRepository
//					.findByRegularContributionIdAndIsActiveTrue(NumericUtils.convertStringToLong(adjustmentId));
			PolicyMasterEntity policyMasterEntity = policyMasterRepository
					.findByPolicyIdAndIsActiveTrue(NumericUtils.convertStringToLong(adjustmentId));
			PolicyContributionEntity policyContributionEntity = policyContributionRepository
					.findByPolicyIdAndContributionType(policyMasterEntity.getPolicyId(), reportType);
			PolicyDepositEntity policyDepositEntity = policyDepositRepository
					.findByPolicyIdAndContributionType(policyMasterEntity.getPolicyId(), reportType);

			List<AdjustmentVoucherDetailDto> voucherDetail = new ArrayList<>();
			List<SupplementaryAdjustmentDto> supplymentary = new ArrayList<>();
			spPdfDto.setPngsDept("");
			spPdfDto.setDivisionalName(policyMasterEntity.getUnitOffice());
			spPdfDto.setVoucherNumber(policyDepositEntity.getChallanNo());
			spPdfDto.setVoucherDate(DateUtils.dateToStringDDMMYYYY(new Date()));
			spPdfDto.setDate("");
			MphMasterEntity mphName = mphMasterRepository.findByMphIdAndIsActiveTrue(policyMasterEntity.getMphId());
			spPdfDto.setFavouringName(mphName.getMphName());
			spPdfDto.setPolicyNumber(policyMasterEntity.getPolicyNumber());
			spPdfDto.setSchemeName(NumericUtils.convertLongToString(policyMasterEntity.getProductId()));
			spPdfDto.setArd(DateUtils.dateToStringDDMMYYYY(policyMasterEntity.getArd()));
			spPdfDto.setMode(NumericUtils.convertIntegerToString(policyMasterEntity.getContributionFrequency()));
			spPdfDto.setDuedate(DateUtils.dateToStringDDMMYYYY(policyMasterEntity.getPolicyCommencementDt()));
			spPdfDto.setDrawn("");
			spPdfDto.setChequeNumber("");
			spPdfDto.setPayDate(DateUtils.dateToStringDDMMYYYY(policyMasterEntity.getModifiedOn()));
			spPdfDto.setPaidDate(DateUtils.dateToStringDDMMYYYY(policyContributionEntity.getModifiedOn()));
			spPdfDto.setPreparedBy(policyMasterEntity.getCreatedBy() != null ? policyMasterEntity.getCreatedBy() : "");
			spPdfDto.setCheckedBy(policyMasterEntity.getModifiedBy() != null ? policyMasterEntity.getModifiedBy() : "");
			spPdfDto.setTotal(NumericUtils.convertBigDecimalToString(policyDepositEntity.getDepositAmount()));
			spPdfDto.setBalanceDeposit(policyDepositEntity.getAvailableAmount());
//			spPdfDto.setAmountInWords("Rs."+ NumericUtils.convertAmountToWord(NumericUtils.convertStringToLong(spPdfDto.getTotal())) + " Only");

			spPdfDto.setAmountInWords("Rs."
					+ NumericUtils.convertAmountToWord(NumericUtils.convertStringToLong(
							NumericUtils.convertBigDecimalToString(policyDepositEntity.getAdjustmentAmount())))
					+ " Only");

			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto = new AdjustmentVoucherDetailDto();
			if (policyDepositEntity.getDepositAmount() != null
					&& policyDepositEntity.getDepositAmount() != BigDecimal.ZERO) {
				adjustmentVoucherDetailDto.setHeadOfAccount("Deposit Policy ");
				adjustmentVoucherDetailDto.setCode("1301");
				adjustmentVoucherDetailDto.setDebitBigdecimal(policyDepositEntity.getAdjustmentAmount());
				adjustmentVoucherDetailDto.setCreditBigDecimal(BigDecimal.ZERO);
			}
			if (adjustmentVoucherDetailDto.getCreditBigDecimal() != null
					&& adjustmentVoucherDetailDto.getDebitBigdecimal() != null) {
				voucherDetail.add(adjustmentVoucherDetailDto);
			}
			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto1 = new AdjustmentVoucherDetailDto();
			if (policyMasterEntity.getFirstPremium() != null
					&& policyMasterEntity.getFirstPremium() != BigDecimal.ZERO) {
				adjustmentVoucherDetailDto1.setHeadOfAccount("First Premium");
				adjustmentVoucherDetailDto1.setCode("3803");
				adjustmentVoucherDetailDto1.setDebitBigdecimal(BigDecimal.ZERO);
				adjustmentVoucherDetailDto1.setCreditBigDecimal(policyMasterEntity.getFirstPremium());
			}
			if (adjustmentVoucherDetailDto1.getCreditBigDecimal() != null
					&& adjustmentVoucherDetailDto1.getDebitBigdecimal() != null) {
				voucherDetail.add(adjustmentVoucherDetailDto1);
			}
			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto2 = new AdjustmentVoucherDetailDto();
			if (policyMasterEntity.getSinglePremiumFirstYr() != null
					&& policyMasterEntity.getSinglePremiumFirstYr() != BigDecimal.ZERO) {
				adjustmentVoucherDetailDto2.setHeadOfAccount("Single Premium - First Year");
				adjustmentVoucherDetailDto2.setCode("3953");
				adjustmentVoucherDetailDto2.setDebitBigdecimal(BigDecimal.ZERO);
				adjustmentVoucherDetailDto2.setCreditBigDecimal(policyMasterEntity.getSinglePremiumFirstYr());
			}
			if (adjustmentVoucherDetailDto2.getCreditBigDecimal() != null
					&& adjustmentVoucherDetailDto2.getDebitBigdecimal() != null) {
				voucherDetail.add(adjustmentVoucherDetailDto2);
			}
			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto3 = new AdjustmentVoucherDetailDto();
			if (policyMasterEntity.getRenewalPremium() != null
					&& policyMasterEntity.getRenewalPremium() != BigDecimal.ZERO) {
				adjustmentVoucherDetailDto3.setHeadOfAccount("Renewal Premium ");
				adjustmentVoucherDetailDto3.setCode("3903");
				adjustmentVoucherDetailDto3.setDebitBigdecimal(BigDecimal.ZERO);
				adjustmentVoucherDetailDto3.setCreditBigDecimal(policyMasterEntity.getRenewalPremium());
			}
			if (adjustmentVoucherDetailDto3.getCreditBigDecimal() != null
					&& adjustmentVoucherDetailDto3.getDebitBigdecimal() != null) {
				voucherDetail.add(adjustmentVoucherDetailDto3);
			}
			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto4 = new AdjustmentVoucherDetailDto();
			if (policyMasterEntity.getSubsequentSinglePremium() != null
					&& policyMasterEntity.getSubsequentSinglePremium() != BigDecimal.ZERO) {
				adjustmentVoucherDetailDto4.setHeadOfAccount("Subsequent Single Premium ");
				adjustmentVoucherDetailDto4.setCode("3983");
				adjustmentVoucherDetailDto4.setDebitBigdecimal(BigDecimal.ZERO);
				adjustmentVoucherDetailDto4.setCreditBigDecimal(policyMasterEntity.getSubsequentSinglePremium());
			}
			if (adjustmentVoucherDetailDto4.getCreditBigDecimal() != null
					&& adjustmentVoucherDetailDto4.getDebitBigdecimal() != null) {
				voucherDetail.add(adjustmentVoucherDetailDto4);
			}
			spPdfDto.setVoucherDetail(voucherDetail);
			SupplementaryAdjustmentDto supplementaryAdjustmentDto = new SupplementaryAdjustmentDto();
			supplementaryAdjustmentDto.setDetailsNo(policyDepositEntity.getChallanNo());
			supplementaryAdjustmentDto
					.setDetailsDate(DateUtils.dateToStringDDMMYYYY(policyContributionEntity.getContributionDate()));
			supplementaryAdjustmentDto.setDetailsAmount(policyDepositEntity.getDepositAmount());
			supplementaryAdjustmentDto.setDetailsAdjusted(policyDepositEntity.getAdjustmentAmount());
			supplementaryAdjustmentDto.setDetailsBalance(policyDepositEntity.getAvailableAmount());
			supplymentary.add(supplementaryAdjustmentDto);
			spPdfDto.setSupplymentary(supplymentary);
			
			
			//Adjustment with Deposit Details 
			//start -nb
			
		List<PolicyDepositEntity>policyDepositEntityList= policyDepositRepository.findByCollectionNo(policyDepositEntity.getCollectionNo());
			
		List<PolicyContributionDto>policyContributionDtoList = new ArrayList<PolicyContributionDto>();


		if(!policyDepositEntityList.isEmpty()) {
			for(PolicyDepositEntity depositEntity:policyDepositEntityList) {
				
				Object contributionEntity = policyContributionRepository.findByContReferenceNo(depositEntity.getChallanNo());
			
			if(contributionEntity!=null) {

				
				Object[] obj=(Object[])contributionEntity;
				
				PolicyContributionDto policyContributionDto = new PolicyContributionDto();
				
				policyContributionDto.setContReferenceNo(String.valueOf(obj[0]));
				policyContributionDto.setContributionDate(DateUtils.convertStringToDate(String.valueOf(obj[1])));
				spPdfDto.setAdjustmentDueDate(String.valueOf(obj[2]));
				policyContributionDto.setTotalContribution(NumericUtils.stringToBigDecimal(String.valueOf(obj[3])));
			
				policyContributionDtoList.add(policyContributionDto);

			}

			}

			spPdfDto.setPolicyContributionDtoList(policyContributionDtoList);


			}
			
			//Deposit Details -2
			PolicyDepositDto policyDepositDto = new PolicyDepositDto();
			
			policyDepositDto.setCollectionNo(policyDepositEntity.getCollectionNo());
			policyDepositDto.setCollectionDate(policyDepositEntity.getCollectionDate());
			policyDepositDto.setDepositAmount(policyDepositEntity.getDepositAmount());
			policyDepositDto.setAdjustmentAmount(policyDepositEntity.getAdjustmentAmount());
			policyDepositDto.setAvailableAmount(policyDepositEntity.getAvailableAmount());
			policyDepositDto.setChequeRealisationDate(policyDepositEntity.getChequeRealisationDate());
			
			spPdfDto.setPolicyDepositDto(policyDepositDto);
			//end

			

			String schemeName = getSchemeNameByVariantId(NumericUtils.stringToLong(policyMasterEntity.getVariant()));
			spPdfDto.setSchemeName(schemeName);
		}
		return spPdfDto;
	}

	private String getSchemaName(Long productId) throws MalformedURLException, ProtocolException, IOException {
		String product = null;
		JsonNode response = integrationService.getProductDetailsByProductId(productId);
		if (response != null) {
			JsonNode proposeDetails = response.path("responseData");
			product = proposeDetails.path("productCode").textValue();
		}
		return product;
	}

	private String getSchemeNameByVariantId(Long variantId)
			throws MalformedURLException, ProtocolException, IOException {
		String product = null;
		JsonNode response = integrationService
				.getVariantDetailsByProductVariantId(NumericUtils.stringToLong(String.valueOf(variantId)));
		if (response != null) {
			JsonNode proposeDetails = response.path("responseData");
			product = proposeDetails.path("subCategory").textValue();

		}
		return product;
	}
	
	private String getSchemaPlanName(Long productId) throws MalformedURLException, ProtocolException, IOException {
		String productName = null;
		JsonNode response = integrationService.getProductDetailsByProductId(productId);
		if (response != null) {
			JsonNode proposeDetails = response.path("responseData");
			productName = proposeDetails.path("productName").textValue();
		}
		return productName;
	}
	
	private String getUINByVariantId(String variantId)
			throws MalformedURLException, ProtocolException, IOException {
		String product = null;
		JsonNode response = integrationService
				.getVariantDetailsByProductVariantId(NumericUtils.stringToLong(String.valueOf(variantId)));
		if (response != null) {
			JsonNode proposeDetails = response.path("responseData");
			product = proposeDetails.path("uin").textValue();

		}
		return product;
	}

	private String adjustmentVoucherStyles() {
		return ""
				+ " .tableborder, .tableborder tr th, .tableborder tfoot tr td { border-width:1pt;border-style:solid;border-color:#586ec5;padding:5pt;border-spacing:0pt;border-collapse: collapse; }"
				+ " .tableborder tr td { padding:5pt; }" + " .tdrightalign { text-align:right; } "
				+ " .bottomborder { border-bottom-width:1pt;border-bottom-style:dashed;border-color:#586ec5; } "
				+ " .signaturepadding { padding-bottom: 22pt; }";
	}

	/*
	 * 
	 * Note: --withdrawalLetter PDF
	 * 
	 */

	private String withdrawalLetter(String payoutNo, String reportType, String forwardTo)
			throws MalformedURLException, ProtocolException, IOException {
		DecimalFormat formatter = new DecimalFormat("#,###.00");

		LifeCoverDetailsDto lifeCoverDetailsDto = getwithdrawalLetter(payoutNo, reportType, forwardTo);
		System.out.println(lifeCoverDetailsDto.toString());

		String reportBody1 = "<p class=\"pb10\" style=\"text-align:center;\">Life Insurance Corporation Of India</p>";
		reportBody1 += "<p class=\"pb10\" style=\"text-align:center;\">Pension &amp; Group Schemes Dept</p>";
		reportBody1 += "<p class=\"pb10\" style=\"text-align:center;\">P&amp;Gs Dept Mdo-I</p>";
		reportBody1 += "<table style=\"width:100%;\"><tr>";
		reportBody1 += "<td style=\"width:70%\">Ref: @POLICYNUMBER</td>";
		reportBody1 += "<td>Date: @DATE</td>";
		reportBody1 += "</tr></table>";
		reportBody1 += "<p class=\"pt10\">@ADDRESSEE</p>";
		reportBody1 += "<p>@ADDRRESSEEADDRESSLINE1</p>";
		reportBody1 += "<p>@ADDRRESSEEADDRESSLINE2</p>";
		reportBody1 += "<p>@ADDRRESSEECITY, @STATE</p>";
		reportBody1 += "<p class=\"pb10\">@ADDRRESSEEPINCODE</p>";
		reportBody1 += "<p>Dear Sir/Madam,</p>";
		reportBody1 += "<table style=\"width:100%;\">";
		reportBody1 += "<tr><td style=\"width:50%;text-align:right\">Re: Master Policy No:</td><td>@POLICYNUMBER</td></tr>";
		reportBody1 += "<tr><td style=\"width:50%;text-align:right\">Mode of Exit:</td><td>@MODEOFEXIT</td></tr>";
		reportBody1 += "</table>";
//		reportBody1 += "<p class=\"pt10 pb10\">We are enclosing herewith ___________________(___________________) Cheques in payment of claims due to @MODEOFEXIT of the member from the scheme, as per particulars given below:</p>";
		reportBody1 += "<p class=\"pt10 pb10\">We are crediting  to @FORWARDTO Bank Account No: @ACCOUNTNO of @BANK  ,  IFSC:@IFSC an amount  of  Rs:@AMOUNT as for details below:-</p>";
		reportBody1 += "<table class=\"tableborder\" style=\"width:100%;\">";
		reportBody1 += "<tr><th style=\"width:15%;\">Lic Id</th><th style=\"width:15%;\">Emp No.</th><th style=\"width:25%;\">Name</th><th style=\"width:15%;\" class=\"tdrightalign\">Amount</th><th style=\"width:15%;\">Date</th><th style=\"width:15%;\" class=\"tdrightalign\">Refund of Premium</th></tr>";
		double total = 0;
		int totalInt = 0;
		for (LifeInsuranceDetailDto lifeInsuranceDetailDto : lifeCoverDetailsDto.getGetLifeInsuranceDetailDto()) {
			reportBody1 += "<tr><td>" + lifeInsuranceDetailDto.getLicid() + "</td><td>"
					+ lifeInsuranceDetailDto.getEmpNo() + "</td><td>"
					+ HtmlEncoder.encode(lifeInsuranceDetailDto.getName()) + "</td><td class=\"tdrightalign\">"
					+ lifeInsuranceDetailDto.getAmount().intValue() + "</td><td>" + lifeInsuranceDetailDto.getDate()
					+ "</td><td class=\"tdrightalign\">" + 0 + "</td></tr>";
			total += lifeInsuranceDetailDto.getAmount() > 0d
					? NumericUtils.doubleRoundInMath(lifeInsuranceDetailDto.getAmount(), 0)
					: 0d; // FIXME: add amount here
			totalInt = (int) total;
		}
		reportBody1 += "</table><p class=\"pb10\">&nbsp;</p>";
		reportBody1 += "<table style=\"width:100%\">";
		reportBody1 += "<tr><td style=\"width:55%;\">dated:</td><td style=\"width:20%;\"><p class=\"signaturepadding\">for Rs. "
				+ totalInt + "</p></td></tr>";
		reportBody1 += "<tr><td></td><td></td><td><p class=\"signaturepadding\">Yours faithfully</p></td></tr>";
		reportBody1 += "<tr><td></td><td></td><td>p. Manager(P &amp; GS)</td></tr>";
		reportBody1 += "</table>";

		String reportBody = reportBody1;
		reportBody = reportBody
				.replaceAll("@POLICYNUMBER",
						"P&amp;&amp;GS /" + HtmlEncoder.encode(lifeCoverDetailsDto.getSchemeName()) + "/User/"
								+ lifeCoverDetailsDto.getMasterPolicyNo())
				.replaceAll("@ADDRESSEE",
						lifeCoverDetailsDto.getManagerName() != null
								? HtmlEncoder.encode(lifeCoverDetailsDto.getManagerName())
								: "")
				.replaceAll("@ADDRRESSEEADDRESSLINE1",
						lifeCoverDetailsDto.getAddress1() != null
								? HtmlEncoder.encode(lifeCoverDetailsDto.getAddress1())
								: "")
				.replaceAll("@ADDRRESSEEADDRESSLINE2",
						lifeCoverDetailsDto.getAddress2() != null
								? HtmlEncoder.encode(lifeCoverDetailsDto.getAddress2())
								: "")
				.replaceAll("@ADDRRESSEECITY",
						lifeCoverDetailsDto.getCity() != null ? HtmlEncoder.encode(lifeCoverDetailsDto.getCity()) : "")
				.replaceAll("@STATE",
						lifeCoverDetailsDto.getState() != null ? HtmlEncoder.encode(lifeCoverDetailsDto.getState())
								: "")
				.replaceAll("@ADDRRESSEEPINCODE",
						lifeCoverDetailsDto.getPincode() != null ? lifeCoverDetailsDto.getPincode() : "")
				.replaceAll("@MODEOFEXIT", "WithDrawal")
				.replaceAll("@DATE", lifeCoverDetailsDto.getDated() != null ? lifeCoverDetailsDto.getDated() : "")
				.replaceAll("@AMOUNT",
						NumericUtils.convertDoubleToString(
								lifeCoverDetailsDto.getGetLifeInsuranceDetailDto().get(0).getAmount()))
				.replaceAll("@ACCOUNTNO", lifeCoverDetailsDto.getAccountNo())
				.replaceAll("@BANK", HtmlEncoder.encode(lifeCoverDetailsDto.getBankName()))
				.replaceAll("@FORWARDTO",
						forwardTo.equalsIgnoreCase("member") ? lifeCoverDetailsDto.getMphName() : "your")
				.replaceAll("@IFSC", lifeCoverDetailsDto.getIfscCode());

		return reportBody.replace("\\", "").replaceAll("\t", "");
	}

	private String withdrawalLetterStyles() {
		return ""
				+ " .tableborder tr th { border-top-width:1pt;border-bottom-width:1pt;border-top-style:dashed;border-bottom-style:dashed;border-color:#000;padding:5pt;border-spacing:0pt;border-collapse: collapse; }"
				+ " .tableborder tr:last-child td { border-bottom-width:1pt;border-bottom-style:dashed;border-color:#000;padding:5pt;border-spacing:0pt;border-collapse: collapse; }"
				+ " .tableborder tr td { padding:5pt; }" + " .tdrightalign { text-align:right; } "
				+ " .signaturepadding { padding-bottom: 22pt; }";
	}

	public LifeCoverDetailsDto getwithdrawalLetter(String payoutNo, String reportType, String forwardTo)
			throws MalformedURLException, ProtocolException, IOException {
		LifeCoverDetailsDto lifeCoverDetailsDto = new LifeCoverDetailsDto();
		PayoutEntity payoutEntity = payoutRepository.findByPayoutNoAndStatusAndIsActiveTrue(payoutNo,
				PayoutStatus.APPROVE.val());
		Object policyMaster = policyMasterRepository.fetchPolicyDetaislBypolicyNumber(payoutEntity.getMasterPolicyNo());
		if (policyMaster != null) {
			Object[] obj = (Object[]) policyMaster;
			JsonNode response = integrationService
					.getProductDetailsByProductId(NumericUtils.stringToLong(String.valueOf(obj[5])));
			if (response != null) {
				JsonNode proposeDetails = response.path("responseData");
				String product = proposeDetails.path("productCode").textValue();
				lifeCoverDetailsDto.setSchemeName(product);
			}

			// mphAddressDetails
			String mphName = "";
			String mphAddressOne = "";
			String mphAddressTwo = "";
			String mphAddressThree = "";
			String mphCity = "";
			String mphState = "";

			Object mphDetails = mphMasterRepository.findMphDetails(NumericUtils.stringToLong(String.valueOf(obj[25])));
			if (mphDetails != null) {
				Object[] objMph = (Object[]) mphDetails;
				lifeCoverDetailsDto.setManagerName(String.valueOf(objMph[14]));
				lifeCoverDetailsDto.setMphName(String.valueOf(objMph[14]));
			}
			if (forwardTo.equalsIgnoreCase("Mph")) {

				Set<MphAddressEntity> mphAddressList = mphAddressRepository
						.findAllByMphIdAndIsActive(NumericUtils.stringToLong(String.valueOf(obj[25])), Boolean.TRUE);

//			lifeCoverDetailsDto.setManagerName(mphDetails.getMphName());
//			Set<MphAddressEntity> mphAddressList=mphDetails.getMphAddress(); 

//			Object[] objs = (Object[]) policyMaster;
//			JsonNode proposalObject = integrationService.getProposalDetailsByProposalNumber(String.valueOf(objs[24]));
//			JsonNode mphBasic = proposalObject.path("responseData").path("mphDetails").path("mphBasicDetails");
//			JsonNode mphAddress = proposalObject.path("responseData").path("mphDetails").path("mphBasicDetails")
//					.path("mphAddressDetails").get(0);
//			String trustId = mphBasic.path("mphCode").textValue();
//			if (trustId != null && mphAddress != null) {
//				trustName = mphBasic.path("mphName").textValue();
//				trustAddressOne = mphAddress.path("address1").textValue();
//				trustAddressTwo = mphAddress.path("address2").textValue();
//				trustAddressThree = mphAddress.path("address3").textValue();
//				trustCity = mphAddress.path("city").textValue();
//				trustState = mphAddress.path("state").textValue();
//
				if (!mphAddressList.isEmpty()) {
					for (MphAddressEntity mphAddress : mphAddressList) {
						lifeCoverDetailsDto.setAddress1(mphAddress.getAddressLine1());
						lifeCoverDetailsDto.setAddress2(mphAddress.getAddressLine2());
						lifeCoverDetailsDto.setAddress3(mphAddress.getAddressLine3());
						lifeCoverDetailsDto.setCity(mphAddress.getCityId());
						lifeCoverDetailsDto.setState(mphAddress.getStateName());
						lifeCoverDetailsDto.setPincode(NumericUtils.convertIntegerToString(mphAddress.getPincode()));
						lifeCoverDetailsDto.setDated(DateUtils.dateToHypenStringDDMMYYYY(payoutEntity.getModifiedOn()));
					}
				} else {
					lifeCoverDetailsDto.setAddress1("No Address Details");
					lifeCoverDetailsDto.setAddress2(" ");
					lifeCoverDetailsDto.setAddress3(" ");
					lifeCoverDetailsDto.setCity(" ");
					lifeCoverDetailsDto.setState(" ");
					lifeCoverDetailsDto.setPincode(" ");
				}
			}

//				
//			}
		}

//		if(reportType.equals("withdrawalLetter")) {

		List<LifeInsuranceDetailDto> dtoList = new ArrayList<LifeInsuranceDetailDto>();
		LifeInsuranceDetailDto lifeInsuranceDetailDto = new LifeInsuranceDetailDto();
		PayoutMbrEntity payoutMbrEntity = payoutEntity.getPayoutMbr();
		PayoutCommutationCalcEntity payoutCommutationCalcEntity = !payoutMbrEntity.getPayoutCommutationCalc().isEmpty()
				? payoutMbrEntity.getPayoutCommutationCalc().get(0)
				: null;
		lifeCoverDetailsDto.setModeofExit(payoutEntity.getModeOfExit());
		lifeCoverDetailsDto.setDated(DateUtils.dateToHypenStringDDMMYYYY(payoutEntity.getCreatedOn()));
		lifeCoverDetailsDto.setMasterPolicyNo(payoutEntity.getMasterPolicyNo());
		lifeInsuranceDetailDto.setEmpNo(payoutMbrEntity.getMemberShipId());
		lifeInsuranceDetailDto.setLicid(payoutMbrEntity.getLicId());
		lifeInsuranceDetailDto.setDate(DateUtils.dateToHypenStringDDMMYYYY(payoutEntity.getCreatedOn()));
		lifeInsuranceDetailDto.setAmount(payoutCommutationCalcEntity.getCommutationAmt() > 0.0
				? NumericUtils.doubleRoundInMath(payoutCommutationCalcEntity.getCommutationAmt(), 0)
				: 0d);
		lifeInsuranceDetailDto.setName(payoutMbrEntity.getFirstName());

		dtoList.add(lifeInsuranceDetailDto);
		if (!payoutMbrEntity.getPayoutPayeeBank().isEmpty()) {
			PayoutPayeeBankDetailsEntity mbrBank = payoutMbrEntity.getPayoutPayeeBank().get(0);
			lifeCoverDetailsDto.setBankName(mbrBank.getBankName());
			lifeCoverDetailsDto.setAccountNo(mbrBank.getAccountNumber());
			lifeCoverDetailsDto.setIfscCode(mbrBank.getIfscCode());
		} else {
			lifeCoverDetailsDto.setBankName("No Bank Details");
			lifeCoverDetailsDto.setAccountNo("No Account Details");
			lifeCoverDetailsDto.setIfscCode("No Account Details");

		}
		lifeCoverDetailsDto.setGetLifeInsuranceDetailDto(dtoList);

		/*** Note:- withdrawalLetter to member ***/
		if (forwardTo.equalsIgnoreCase("Member")) {
			LifeCoverDetailsDto lifeCoverDetailsDtoTomember = new LifeCoverDetailsDto();
			lifeCoverDetailsDtoTomember = modelMapper.map(lifeCoverDetailsDto, LifeCoverDetailsDto.class);

			lifeCoverDetailsDtoTomember.setManagerName(payoutMbrEntity.getFirstName());
			if (!payoutMbrEntity.getPayoutMbrAddresses().isEmpty()) {
				PayoutMbrAddressEntity payoutMbrAddressEntity = payoutMbrEntity.getPayoutMbrAddresses().get(0);
				lifeCoverDetailsDtoTomember.setAddress1(payoutMbrAddressEntity.getAddressLineOne());
				lifeCoverDetailsDtoTomember.setAddress2(payoutMbrAddressEntity.getAddressLineTwo());
				lifeCoverDetailsDtoTomember.setAddress3(payoutMbrAddressEntity.getAddressLineThree());
				lifeCoverDetailsDtoTomember.setCity(payoutMbrAddressEntity.getCity());
				lifeCoverDetailsDtoTomember.setState(payoutMbrAddressEntity.getState());
				lifeCoverDetailsDtoTomember
						.setPincode(NumericUtils.convertIntegerToString(payoutMbrAddressEntity.getPinCode()));
				lifeCoverDetailsDtoTomember
						.setDated(DateUtils.dateToHypenStringDDMMYYYY(payoutMbrAddressEntity.getModifiedOn()));
			} else {
				lifeCoverDetailsDtoTomember.setAddress1("No Address Details");
				lifeCoverDetailsDtoTomember.setAddress2("");
				lifeCoverDetailsDtoTomember.setAddress3(" ");
				lifeCoverDetailsDtoTomember.setCity(" ");
				lifeCoverDetailsDtoTomember.setState(" ");
				lifeCoverDetailsDtoTomember.setPincode(" ");
			}
		}

//		}
		return lifeCoverDetailsDto;

	}

	private SpPaymentPdfDto getPayoutVocher(String payoutNo, String reportType)
			throws MalformedURLException, ProtocolException, IOException {
		SpPaymentPdfDto spPaymentPdfDto = new SpPaymentPdfDto();

		PayoutEntity payoutEntity = payoutRepository.findByPayoutNoAndStatusAndIsActiveTrue(payoutNo,
				PayoutStatus.APPROVE.val());
		spPaymentPdfDto.setUnitName(getUnitNameByUnitCode(payoutEntity.getUnitCode()));
		spPaymentPdfDto.setUnitCode(payoutEntity.getUnitCode());
		spPaymentPdfDto.setMphName(payoutEntity.getMphName());
		PayoutMbrEntity payoutMbrEntity = payoutEntity.getPayoutMbr();
		PayoutFundValueEntity payoutFundValueEntity = !payoutMbrEntity.getPayoutFundValue().isEmpty()
				? payoutMbrEntity.getPayoutFundValue().get(0)
				: null;
		List<PayoutCommutationCalcEntity> payoutCommutationCalcEntityList = !payoutMbrEntity.getPayoutCommutationCalc()
				.isEmpty() ? payoutMbrEntity.getPayoutCommutationCalc() : new ArrayList<>();
		PayoutCommutationCalcEntity payoutCommutationCalcEntity = payoutMbrEntity.getPayoutCommutationCalc().get(0);

		Object policyMaster = policyMasterRepository.fetchPolicyDetaislBypolicyNumber(payoutEntity.getMasterPolicyNo());

		List<AdjustmentVoucherDetailDto> headOfAccount = new ArrayList<>();
		spPaymentPdfDto.setDivisionalName(reportType);
		spPaymentPdfDto.setPreparedBy(payoutEntity.getCreatedBy());
		spPaymentPdfDto.setSchemeName(payoutEntity.getProduct());
		spPaymentPdfDto.setPolicyNumber(payoutEntity.getMasterPolicyNo());
		if (reportType.equals("payoutVoucherDeath")) {
			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto = new AdjustmentVoucherDetailDto();
			if (payoutEntity.getModeOfExit() == 1) {
				Double totalCommutationAmount = 0d;
				Double totalNetAmount = 0d;
				Double totalTdsAmount = 0d;
				for (PayoutCommutationCalcEntity entity : payoutCommutationCalcEntityList) {
					totalCommutationAmount += entity.getCommutationAmt();
					if (entity.getTdsApplicable().equals(true)) {
						totalTdsAmount += entity.getTdsAmount();
					}
					totalNetAmount += entity.getNetAmount();
				}
				adjustmentVoucherDetailDto.setHeadOfAccount("O/s Death Claim Paid- Commuted Value");
				adjustmentVoucherDetailDto.setCode("1205");
				adjustmentVoucherDetailDto.setCreditAmount("-");
				adjustmentVoucherDetailDto.setDebitAmount(totalCommutationAmount != null
						? NumericUtils.convertDoubleToString(NumericUtils.doubleRoundInMath(totalCommutationAmount, 0))
						: "-");

				headOfAccount.add(adjustmentVoucherDetailDto);
				AdjustmentVoucherDetailDto adjustmentVoucherDetailDto1 = new AdjustmentVoucherDetailDto();

				if (payoutCommutationCalcEntity != null && payoutCommutationCalcEntity.getAmtPayableTo() != null) {
					if (commutationPayable(NumericUtils.stringToInteger(payoutCommutationCalcEntity.getAmtPayableTo()))
							.equals(ClaimConstants.MPH)) {
						spPaymentPdfDto.setFavouring(payoutEntity.getMphName());
					} else if (commutationPayable(
							NumericUtils.stringToInteger(payoutCommutationCalcEntity.getAmtPayableTo()))
							.equals(ClaimConstants.NOMINEE)) {
						PayoutMbrNomineeEntity payoutMbrNomineeEntity = payoutMbrEntity.getPayoutMbrNomineeDtls()
								.get(0);

						spPaymentPdfDto.setFavouring(payoutMbrNomineeEntity.getFirstName());
					} else {
						spPaymentPdfDto.setFavouring(payoutEntity.getMphName());
					}
				} else {
					spPaymentPdfDto.setFavouring(payoutMbrEntity.getPayoutMbrNomineeDtls().get(0).getFirstName());
				}

				adjustmentVoucherDetailDto1.setHeadOfAccount("Bank Account No IV");
				adjustmentVoucherDetailDto1.setCode("2203");
				adjustmentVoucherDetailDto1.setCreditAmount(totalNetAmount != null
						? NumericUtils.convertDoubleToString(NumericUtils.doubleRoundInMath(totalNetAmount, 0))
						: "-");
				adjustmentVoucherDetailDto1.setDebitAmount("-");
				spPaymentPdfDto.setAmountInWords(
						NumericUtils.convertAmountToWord((totalNetAmount != null)
								? totalNetAmount.longValue()
								: 0L) + "  " + "Only");
				headOfAccount.add(adjustmentVoucherDetailDto1);
				if (totalTdsAmount > 0d) {
					AdjustmentVoucherDetailDto adjustmentVoucherDetailDto2 = new AdjustmentVoucherDetailDto();
					adjustmentVoucherDetailDto2.setHeadOfAccount("Income Tax Deduction from payment");
					adjustmentVoucherDetailDto2.setCode("1107");
					adjustmentVoucherDetailDto2.setCreditAmount(totalTdsAmount != null
							? NumericUtils.convertDoubleToString(NumericUtils.doubleRoundInMath(totalTdsAmount, 0))
							: "-");
					adjustmentVoucherDetailDto2.setDebitAmount("-");
					headOfAccount.add(adjustmentVoucherDetailDto2);
				}
			}

		} else if (reportType.equals("payoutVoucherWithdrawal")) {
			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto = new AdjustmentVoucherDetailDto();
			if (payoutEntity.getModeOfExit() == 4) {
				Double totalCommutationAmount = 0d;
				Double totalTdsAmount = 0d;
				Double totalNetAmount = 0d;
				Double existLoad = 0d;
				Double totalValue = 0d;
				for (PayoutCommutationCalcEntity entity : payoutCommutationCalcEntityList) {
					totalCommutationAmount += entity.getCommutationAmt();
//						if (entity.getTdsApplicable().equals(true)) {
//							totalTdsAmount += entity.getTdsAmount();
//						}
					existLoad = entity.getExitLoad();
					totalNetAmount += entity.getNetAmount();
				}
				adjustmentVoucherDetailDto.setHeadOfAccount("Outstanding withdrawal benefits paid");
				adjustmentVoucherDetailDto.setCode("1254");
				adjustmentVoucherDetailDto.setCreditAmount("-");

				if (payoutEntity.getPolicyType().equals(ClaimConstants.DB)) {
					adjustmentVoucherDetailDto.setDebitAmount((payoutFundValueEntity.getPurchasePrice() > 0d
							|| payoutFundValueEntity.getPurchasePrice() != null)
									? NumericUtils.convertDoubleToString(
											NumericUtils.doubleRoundInMath(payoutFundValueEntity.getPurchasePrice(), 0))
									: NumericUtils.convertDoubleToString(
											NumericUtils.doubleRoundInMath(payoutFundValueEntity.getPension(), 0)));
					totalValue = (payoutFundValueEntity.getPurchasePrice() > 0d
							|| payoutFundValueEntity.getPurchasePrice() != null)
									? NumericUtils.doubleRoundInMath(payoutFundValueEntity.getPurchasePrice(), 0)
									: NumericUtils.doubleRoundInMath(payoutFundValueEntity.getPension(), 0);
				} else {
					adjustmentVoucherDetailDto.setDebitAmount(NumericUtils.convertDoubleToString(
							NumericUtils.doubleRoundInMath(payoutFundValueEntity.getFundValue(), 0)));
					totalValue = (payoutFundValueEntity.getFundValue() > 0d
							|| payoutFundValueEntity.getFundValue() != null)
									? NumericUtils.doubleRoundInMath(payoutFundValueEntity.getFundValue(), 0)
									: NumericUtils.doubleRoundInMath(payoutFundValueEntity.getFundValue(), 0);
				}

				headOfAccount.add(adjustmentVoucherDetailDto);
				spPaymentPdfDto.setFavouring(payoutEntity.getMphName());
				AdjustmentVoucherDetailDto adjustmentVoucherDetailDto1 = new AdjustmentVoucherDetailDto();
				adjustmentVoucherDetailDto1.setHeadOfAccount("Bank Account No IV");
				adjustmentVoucherDetailDto1.setCode("2203");
				adjustmentVoucherDetailDto1.setDebitAmount("-");
				adjustmentVoucherDetailDto1.setCreditAmount(totalCommutationAmount > 0d
						? NumericUtils.convertDoubleToString(NumericUtils.doubleRoundInMath(totalCommutationAmount, 0))
						: "-");
				spPaymentPdfDto
						.setAmountInWords(NumericUtils
								.convertAmountToWord(totalCommutationAmount > 0 ? totalCommutationAmount.longValue() : 0L)
								+ "  " + "Only");
				headOfAccount.add(adjustmentVoucherDetailDto1);

				AdjustmentVoucherDetailDto adjustmentVoucherDetailDto2 = new AdjustmentVoucherDetailDto();
				adjustmentVoucherDetailDto2.setHeadOfAccount("Exit load");
				adjustmentVoucherDetailDto2.setCode("4125");
				adjustmentVoucherDetailDto2.setCreditAmount(existLoad > 0d
						? NumericUtils.convertDoubleToString(NumericUtils.doubleRoundInMath(existLoad, 0))
						: "-");
				adjustmentVoucherDetailDto2.setDebitAmount("-");
				headOfAccount.add(adjustmentVoucherDetailDto2);

			}
		} else {
			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto = new AdjustmentVoucherDetailDto();
			Double totalCommutationAmount = 0d;
			Double totalTdsAmount = 0d;
			Double totalNetAmount = 0d;
			String amountPayableTo = "";
			for (PayoutCommutationCalcEntity entity : payoutCommutationCalcEntityList) {
				totalCommutationAmount += entity.getCommutationAmt();
				if (entity.getTdsApplicable().equals(true)) {
					totalTdsAmount += entity.getTdsAmount();
				}
				amountPayableTo = entity.getAmtPayableTo() != null ? entity.getAmtPayableTo() : "2";
				totalNetAmount += entity.getNetAmount();
			}
			adjustmentVoucherDetailDto.setHeadOfAccount("O/s Maturity Claims Paid-" + spPaymentPdfDto.getSchemeName());
			adjustmentVoucherDetailDto.setCode("1232");
			adjustmentVoucherDetailDto.setCreditAmount("-");
			adjustmentVoucherDetailDto.setDebitAmount(totalCommutationAmount > 0d
					? NumericUtils.convertDoubleToString(NumericUtils.doubleRoundInMath(totalCommutationAmount, 0))
					: "-");

			amountPayablToResignAndRetriment(payoutEntity.getMphCode(), null,
					commutationPayable(NumericUtils.stringToInteger(amountPayableTo)), payoutEntity, spPaymentPdfDto);
			headOfAccount.add(adjustmentVoucherDetailDto);
			AdjustmentVoucherDetailDto adjustmentVoucherDetailDto1 = new AdjustmentVoucherDetailDto();
			adjustmentVoucherDetailDto1.setHeadOfAccount("Bank Account No IV");
			adjustmentVoucherDetailDto1.setCode("2203");
			adjustmentVoucherDetailDto1.setCreditAmount(totalNetAmount > 0d
					? NumericUtils.convertDoubleToString(NumericUtils.doubleRoundInMath(totalNetAmount, 0))
					: "-");
			spPaymentPdfDto.setAmountInWords(NumericUtils.convertAmountToWord(totalNetAmount > 0
					? totalNetAmount.longValue()
					: 0L) + "  " + "Only");
			adjustmentVoucherDetailDto1.setDebitAmount("-");
			headOfAccount.add(adjustmentVoucherDetailDto1);
			if (totalTdsAmount > 0d) {
				AdjustmentVoucherDetailDto adjustmentVoucherDetailDto2 = new AdjustmentVoucherDetailDto();
				adjustmentVoucherDetailDto2.setHeadOfAccount("Income Tax Deduction from payment");
				adjustmentVoucherDetailDto2.setCode("1107");
				adjustmentVoucherDetailDto2.setCreditAmount(totalTdsAmount > 0d
						? NumericUtils.convertDoubleToString(NumericUtils.doubleRoundInMath(totalTdsAmount, 0))
						: "-");
				adjustmentVoucherDetailDto2.setDebitAmount("-");
				headOfAccount.add(adjustmentVoucherDetailDto2);
			}

		}
		spPaymentPdfDto.setVoucherDetail(headOfAccount);
		spPaymentPdfDto.setDivisionalName(payoutMbrEntity.getFirstName());
		spPaymentPdfDto.setPreparedBy(payoutEntity.getCreatedBy());
		if (policyMaster != null) {
			Object[] obj = (Object[]) policyMaster;
			JsonNode response = integrationService
					.getProductDetailsByProductId(NumericUtils.stringToLong(String.valueOf(obj[5])));
			if (response != null) {
				JsonNode proposeDetails = response.path("responseData");
				String product = proposeDetails.path("productCode").textValue();
				spPaymentPdfDto.setSchemeName(product);
			}
		}
		
//			spPaymentPdfDto.setFavouring(payoutMbrEntity.getFirstName());
		spPaymentPdfDto.setVoucherNumber(payoutEntity.getPayoutNo());
		spPaymentPdfDto.setPreparedBy(payoutEntity.getCreatedBy());
		spPaymentPdfDto.setVoucherDate(DateUtils.dateToHypenStringDDMMYYYY(payoutEntity.getModifiedOn()));

//			// mphDetails
//			if (policyMaster != null) {
//				Object[] obj = (Object[]) policyMaster;
//				JsonNode proposalObject = integrationService.getProposalDetailsByProposalNumber(String.valueOf(obj[24]));
//				JsonNode mphBasic = proposalObject.path("responseData").path("mphDetails").path("mphBasicDetails");
//				JsonNode mphAddress = proposalObject.path("responseData").path("mphDetails").path("mphBasicDetails")
//						.path("mphAddressDetails").get(0);
//				String trustId = mphBasic.path("mphCode").textValue();
//				if (trustId != null && mphAddress != null) {
//					String mphName = mphBasic.path("mphName").textValue();
//					spPaymentPdfDto.setFavouring(payoutMbrEntity.getFirstName());
//				}
//			}

		return spPaymentPdfDto;
	}

	/*** Note:-getunitDetailsByuniyCode ***/

	private String getUnitNameByUnitCode(String unitCode) {
		ResponseDto responseDto = accountingIntegrationService.commonmasterserviceUnitByCode(unitCode);
		if (responseDto != null) {
			return responseDto.getCityName();
		} else {
			return "No unitName";
		}
	}

	public String modeOfExist(Integer modeOfExist) {

		switch (modeOfExist) {
		case 1:
			return "DEATH";
		case 2:
			return "RETIREMENT";
		case 3:
			return "RESIGNATION";
		case 4:
			return "WITHDRAWAL";
		default:
			return "code to be executed if all cases are not matched";
		}

	}

	public static String commutationPayable(Integer payableId) {

		switch (payableId) {
		case 1:
			return "MPH";
		case 2:
			return "MEMBER";
		case 3:
			return "NOMINEE";

		default:
			return "code to be executed if all cases are not matched";
		}
	}

	@Override
	public String premiumreceipt(Long masterpolicyId, String reportType) throws IOException {

		InputStream islicLogo = new ClassPathResource("liclogo.png").getInputStream();
		// Uncomment below line to add watermark to the pdf
		// InputStream islicWatermark = new
		// ClassPathResource("watermark.png").getInputStream();

		byte[] bytesLogo = IOUtils.toByteArray(islicLogo);
		// Uncomment below line to add watermark to the pdf
		// byte[] bytesWatermark = IOUtils.toByteArray(islicWatermark);

		String licLogo = Base64.getEncoder().encodeToString(bytesLogo);
		// Uncomment below line to add watermark to the pdf
		// String licWatermark = Base64.getEncoder().encodeToString(bytesWatermark);
//		List<AgeValuationReportDto> ageValuationReportDto = new ArrayList<AgeValuationReportDto>();
//		List<Object[]> getAgeReport = policyRepository.getAgeReport(masterpolicyId);

//		ageValuationReportDto = PolicyHelper.valuationObjtoDto(getAgeReport);
		boolean showLogo = true;
		String reportBody = "";
		String reportStyles = "";
		String htmlContent2 = "</div></body></html>";

		reportBody = premiumadjustmentvoucher(masterpolicyId, reportType) + htmlContent2;

		String completehtmlContent = "<!DOCTYPE  html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">"
				+ "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>"
				+ "<title>LIC ePGS</title>" + "<meta name=\"author\" content=\"LIC PNGS\"/>"
				+ "<meta name=\"keywords\" content=\"Customer Communication\"/>"
				+ "<meta name=\"description\" content=\"Report/Letter/Contract\"/>" + "<style type=\"text/css\"> "
				+ "body{border-width:0px;\r\n" + "border-style:solid;\r\n" + "border-color:#586ec5;} "
				+ "* {margin:0; padding:0; text-indent:0; }"
				+ ".s1 { color: #2E5396; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 16pt; }"
				+ ".s2 { color: #2E3599; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ "p { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 10pt; margin:0pt; }"
				+ ".a { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ ".s3 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ ".s5 { color: #2E3599; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ ".s6 { color: #120000; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ ".s7 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ ".s8 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; vertical-align: -2pt; }"
				+ ".s9 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ "h1 { color: #120000; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ ".s10 { color: #2D3499; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ ".pt10 { padding-top: 10pt; }" + " .pb10 { padding-bottom: 10pt; }"
				+ ".pb50 { padding-bottom: 50pt; }"
				+ "table, tbody, td {vertical-align: top; overflow: visible; color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 10pt; margin:0pt; } "
				// Uncomment below line to add watermark to the pdf
				// + " .watermark {\r\n background-image: url(\"data:image/png;base64,
				// licWatermark + "\");}"
				+ " " + reportStyles + " " + " </style></head><body><div id=\"bg\" class=\"watermark\">"
				+ "<p style=\"padding-left: 5pt;text-indent: 0pt;text-align: left;\"/>";

		if (showLogo) {
			completehtmlContent = completehtmlContent + "<p style=\"text-indent: 0pt;text-align: center;\">" + "<span>"
					+ "<table style=\"width:100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
					+ "<tr style=\"display:flex;justify-content:space-between;align-items:center\">"
					+ "<td style=\"padding-left:24pt\"><img width=\"100\" height=\"63\" src=\"data:image/jpg;base64,"
					+ licLogo + "\"/></td>" + "</tr>" + "</table>" + "</span>" + "</p>";
		}
		completehtmlContent = completehtmlContent + "<p style=\"text-indent: 0pt;text-align: left;\"><br/></p>"
				+ reportBody + "";

		String htmlFileLoc = tempPdfLocation + "/" + masterpolicyId + ".html";
		FileWriter fw = new FileWriter(htmlFileLoc);
		fw.write(completehtmlContent);
		fw.close();

		FileOutputStream fileOutputStream = new FileOutputStream(tempPdfLocation + "/" + masterpolicyId + ".pdf");
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(new File(htmlFileLoc));
		renderer.layout();
		try {
			renderer.createPDF(fileOutputStream, false);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		renderer.finishPDF();

		if (new File(htmlFileLoc).exists()) {
			new File(htmlFileLoc).delete();
		}

		return tempPdfLocation + "/" + masterpolicyId + ".pdf";
	}

	private String premiumadjustmentvoucher(Long masterpolicyId, String reportType) throws MalformedURLException, ProtocolException, IOException {

		PremiumAdjustmentVoucherDto premiumAdjustmentVoucherDto = generatepremiumadjustmentvoucher(masterpolicyId,
				reportType);

		String reportBody = 
				  "<p style=\"text-align:center;\">PENSION AND GROUP SCHEME DEPARTMENT</p>" 
			    + "<p>&nbsp;</p>"
						  
			    + "<div style=\"width: 200%;\">"
			    
			    + "<div style=\"width: 50%;\">"
				+ "<table style=\"width: 100%;\">" 
			    + "<tr>" 
				+ "<td>Date of Adjustment: @DATEOFADJUSTMENT </td>"
				+ "<td style=\" text-align: right;\">Servicing Unit: @SERVICINGUNIT </td>" 
				+ "</tr>" 
				+ "</table>"
			    + "</div>"
				
			    + "<div style=\"width: 50%; \">"
			    + "<table style=\"width: 100%;\">" 
				+ "<tr>" 
				+ "<td>Adjustment No: @ADJUSTMENTNUMBER </td>"
				+ "<td style=\" text-align: right;\">Address of Unit:@ADDRESSOFUNIT </td>" 
				+ "</tr>" 
				+ "</table>"
			    + "</div>"
			    
			    + "</div>"
				
			    + "<div style=\"width: 50%;\">"
				+ "<table style=\"width: 100%;\">" 
				+ "<tr>"
				+ "<td  style=\" text-align: left;\">Unit GST Number: @UNITGSTNUMBER </td>"
				+ "</tr>"
				+ "</table>" 
				+ "</div>"
		
				
				+ "<p style=\"text-align:center;\">Final Premium Receipt</p>"
				
				+ "<table style=\"width: 100%;\">" 
				+ "<tr>"
				+ "<td style=\" text-align: left; width: 50%;\">MPH Name: @MPHNAME </td>"
				+ "<td style=\" text-align: right; width: 50%;\">Mode of Collection of Deposit: @MODEOFDEPOSIT</td>"
//				+ "class=\"alignRight\">Mode of Collection of Deposit: @MODEOFDEPOSIT</td>" 
				+ "</tr>" 
				+ "</table>"
				
				+ "<p>&nbsp;</p>";

		String table = 
				  "<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
				+ "<tr style=\"text-align: center; vertical-align: middle;\">"
						  
				+ "<td style=\"text-align: center; width=16.70%\">Policy no.</td>" 
				+ "<td style=\"text-align: center; width=16.70%\">@POLICYNUMBER</td>"
				
				+ "<td style=\"text-align: center; width=16.70%\">UIN</td>" 
				+ "<td style=\"text-align: center; width=16.70%\">@UIN</td>"
				
				+ "<td style=\"text-align: center; width=16.70%\">Name of Plan</td>" 
				+ "<td style=\"text-align: center; width=16.70%\">@NAMEOFPLAN</td>"
				
				+ "</tr>" 
				+ "</table>" 
				+ "<p>&nbsp;</p>";

		String table1 = 
				  "<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
				
				+ "<tr style=\"text-align: center; vertical-align: middle;\">"
				+ "<td style=\"width=16.70%\">Deposit Nos</td>" 
				+ "<td style=\"width=16.70%\">Deposit Date</td>"
				+ "<td style=\"width=16.70%\">Deposit Amount</td>"
				+ "<td style=\"width=16.70%\">Deposit Nos.</td>" 
				+ "<td style=\"width=16.70%\">Deposit Date</td>"
				+ "<td style=\"width=16.70%\">Deposit Amount</td>"
				+ "</tr>" 
				
				+ "<tr>"
				+ "<td style=\"width=16.70%\"> 1.</td>"
				+ "<td style=\"width=16.70%\">@DEPOSITDATE</td>"
				+ "<td style=\"width=16.70%\">@DEPOSITAMOUNT </td>"

				+ "<td style=\"width=16.70%\">4.</td>" 
				+ "<td style=\"width=16.70%\"></td>"
				+ "<td style=\"width=16.70%\"></td>"
				+ "</tr>" 
				
				+ "<tr>"
				+ "<td style=\"width=16.70%\">2.</td>" 
				+ "<td style=\"width=16.70%\"></td>"
				+ "<td style=\"width=16.70%\"></td>"

				+ "<td style=\"width=16.70%\">5.</td>" 
				+ "<td style=\"width=16.70%\"></td>"
				+ "<td style=\"width=16.70%\"></td>"
				+ "</tr>" 
				
				+ "<tr>"
				+ "<td style=\"width=16.70%\">3.</td>" 
				+ "<td style=\"width=16.70%\"></td>"
				+ "<td style=\"width=16.70%\"></td>"

				+ "<td style=\"width=16.70%\">6.</td>" 
				+ "<td style=\"width=16.70%\"></td>"
				+ "<td style=\"width=16.70%\"></td>"
				+ "</tr>" 
				
				+ "</table>" 
				+ "<p>&nbsp;</p>";

		String reportBody1 = 
				  "<div style=\"width: 200%;\">"
				+ "<table style=\"width: 50%;\">"
				+ "<tr>"
				+ "<td  style=\"text-align: left;\">Annual Renewal Date:@ANNUALRENEWALDATE </td>"
				+ "<td  style=\"text-align: right;\">Premium Adjustment For:@PREMIUMADJUSTMENTFOR </td>" 
				+ "</tr>"
				+ "</table>" 
				
				+ "<table style=\"width: 50%;\">" 
				+ "<tr>"
				+ "<td style=\"text-align: left;\">Next Premium Due:@NEXTPREMIUMDUE </td>"
				+ "<td style=\"text-align: right;\">Premium Mode:@FREQUENCY</td>" 
				+ "</tr>" 
				+ "</table>"
				+ "</div>";

		String table3 = 
				"<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
				
				+ "<tr style=\"text-align: center; vertical-align: middle;\">"
				+ "<td style=\"width=20%\">Description</td>"
				+ "<td style=\"width=20%\">PREMIUM </td>"
				+ "<td style=\"width=60%\" colspan=\"5\"> GST </td>" 
				+ "</tr>"
				
				+ "<tr style=\"text-align: center; vertical-align: middle;\">" 
				+ "<td style=\"width=20%\"></td>"
				+ "<td style=\"width=20%\"></td>" 
				+ "<td style=\"width=12%\">CGST</td>"
				+ "<td style=\"width=12%\">SGST</td>" 
				+ "<td style=\"width=12%\">UGST</td>"
				+ "<td style=\"width=12%\">IGST</td>" 
				+ "<td style=\"width=12%\">TOTAL</td>" 
				+ "</tr>"
				
				+ "<tr style=\"text-align: center; vertical-align: middle;\">"
				+ "<td style=\"text-align: left; width=20%\">&nbsp; FUNDED </td>" 
				+ "<td style=\"width=20%\">@FUNDED</td>"
				+ "<td style=\"width=12%\"></td>" 
				+ "<td style=\"width=12%\"></td>"
				+ "<td style=\"width=12%\"></td>" 
				+ "<td style=\"width=12%\"></td>"
				+ "<td style=\"width=12%\"></td>" 
				+ "</tr>"
				
				+ "<tr style=\"text-align: center; vertical-align: middle;\">"
				+ "<td style=\"text-align: left; width=20%\">&nbsp; TERM INSURANCE</td>" 
				+ "<td style=\"width=20%\">@TERMINSURANCE</td>"
				+ "<td style=\"width=12%\"></td>" 
				+ "<td style=\"width=12%\"></td>"
				+ "<td style=\"width=12%\"></td>" 
				+ "<td style=\"width=12%\"></td>"
				+ "<td style=\"width=12%\"></td>" 
				+ "</tr>"
				
				+ "<tr style=\"text-align: center; vertical-align: middle;\">"
				+ "<td style=\"text-align: left; width=20%\">&nbsp; ANNUITY PURCHASE</td>" 
				+ "<td style=\"width=20%\">@ANNUITYPURCHASE</td>"
				+ "<td style=\"width=12%\"></td>"
				+ "<td style=\"width=12%\"></td>"
				+ "<td style=\"width=12%\"></td>"
				+ "<td style=\"width=12%\"></td>"
				+ "<td style=\"width=12%\"></td>" 
				+ "</tr>"
				
				+ "<tr style=\"text-align: center; vertical-align: middle;\">"
				+ "<td style=\"text-align: left; width=20%\">&nbsp; LATE FEE ON PREMIUM</td>" 
				+ "<td style=\"width=20%\">@LATEFEEONPREMIUM</td>"
				+ "<td style=\"width=12%\"></td>"
				+ "<td style=\"width=12%\"></td>"
				+ "<td style=\"width=12%\"></td>"
				+ "<td style=\"width=12%\"></td>"
				+ "<td style=\"width=12%\"></td>" 
				+ "</tr>"
				
				+ "<tr style=\"text-align: center; vertical-align: middle;\">" 
				+ "<td style=\"text-align: left; width=20%\">&nbsp; OTHERS </td>"
				+ "<td style=\"width=20%\">@OTHERS</td>" 
				+ "<td style=\"width=12%\"></td>"
				+ "<td style=\"width=12%\"></td>" 
				+ "<td style=\"width=12%\"></td>"
				+ "<td style=\"width=12%\"></td>" 
				+ "<td style=\"width=12%\"></td>" 
				+ "</tr>"
				
				+ "<tr style=\"text-align: center; vertical-align: middle;\">" 
				+ "<td style=\"text-align: left; width=20%\">&nbsp; TOTAL </td>"
				+ "<td style=\"width=20%\">@TOTAL</td>" 
				+ "<td style=\"width=12%\"></td>"
				+ "<td style=\"width=12%\"></td>" 
				+ "<td style=\"width=12%\"></td>"
				+ "<td style=\"width=12%\"></td>" 
				+ "<td style=\"width=12%\"></td>" 
				+ "</tr>"
				
				+ "</table>" 
				+ "<p>&nbsp;</p>";

		String body = 
				  "<p>Address of the policy holder: @ADDRESSOFPOLICYHOLDER </p>"
				+ "<p>&nbsp;</p>" 
				+ "<p>GST no. of Customer: @GSTNUMBEROFCUSTOMER </p>"
				
				+ "<table width=\"100%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\">"
				+ "<tr>" 
				+ "<td style=\"width=30%\">Total Premium Amount</td>"
				+ "<td style=\"width=70%\">Rs.@TOTALPREMIUMAMOUNT  &amp;  @TOTALPREAMOUNTINWORDS </td>" 
				+ "</tr>"
				+ "</table>"
				
				+ "<p>This Final premium receipt is electronically generated on @SYSTEMDATE and does not </p>"
				+ "<p>require signature.</p>" 
				+ "<p>{If premium amount  Rs. 5000 and other than Mudrank.</p>"
				+ "<p>The Physical receipt with revenue stamp will be issued shortly.}will not come in print</p>"
				+ "<p>&nbsp;</p>" 
				+ "<p style=\"text-align:right;\">Mudrank</p>" 
				+ "<p>&nbsp;</p>"
				+ "<p style=\"text-align:right;\">Revenue Stamp</p>" 
				+ "<p>&nbsp;</p>";

		String table4 = "<table align=\"right\" width=\"12%\" border=\"1\" bordercolor=\"black\" cellpadding=\"4\" cellspacing=\"0\" >"
					  + "<tr style=\"height:80px\">" 
					  + "<td> &nbsp;&nbsp; &nbsp;&nbsp; </td>" 
					  + "</tr>" 
					  + "</table>";

		reportBody = reportBody + table + table1 + reportBody1 + table3 + body + table4;

		String adjustmenttypeLabel = new String();
		if (reportType.equalsIgnoreCase("regularAdjustment")) {
			adjustmenttypeLabel = "Regular Deposit Adjustment";
		} else if (reportType.equalsIgnoreCase("subsequentAdjustment")) {
			adjustmenttypeLabel = "Supplementary Deposit Adjustment";
		} else if (reportType.equalsIgnoreCase("NB")) {
			adjustmenttypeLabel = "New Business Deposit Adjustment";
		}

		String mode = null;

		if (premiumAdjustmentVoucherDto.getFrequency() != null) {
			if ("1".equals(premiumAdjustmentVoucherDto.getFrequency())) {
				mode = "Monthly";
			} else if ("2".equals(premiumAdjustmentVoucherDto.getFrequency())) {
				mode = "Quarterly";
			} else if ("3".equals(premiumAdjustmentVoucherDto.getFrequency())) {
				mode = "Half-Yearly";
			} else if ("4".equals(premiumAdjustmentVoucherDto.getFrequency())) {
				mode = "Yearly";
			}
		} else {
			mode = "";
		}

		reportBody = reportBody
				.replace("@DATEOFADJUSTMENT",premiumAdjustmentVoucherDto.getDateOfAdjustment() != null ? premiumAdjustmentVoucherDto.getDateOfAdjustment() : "  ")
				.replace("@SERVICINGUNIT", premiumAdjustmentVoucherDto.getServicingUnit() != null ? premiumAdjustmentVoucherDto.getServicingUnit() : "  ")
				.replace("@ADJUSTMENTNUMBER", premiumAdjustmentVoucherDto.getAdjustmentNumber() != null ? premiumAdjustmentVoucherDto.getAdjustmentNumber() : "  ")
				.replace("@ADDRESSOFUNIT", premiumAdjustmentVoucherDto.getAddressOfUnit() != null ? HtmlEncoder.encode(premiumAdjustmentVoucherDto.getAddressOfUnit()) : " ")
				.replace("@UNITGSTNUMBER", premiumAdjustmentVoucherDto.getUnitGstNumber() != null ? premiumAdjustmentVoucherDto.getUnitGstNumber() : " ")
				.replace("@MPHNAME", premiumAdjustmentVoucherDto.getMphName() != null ? HtmlEncoder.encode(premiumAdjustmentVoucherDto.getMphName()) : " ")
//				.replace("@MODEOFDEPOSIT", premiumAdjustmentVoucherDto.getModeOfDeposit() != null ? premiumAdjustmentVoucherDto.getModeOfDeposit() : "MODEOFDEPOSIT")
				.replace("@MODEOFDEPOSIT", adjustmenttypeLabel != null ? adjustmenttypeLabel : "")
				.replace("@POLICYNUMBER", premiumAdjustmentVoucherDto.getPolicyNumber() != null ? premiumAdjustmentVoucherDto.getPolicyNumber() : " ")
				.replace("@UIN", premiumAdjustmentVoucherDto.getUin() != null ? premiumAdjustmentVoucherDto.getUin() : " ")
				.replace("@NAMEOFPLAN", premiumAdjustmentVoucherDto.getNameOfPlan() != null ? premiumAdjustmentVoucherDto.getNameOfPlan() : "  ")
				.replace("@ANNUALRENEWALDATE", premiumAdjustmentVoucherDto.getAnnualRenewalDate() != null ? premiumAdjustmentVoucherDto.getAnnualRenewalDate() : " ")
				.replace("@PREMIUMADJUSTMENTFOR", premiumAdjustmentVoucherDto.getPremiumAdjustmentFor() != null ? premiumAdjustmentVoucherDto.getPremiumAdjustmentFor() : " ")
				.replace("@NEXTPREMIUMDUE", premiumAdjustmentVoucherDto.getNextPremiumDue() != null ? premiumAdjustmentVoucherDto.getNextPremiumDue() : " ")
				.replace("@FREQUENCY", mode != null ? mode : "")
				.replace("@ADDRESSOFPOLICYHOLDER", premiumAdjustmentVoucherDto.getAddressOfPolicyHolder() != null ? premiumAdjustmentVoucherDto.getAddressOfPolicyHolder() : " ")
				.replace("@GSTNUMBEROFCUSTOMER", premiumAdjustmentVoucherDto.getCustomerGstNumber() != null ? premiumAdjustmentVoucherDto.getCustomerGstNumber() : " ")
				.replace("@TOTALPREMIUMAMOUNT", premiumAdjustmentVoucherDto.getTotalPremiumAmount() != null ? NumericUtils .convertBigDecimalToString(premiumAdjustmentVoucherDto.getTotalPremiumAmount()) : "")
				.replace("@TOTALPREAMOUNTINWORDS", premiumAdjustmentVoucherDto.getTotalPremiumAmountInWords() != null ? premiumAdjustmentVoucherDto.getTotalPremiumAmountInWords() : " ")
				.replace("@SYSTEMDATE", premiumAdjustmentVoucherDto.getSysDate() != null ? premiumAdjustmentVoucherDto.getSysDate() : " ")


				.replace("@DEPOSITNO", premiumAdjustmentVoucherDto.getDepositNo() != null ? premiumAdjustmentVoucherDto.getDepositNo() : " ")
				.replace("@DEPOSITDATE", premiumAdjustmentVoucherDto.getDepositDate() != null ? premiumAdjustmentVoucherDto.getDepositDate() : " ")
				.replace("@DEPOSITAMOUNT", premiumAdjustmentVoucherDto.getDepositAmount() != null ? NumericUtils.convertBigDecimalToString(premiumAdjustmentVoucherDto.getDepositAmount()) : " ")
				
				.replace("@FUNDED", premiumAdjustmentVoucherDto.getTotalPremiumAmount() != null ? NumericUtils .convertBigDecimalToString(premiumAdjustmentVoucherDto.getTotalPremiumAmount()) : "")

				.replace("@TERMINSURANCE", (" "))
				.replace("@TERMINSURANCECGST", (" "))
				.replace("@TERMINSURANCESGST", (" "))
				.replace("@TERMINSURANCEUGST", (" "))
				.replace("@TERMINSURANCEIGST", (" "))
				.replace("@TERMINSURANCETOTAL", (" "))
				.replace("@ANNUITYPURCHASE", (" "))
				.replace("@ANNUITYPURCHASECGST", (" "))
				.replace("@ANNUITYPURCHASESGST", (" "))
				.replace("@ANNUITYPURCHASEUGST", (" "))
				.replace("@ANNUITYPURCHASEIGST", (" "))
				.replace("@ANNUITYPURCHASETOTAL", (" "))
				.replace("@LATEFEEONPREMIUM", (" "))
				.replace("@LATEFEEONPREMIUMCGST", (" "))
				.replace("@LATEFEEONPREMIUMSGST", (" "))
				.replace("@LATEFEEONPREMIUMUGST", (" "))
				.replace("@LATEFEEONPREMIUMIGST", (" "))
				.replace("@LATEFEEONPREMIUMTOTAL", (" "))
				.replace("@OTHERS", (" "))
				.replace("@OTHERSCGST", (" "))
				.replace("@OTHERSSGST", (" "))
				.replace("@OTHERSUGST", (" "))
				.replace("@OTHERSIGST", (" "))
				.replace("@OTHERSTOTAL", (" "))

				.replace("@TOTAL", (" "))
				.replace("@TOTALCGST", (" "))
				.replace("@TOTALSGST", (" "))
				.replace("@TOTALUGST", (" "))
				.replace("@TOTALIGST", (" "))
				.replace("@TOTALTOTAL", (" "));

		return reportBody.replace("\\", "").replace("\t", "");
	}

	private PremiumAdjustmentVoucherDto generatepremiumadjustmentvoucher(Long masterpolicyId, String reportType)
			throws MalformedURLException, ProtocolException, IOException {

		PremiumAdjustmentVoucherDto premiumAdjustmentVoucherDto = new PremiumAdjustmentVoucherDto();

		PolicyMasterEntity policyMasterEntity = new PolicyMasterEntity();
		PolicyContributionEntity policyContributionEntity = new PolicyContributionEntity();
		PolicyDepositEntity policyDepositEntity = new PolicyDepositEntity();

		if (reportType.equalsIgnoreCase("subsequentAdjustment")) {
			AdjustmentContributionEntity adjustmentContributionEntity = adjustmentContributionRepository
					.findByAdjustmentContributionIdAndIsActiveTrue(masterpolicyId);
			policyMasterEntity = policyMasterRepository
					.findByPolicyIdAndIsActiveTrue(adjustmentContributionEntity.getPolicyId());
			policyContributionEntity = policyContributionRepository.findByPolicyIdAndAdjustmentContributionId(
					adjustmentContributionEntity.getPolicyId(),
					adjustmentContributionEntity.getAdjustmentContributionId());
			policyDepositEntity = policyDepositRepository.findByPolicyIdAndAdjustmentContributionId(
					adjustmentContributionEntity.getPolicyId(),
					adjustmentContributionEntity.getAdjustmentContributionId());

		} else if (reportType.equalsIgnoreCase("regularAdjustment")) {

			RegularAdjustmentContributionEntity adjustmentContributionEntity = regularAdjustmentContributionRepository
					.findByRegularContributionIdAndIsActiveTrue(masterpolicyId);
			policyMasterEntity = policyMasterRepository
					.findByPolicyIdAndIsActiveTrue(adjustmentContributionEntity.getPolicyId());
			policyContributionEntity = policyContributionRepository.findByPolicyIdAndRegularContributionId(
					adjustmentContributionEntity.getPolicyId(),
					adjustmentContributionEntity.getRegularContributionId());
			policyDepositEntity = policyDepositRepository.findByPolicyIdAndRegularContributionId(
					adjustmentContributionEntity.getPolicyId(),
					adjustmentContributionEntity.getRegularContributionId());

		} else if (reportType.equalsIgnoreCase("NB")) {

			policyMasterEntity = policyMasterRepository.findByPolicyIdAndIsActiveTrue(masterpolicyId);
			policyContributionEntity = policyContributionRepository.findByPolicyIdAndContributionType(masterpolicyId,
					reportType);
			policyDepositEntity = policyDepositRepository.findByPolicyIdAndContributionType(masterpolicyId, reportType);

		}

		long policyId = policyMasterEntity.getPolicyId();
		
		Object mphDetails = mphMasterRepository.findMphDetails(policyMasterEntity.getMphId());
		if (mphDetails != null) {
			Object[] objMph = (Object[]) mphDetails;
			premiumAdjustmentVoucherDto.setMphName(String.valueOf(objMph[14]));
		}
		
		
		MphAddressEntity addressEntity = mphAddressRepository.findByMphIdAndIsActiveTrueAndIsDefaultTrue(policyMasterEntity.getMphId());
		if(addressEntity != null) {
//			premiumAdjustmentVoucherDto.setAddressOfPolicyHolder(addressEntity.getAddressLine1());

			
			String addressone = addressEntity.getAddressLine1()!=null ? addressEntity.getAddressLine1() : "";
			String addresstwo = addressEntity.getAddressLine2()!=null ? ", "+addressEntity.getAddressLine2() : "";
			String addressthree = addressEntity.getAddressLine3()!=null ? ", "+addressEntity.getAddressLine3() : "";
			
			premiumAdjustmentVoucherDto.setAddressOfPolicyHolder(addressone+addresstwo+addressthree);
					
//					addressEntity.getAddressLine1());
					//+","+addressEntity.getAddressLine2()+","+addressEntity.getAddressLine3());
		}

		
		premiumAdjustmentVoucherDto.setUin(getUINByVariantId(policyMasterEntity.getVariant()));
//		premiumAdjustmentVoucherDto.setNameOfPlan(getSchemaName(policyMasterEntity.getProductId()));
//		premiumAdjustmentVoucherDto.setNameOfPlan(getSchemeNameByVariantId(policyMasterEntity.getProductId()));
		premiumAdjustmentVoucherDto.setNameOfPlan(getSchemaPlanName(policyMasterEntity.getProductId()));

		List<PolicyFrequencyDetailsEntity> policyEntityNextDue = policyFrequencyRepository.findAllByPolicyIdAndStatusAndIsActiveTrueOrderByFrequencyIdDesc(policyId, CommonConstants.UNPAID);
		if(!policyEntityNextDue.isEmpty() && policyEntityNextDue.get(0) != null) {
			PolicyFrequencyDetailsEntity frequencyDetailsEntity = policyEntityNextDue.get(0);
			premiumAdjustmentVoucherDto.setNextPremiumDue(DateUtils.dateToStringDDMMYYYY(frequencyDetailsEntity.getFrequencyDates()));
		}

		List<PolicyFrequencyDetailsEntity> policyEntityPreviousDue = policyFrequencyRepository.findAllByPolicyIdAndStatusAndIsActiveTrueOrderByFrequencyIdAsc(policyId, CommonConstants.PAID);
		if(!policyEntityPreviousDue.isEmpty() && policyEntityPreviousDue.get(0) != null) {
			PolicyFrequencyDetailsEntity previousDueFrequencyDetailsEntity = policyEntityPreviousDue.get(0);
			premiumAdjustmentVoucherDto.setPremiumAdjustmentFor(DateUtils.dateToStringDDMMYYYY(previousDueFrequencyDetailsEntity.getFrequencyDates()));
		}
		
		
		premiumAdjustmentVoucherDto.setDateOfAdjustment(DateUtils.dateToStringDDMMYYYY(policyContributionEntity.getContributionDate()));

		premiumAdjustmentVoucherDto.setAdjustmentNumber(policyDepositEntity.getChallanNo());
		premiumAdjustmentVoucherDto.setPolicyNumber(policyMasterEntity.getPolicyNumber());
		premiumAdjustmentVoucherDto.setFrequency(NumericUtils.convertIntegerToString(policyMasterEntity.getContributionFrequency()));
		premiumAdjustmentVoucherDto.setAnnualRenewalDate(DateUtils.dateToStringDDMMYYYY(policyMasterEntity.getArd()));

		premiumAdjustmentVoucherDto.setTotalPremiumAmount(policyContributionEntity.getTotalContribution());
		premiumAdjustmentVoucherDto.setTotalPremiumAmountInWords("Rs."
				+ NumericUtils.convertAmountToWord(NumericUtils.convertStringToLong(
						NumericUtils.convertBigDecimalToString(policyContributionEntity.getTotalContribution())))
				+ " Only");

		/*** Note:-getunitDetailsByuniyCode ***/
		ResponseDto responseDto = accountingIntegrationService.commonmasterserviceUnitByCode(policyMasterEntity.getUnitId());
		if (responseDto != null) {
			premiumAdjustmentVoucherDto.setAddressOfUnit(responseDto.getAddress1());
			premiumAdjustmentVoucherDto.setUnitGstNumber(responseDto.getGastIn());
			premiumAdjustmentVoucherDto.setServicingUnit(responseDto.getCityName());
		} else {
			premiumAdjustmentVoucherDto.setAddressOfUnit("");
			premiumAdjustmentVoucherDto.setUnitGstNumber("");
			premiumAdjustmentVoucherDto.setServicingUnit("");
		}

//		premiumAdjustmentVoucherDto.setAddressOfUnit(policyMasterEntity.getUnitId());
//		premiumAdjustmentVoucherDto.setUnitGstNumber(policyMasterEntity.getUnitId());
//		premiumAdjustmentVoucherDto.setServicingUnit(policyMasterEntity.getUnitId());

		
		premiumAdjustmentVoucherDto.setCustomerGstNumber(null);

		premiumAdjustmentVoucherDto.setSysDate(DateUtils.dateToStringDDMMYYYY(DateUtils.sysDate()));

		premiumAdjustmentVoucherDto.setDepositNo(null);
		premiumAdjustmentVoucherDto.setDepositDate(DateUtils.dateToStringDDMMYYYY(policyDepositEntity.getCollectionDate()));
		premiumAdjustmentVoucherDto.setDepositAmount(policyDepositEntity.getDepositAmount());

		premiumAdjustmentVoucherDto.setTERMINSURANCE(null);
		premiumAdjustmentVoucherDto.setTERMINSURANCECGST(null);
		premiumAdjustmentVoucherDto.setTERMINSURANCESGST(null);
		premiumAdjustmentVoucherDto.setTERMINSURANCEUGST(null);
		premiumAdjustmentVoucherDto.setTERMINSURANCEIGST(null);
		premiumAdjustmentVoucherDto.setTERMINSURANCETOTAL(null);

		premiumAdjustmentVoucherDto.setANNUITYPURCHASE(null);
		premiumAdjustmentVoucherDto.setANNUITYPURCHASECGST(null);
		premiumAdjustmentVoucherDto.setANNUITYPURCHASESGST(null);
		premiumAdjustmentVoucherDto.setANNUITYPURCHASEUGST(null);
		premiumAdjustmentVoucherDto.setANNUITYPURCHASEIGST(null);
		premiumAdjustmentVoucherDto.setANNUITYPURCHASETOTAL(null);

		premiumAdjustmentVoucherDto.setLATEFEEONPREMIUM(null);
		premiumAdjustmentVoucherDto.setLATEFEEONPREMIUMCGST(null);
		premiumAdjustmentVoucherDto.setLATEFEEONPREMIUMSGST(null);
		premiumAdjustmentVoucherDto.setLATEFEEONPREMIUMUGST(null);
		premiumAdjustmentVoucherDto.setLATEFEEONPREMIUMIGST(null);
		premiumAdjustmentVoucherDto.setLATEFEEONPREMIUMTOTAL(null);

		premiumAdjustmentVoucherDto.setOTHERS(null);
		premiumAdjustmentVoucherDto.setOTHERSCGST(null);
		premiumAdjustmentVoucherDto.setOTHERSSGST(null);
		premiumAdjustmentVoucherDto.setOTHERSUGST(null);
		premiumAdjustmentVoucherDto.setOTHERSIGST(null);
		premiumAdjustmentVoucherDto.setOTHERSTOTAL(null);

		premiumAdjustmentVoucherDto.setTOTAL(null);
		premiumAdjustmentVoucherDto.setTOTALCGST(null);
		premiumAdjustmentVoucherDto.setTOTALSGST(null);
		premiumAdjustmentVoucherDto.setTOTALUGST(null);
		premiumAdjustmentVoucherDto.setTOTALIGST(null);
		premiumAdjustmentVoucherDto.setTOTALTOTAL(null);

		return premiumAdjustmentVoucherDto;
	}
	
	@Override
	public String getGlCodesSheetpdf(String claimOnBoardNo) throws IOException {

		InputStream islicLogo = new ClassPathResource("liclogo.png").getInputStream();
		// Uncomment below line to add watermark to the pdf
		// InputStream islicWatermark = new
		// ClassPathResource("watermark.png").getInputStream();

		byte[] bytesLogo = IOUtils.toByteArray(islicLogo);
		// Uncomment below line to add watermark to the pdf
		// byte[] bytesWatermark = IOUtils.toByteArray(islicWatermark);

		String licLogo = Base64.getEncoder().encodeToString(bytesLogo);
		// Uncomment below line to add watermark to the pdf
		// String licWatermark = Base64.getEncoder().encodeToString(bytesWatermark);
		
		
		List<GlCodesResponseDto> glCodesResponseDto = new ArrayList<GlCodesResponseDto>();
		
//		List<Object[]> claimEntity = claimRepository.getModeOfExistFromClaim(claimOnBoardNo);
				
		ClaimEntity claimEntity = claimRepository.getClaimDetails(claimOnBoardNo);

		if (claimEntity != null) {
//		Integer modeOfExist = 0;
//		for (Object[] objects : claimEntity) {
//
//			modeOfExist = NumericUtils.convertStringToInteger((objects[0] != null) ? objects[0].toString() : null);
//		}

		List<ProcSaStoredProcedureResponseEntity> storedProcedureResponseEntity = procSaStoredProcedureResponseRepository
				.findByClaimOnBoardNoAndIsActiveTrue(claimOnBoardNo);

		if (storedProcedureResponseEntity != null) {

			for (ProcSaStoredProcedureResponseEntity procSaStoredProcedureResponseEntity : storedProcedureResponseEntity) {

				GlCodesResponseDto glCodesResponse = new GlCodesResponseDto();

				List<String> dethCase = new ArrayList<>();
				dethCase.add("CL047".toLowerCase());
				dethCase.add("CL049".toLowerCase());
				dethCase.add("CL029".toLowerCase());
				dethCase.add("AD028".toLowerCase());

				List<String> withdrawalCase = new ArrayList<>();
				withdrawalCase.add("CL059".toLowerCase());
				withdrawalCase.add("CL040".toLowerCase());
				withdrawalCase.add("CL029".toLowerCase());

				List<String> maturityCase = new ArrayList<>();
				maturityCase.add("CL055".toLowerCase());
				maturityCase.add("CL038".toLowerCase());
				maturityCase.add("CL029".toLowerCase());
				maturityCase.add("AD028".toLowerCase());
				
				List<String> deathGlCode = new ArrayList<>();
				deathGlCode.add("1205".toLowerCase());
				deathGlCode.add("2203".toLowerCase());
				deathGlCode.add("1291".toLowerCase());
//				deathGlCode.add("1107".toLowerCase());
				
				List<String> withdrawralGlCode = new ArrayList<>();
				withdrawralGlCode.add("1254".toLowerCase());
				withdrawralGlCode.add("2203".toLowerCase());
//				withdrawralGlCode.add("2203".toLowerCase());
				withdrawralGlCode.add("1291".toLowerCase());
				
				List<String> maturityGlCode = new ArrayList<>();
				maturityGlCode.add("1232".toLowerCase());
				maturityGlCode.add("2203".toLowerCase());
				maturityGlCode.add("1291".toLowerCase());
//				maturityGlCode.add("1107".toLowerCase());

				if (((claimEntity.getModeOfExit() == ClaimConstants.RETRIERMENT
						|| claimEntity.getModeOfExit() == ClaimConstants.RESIGNATION)
						&& maturityCase.contains(procSaStoredProcedureResponseEntity.getAccountRuleCode().toLowerCase()))
						|| (claimEntity.getModeOfExit() == ClaimConstants.DEATH && dethCase
								.contains(procSaStoredProcedureResponseEntity.getAccountRuleCode().toLowerCase()))
						|| ((claimEntity.getModeOfExit() == ClaimConstants.WITHDRAWAL && withdrawalCase.contains(
								procSaStoredProcedureResponseEntity.getAccountRuleCode().toLowerCase())))) {

					if ((procSaStoredProcedureResponseEntity != null
							&& dethCase.contains(procSaStoredProcedureResponseEntity.getAccountRuleCode().toLowerCase())
							&& deathGlCode.contains(procSaStoredProcedureResponseEntity.getGlCode().toLowerCase()))) {

						glCodesResponse.setAmount(procSaStoredProcedureResponseEntity.getAmount());
						glCodesResponse.setAccountType(procSaStoredProcedureResponseEntity.getAccountType());
						glCodesResponse.setGlCode(procSaStoredProcedureResponseEntity.getGlCode());
						glCodesResponse.setGlCodeDesc(procSaStoredProcedureResponseEntity.getGlCodeDesc());
						glCodesResponse
								.setAccountRuleCode(procSaStoredProcedureResponseEntity.getAccountRuleCode());
						glCodesResponseDto.add(glCodesResponse);

					}
					else if ((procSaStoredProcedureResponseEntity != null
							&& withdrawalCase.contains(procSaStoredProcedureResponseEntity.getAccountRuleCode().toLowerCase())
							&& withdrawralGlCode.contains(procSaStoredProcedureResponseEntity.getGlCode().toLowerCase()))) {

						glCodesResponse.setAmount(procSaStoredProcedureResponseEntity.getAmount());
						glCodesResponse.setAccountType(procSaStoredProcedureResponseEntity.getAccountType());
						glCodesResponse.setGlCode(procSaStoredProcedureResponseEntity.getGlCode());
						glCodesResponse.setGlCodeDesc(procSaStoredProcedureResponseEntity.getGlCodeDesc());
						glCodesResponse
								.setAccountRuleCode(procSaStoredProcedureResponseEntity.getAccountRuleCode());
						glCodesResponseDto.add(glCodesResponse);

					} else if ((procSaStoredProcedureResponseEntity != null
							&& maturityCase.contains(procSaStoredProcedureResponseEntity.getAccountRuleCode().toLowerCase())
							&& maturityGlCode.contains(procSaStoredProcedureResponseEntity.getGlCode().toLowerCase()))) {

						glCodesResponse.setAmount(procSaStoredProcedureResponseEntity.getAmount());
						glCodesResponse.setAccountType(procSaStoredProcedureResponseEntity.getAccountType());
						glCodesResponse.setGlCode(procSaStoredProcedureResponseEntity.getGlCode());
						glCodesResponse.setGlCodeDesc(procSaStoredProcedureResponseEntity.getGlCodeDesc());
						glCodesResponse
								.setAccountRuleCode(procSaStoredProcedureResponseEntity.getAccountRuleCode());
						glCodesResponseDto.add(glCodesResponse);

					}
				}

			}
		}
	}
		
		boolean showLogo = true;
		String reportBody = "";
		String reportStyles = "";
		String htmlContent2 = "</div></body></html>";
		
		reportBody = generateGlCodesSheet(glCodesResponseDto) + htmlContent2;

//		reportStyles = quotationValuationReportStyles();

		String completehtmlContent = "<!DOCTYPE  html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><title>LIC ePGS</title><meta name=\"author\" content=\"LIC PNGS\"/><meta name=\"keywords\" content=\"Customer Communication\"/><meta name=\"description\" content=\"Report/Letter/Contract\"/>"
				+ "<style type=\"text/css\"> @page{size: A4 portrait;} body{border-width:0px;\r\n"
				+ "border-style:solid;\r\n" + "border-color:#586ec5;} * {margin:0; padding:0; text-indent:0; }"
				+ " .s1 { color: #2E5396; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 16pt; }"
				+ " .s2 { color: #2E3599; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " p { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 10pt; margin:0pt; }"
				+ " .a { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " .s3 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " .s5 { color: #2E3599; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ " .s6 { color: #120000; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ " .s7 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " .s8 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; vertical-align: -2pt; }"
				+ " .s9 { color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 12pt; }"
				+ " h1 { color: #120000; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ " .s10 { color: #2D3499; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: bold; text-decoration: none; font-size: 12pt; }"
				+ " .pt10 { padding-top: 10pt; }" + " .pb10 { padding-bottom: 10pt; }"
				+ " .pb50 { padding-bottom: 50pt; }"
				+ " table, tbody, td {vertical-align: top; overflow: visible; color: black; font-family:\"Times New Roman\", serif; font-style: normal; font-weight: normal; text-decoration: none; font-size: 8pt; margin:0pt; } "
				+ " @media print { .pagebreak {page-break-after: always;} } "
				+ " table thead tr td { padding-top:5px; padding-bottom:5px; border-bottom: 1px solid #000000; border-top: 1px solid #000000;}"
				// Uncomment below line to add watermark to the pdf
				// + " .watermark {\r\n background-image: url(\"data:image/png;base64," +
				// licWatermark + "\");} "
				+ " " + reportStyles + " " + " </style></head><body><div id=\"bg\" class=\"watermark\">"
//				+ "<p style=\"text-align: Center;\">Life Insurance Corporation of India"
//				+ " Pensions & Group Schemes Department</p>"
				+"<p style=\"text-align:center;\"> Life Insurance Corporation of India <br/>"
				+ " Pensions And Group Schemes Department</p>"
				+ "<p style=\"padding-left: 5pt;text-indent: 0pt;text-align: left;\"/>";

		if (showLogo) {
			completehtmlContent = completehtmlContent
					+ "<p style=\"text-indent: 0pt;text-align: left;\"><span><table style=\"width:100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr style=\"display:flex;justify-content:space-between;align-items:center\"><td style=\"padding-left:24pt\"><img width=\"150\" height=\"96\" src=\"data:image/jpg;base64,"
					+ licLogo + "\"/></td>" + "</tr></table></span> <br/></p>";
					
		}
		completehtmlContent = completehtmlContent + "<p style=\"text-indent: 0pt;text-align: Center;\"><br/> <u>Detailed Transactions Description</u></p>"
				+ reportBody + "";

		String htmlFileLoc = tempPdfLocation + "/" + claimOnBoardNo+ ".html";
		FileWriter fw = new FileWriter(htmlFileLoc);
		fw.write(completehtmlContent);
		fw.close();

		FileOutputStream fileOutputStream = new FileOutputStream(tempPdfLocation + "/" + claimOnBoardNo+ ".pdf");
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(new File(htmlFileLoc));
		renderer.layout();
		try {
			renderer.createPDF(fileOutputStream, false);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		renderer.finishPDF();

		if (new File(htmlFileLoc).exists()) {
			new File(htmlFileLoc).delete();
		}

		return tempPdfLocation + "/" + claimOnBoardNo + ".pdf";
	}

	private String generateGlCodesSheet(List<GlCodesResponseDto> glCodesResponseDto) {

		
		int pageno = 1;
		pageno++;
		
		String border = "";
		
		String table = 
				        "<br/><br/>"
						+ "<table style=\"width:100%;border: 1px solid black; border-collapse: collapse;\">" 
						+"<thead>"
						+ "<tr style=\"text-align:right;\">"
						+ "<th style=\"text-align:center;border: 1px solid black; border-collapse: collapse; padding:5px;\">Voucher Amount</th>"
						+ "<th style=\"text-align:center;border: 1px solid black; border-collapse: collapse; padding:5px;\">Transaction Type</th>"
						+ "<th style=\"text-align:center;border: 1px solid black; border-collapse: collapse; padding:5px;\">Gl Code</th>"
						+ "<th style=\"text-align:center; border: 1px solid black; border-collapse: collapse; padding:5px;\">Gl Code Description</th>"
						+ "<th style=\"text-align:center; border: 1px solid black; border-collapse: collapse; padding:5px;\">Account Code</th>"
						+ "</tr>"
						+"</thead>";
		

		String detailTable = "";
		
		for (GlCodesResponseDto get : glCodesResponseDto ) {
			detailTable +=
					"<tr style=\"text-align:right;\">"
					+ "<td style=\"text-align:center; border: 1px solid black; border-collapse: collapse; padding:5px;\">"+ get.getAmount()+"</td>" 
					+ "<td style=\"text-align:center; border: 1px solid black; border-collapse: collapse; padding:5px;\">"+ get.getAccountType()+"</td>"
					+ "<td style=\"text-align:center;border: 1px solid black; border-collapse: collapse; padding:5px;\">"+ get.getGlCode()+" </td>" 
					+ "<td style=\"text-align:center; border: 1px solid black; border-collapse: collapse; padding:5px;\">"+ get.getGlCodeDesc()+"</td>"
					+ "<td style=\"text-align:center; border: 1px solid black; border-collapse: collapse; padding:5px;\">"+ get.getAccountRuleCode() +"</td>"
					+ "</tr>";
		}
		
		detailTable += "</table>"
				+ "<hr style=\"border: 1px solid black;\"></hr>";

		return table+detailTable;

		
	}
	
	
	

}