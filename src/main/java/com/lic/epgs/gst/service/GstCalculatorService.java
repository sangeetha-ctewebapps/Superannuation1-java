package com.lic.epgs.gst.service;

import com.lic.epgs.gst.dto.GstCalculationRequest;
import com.lic.epgs.gst.dto.GstCalculationResponse;

public interface GstCalculatorService {
	GstCalculationResponse callGstCalculation(GstCalculationRequest gstCalculationRequest);
}
