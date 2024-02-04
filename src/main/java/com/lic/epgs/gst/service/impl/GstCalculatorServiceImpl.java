package com.lic.epgs.gst.service.impl;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.lic.epgs.gst.dto.GstCalculationRequest;
import com.lic.epgs.gst.dto.GstCalculationResponse;
import com.lic.epgs.gst.service.GstCalculatorService;
@Service
public class GstCalculatorServiceImpl implements GstCalculatorService{
	Logger log = LoggerFactory.getLogger(GstCalculatorServiceImpl.class);
	
	@Autowired
	private Environment env;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public GstCalculationResponse callGstCalculation(GstCalculationRequest gstCalculationRequest) {

		log.info("Enter into GstCalculatorServiceImpl : callGstCalculation");

		StoredProcedureQuery storedProcedure = entityManager
				.createStoredProcedureQuery("licannuity.SP_PENSION_GST_CAL");
		log.info("Gst calculation Store Proc name : licannuity.SP_PENSION_GST_CAL ");
		log.info("input output param register");

		storedProcedure.registerStoredProcedureParameter("FROMSTATECODE", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("TOSTATECODE", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("FROMSTATENAME", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("TOSTATENAME", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("AMOUNT", BigDecimal.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("ISAMOUNTWITHTAX", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("HSNCODE", String.class, ParameterMode.IN);

		storedProcedure.registerStoredProcedureParameter("CGSTRATE", BigDecimal.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("CGSTRATEAMOUNT", BigDecimal.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("SGSTRATE", BigDecimal.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("SGSTRATEAMOUNT", BigDecimal.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("IGSTRATE", BigDecimal.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("IGSTRATEAMOUNT", BigDecimal.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("UTGSTRATE", BigDecimal.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("UTGSTRATEAMOUNT", BigDecimal.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("TOTALGSTRATE", BigDecimal.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("TOTALGSTAMOUNT", BigDecimal.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("FROMGSTSTATECODE", String.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("TOGSTSTATECODE", String.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("TAXABLEPERCENTAGE", BigDecimal.class, ParameterMode.OUT);

		storedProcedure.registerStoredProcedureParameter("P_MESSAGE", String.class, ParameterMode.OUT);
		storedProcedure.registerStoredProcedureParameter("P_RESULTCODE", Integer.class, ParameterMode.OUT);

		log.info("before input parameter values inserction");

		log.info("FROMSTATECODE:{}", gstCalculationRequest.getFromStateCode());
		storedProcedure.setParameter("FROMSTATECODE", gstCalculationRequest.getFromStateCode());

		log.info("TOSTATECODE:{}", gstCalculationRequest.getToStateCode());
		storedProcedure.setParameter("TOSTATECODE", gstCalculationRequest.getToStateCode());

		log.info("FROMSTATENAME:{}", gstCalculationRequest.getFromStateName());
		storedProcedure.setParameter("FROMSTATENAME", gstCalculationRequest.getFromStateName());

		log.info("TOSTATENAME:{}", gstCalculationRequest.getToStateName());
		storedProcedure.setParameter("TOSTATENAME", gstCalculationRequest.getToStateName());

		log.info("AMOUNT:{}", gstCalculationRequest.getAmount());
		storedProcedure.setParameter("AMOUNT", gstCalculationRequest.getAmount());

		log.info("ISAMOUNTWITHTAX:{}", gstCalculationRequest.getIsAmountWithTax());
		storedProcedure.setParameter("ISAMOUNTWITHTAX", gstCalculationRequest.getIsAmountWithTax());

		storedProcedure.setParameter("HSNCODE",
				gstCalculationRequest.getHsnCode() != null ? gstCalculationRequest.getHsnCode()
						: env.getProperty("accounting.annuity.hsncode"));

		log.info("HSNCODE:{}", gstCalculationRequest.getHsnCode() != null ? gstCalculationRequest.getHsnCode()
				: env.getProperty("accounting.annuity.hsncode"));

		log.info("before storedProcedure execution");
		storedProcedure.execute();
		log.info("after storedProcedure execution");

		GstCalculationResponse gstCalculationResponse = new GstCalculationResponse();

		gstCalculationResponse.setcGstRate((BigDecimal) storedProcedure.getOutputParameterValue("CGSTRATE"));
		log.info("CGSTRATE:{}", gstCalculationResponse.getcGstRate());

		gstCalculationResponse
				.setcGstRateAmount((BigDecimal) storedProcedure.getOutputParameterValue("CGSTRATEAMOUNT"));
		log.info("CGSTRATEAMOUNT:{}", gstCalculationResponse.getcGstRateAmount());

		gstCalculationResponse.setsGstRate((BigDecimal) storedProcedure.getOutputParameterValue("SGSTRATE"));
		log.info("SGSTRATE:{}", gstCalculationResponse.getsGstRate());

		gstCalculationResponse
				.setsGstRateAmount((BigDecimal) storedProcedure.getOutputParameterValue("SGSTRATEAMOUNT"));
		log.info("SGSTRATEAMOUNT:{}", gstCalculationResponse.getsGstRateAmount());

		gstCalculationResponse.setiGstRate((BigDecimal) storedProcedure.getOutputParameterValue("IGSTRATE"));
		log.info("IGSTRATE:{}", gstCalculationResponse.getiGstRate());

		gstCalculationResponse
				.setiGstRateAmount((BigDecimal) storedProcedure.getOutputParameterValue("IGSTRATEAMOUNT"));
		log.info("IGSTRATEAMOUNT:{}", gstCalculationResponse.getiGstRateAmount());

		gstCalculationResponse.setUtGstRate((BigDecimal) storedProcedure.getOutputParameterValue("UTGSTRATE"));
		log.info("UTGSTRATE:{}", gstCalculationResponse.getUtGstRate());

		gstCalculationResponse
				.setUtGstRateAmount((BigDecimal) storedProcedure.getOutputParameterValue("UTGSTRATEAMOUNT"));
		log.info("UTGSTRATEAMOUNT:{}", gstCalculationResponse.getUtGstRateAmount());

		gstCalculationResponse.setTotalGstRate((BigDecimal) storedProcedure.getOutputParameterValue("TOTALGSTRATE"));
		log.info("TOTALGSTRATE:{}", gstCalculationResponse.getTotalGstRate());

		gstCalculationResponse
				.setTotalGstAmount((BigDecimal) storedProcedure.getOutputParameterValue("TOTALGSTAMOUNT"));
		log.info("TOTALGSTAMOUNT:{}", gstCalculationResponse.getTotalGstAmount());

		gstCalculationResponse
				.setFromGstStateCode((String) storedProcedure.getOutputParameterValue("FROMGSTSTATECODE"));
		log.info("FROMGSTSTATECODE:{}", gstCalculationResponse.getFromGstStateCode());

		gstCalculationResponse.setToGstStateCode((String) storedProcedure.getOutputParameterValue("TOGSTSTATECODE"));
		log.info("TOGSTSTATECODE:{}", gstCalculationResponse.getToGstStateCode());

		gstCalculationResponse
				.setTaxablePercengtage((BigDecimal) storedProcedure.getOutputParameterValue("TAXABLEPERCENTAGE"));
		log.info("TAXABLEPERCENTAGE:{}", gstCalculationResponse.getTaxablePercengtage());

		gstCalculationResponse.setMessage((String) storedProcedure.getOutputParameterValue("P_MESSAGE"));
		log.info("P_MESSAGE:{}", gstCalculationResponse.getMessage());

		gstCalculationResponse.setResultCode((int) storedProcedure.getOutputParameterValue("P_RESULTCODE"));
		log.info("P_RESULTCODE:{}", gstCalculationResponse.getResultCode());

		log.info("Exit into AnnuityCalculatorServiceImpl : callGstCalculation");

		return gstCalculationResponse;

	}


}
