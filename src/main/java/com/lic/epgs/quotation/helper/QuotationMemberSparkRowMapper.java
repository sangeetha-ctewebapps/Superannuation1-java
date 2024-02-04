//package com.lic.epgs.quotation.helper;
//
//import org.apache.spark.api.java.function.MapFunction;
//import org.apache.spark.sql.Row;
//
//import com.lic.epgs.quotation.dto.QuotationMemberErrorDto;
//
//public class QuotationMemberSparkRowMapper implements MapFunction<Row, QuotationMemberErrorDto> {
//
//	private static final long serialVersionUID = 1L;
//
//	@Override
//	public QuotationMemberErrorDto call(Row row) throws Exception {
//		QuotationMemberErrorDto errorDto = new QuotationMemberErrorDto();
//		errorDto.setMembershipNumber(row.getAs("MEMBERSHIP_NUMBER"));
//		errorDto.setFirstName(row.getAs("FIRSTNAME"));
//		errorDto.setMiddleName(row.getAs("MIDDLE_NAME"));
//		errorDto.setLastName(row.getAs("LAST_NAME"));
//		errorDto.setGender(row.getAs("GENDER"));
//		errorDto.setAadharNumber(row.getAs("AADHAR_NUMBER"));
////		errorDto.setPhone(row.getAs("PHONE"));L̥
////		errorDto.setEmail(row.getAs("EMAILID"));
//		return errorDto;
//	}
//
//}
