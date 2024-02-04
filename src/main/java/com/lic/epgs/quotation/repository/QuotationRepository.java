package com.lic.epgs.quotation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.lic.epgs.quotation.entity.QuotationEntity;

public interface QuotationRepository extends JpaRepository<QuotationEntity, Integer> {

//for quotation 
	
	@Query(value = "SELECT MODULE_SEQ_FUNC(?1) FROM DUAL", nativeQuery = true)
	String getSequence(String type);
	List<QuotationEntity> findAllByStatusInAndIsActiveTrueAndUnitCodeOrderByQuotationIdDesc(List<Integer> makerInprogress, String unitCode);
	List<QuotationEntity> findAllByStatusInAndUnitCodeOrderByQuotationIdDesc(List<Integer> makerInprogress,String unitCode);
	QuotationEntity findByIsActiveTrueAndQuotationNoAndStatusInAndUnitCode(String quotationNo,List<Integer> existingActuary, String unitOffice);
	QuotationEntity findByQuotationNoAndStatusInAndUnitCode(String quotationNo, List<Integer> existingActuary,String unitOffice);
	QuotationEntity findByProposalNumberAndQuotationNoAndUnitCodeAndIsActiveFalseAndStatusNot(String proposalNumber,String quotationNo, String unitCode, Integer status);
	QuotationEntity findByProposalNumberAndStatusAndIsActiveTrue(String proposalNumber, Integer commonApproved);
	QuotationEntity findByProposalNumberAndIsActiveTrueAndUnitCodeAndStatus(String proposalNumber,String unitCode, Integer status);
	
	
//	
	
	
	
//	for policy
	Optional<QuotationEntity> findByQuotationIdAndStatusAndIsActiveTrue(Integer quotationId, Integer approvedNo);
//	
	
	
//	for claims
	
//	@Query(value="SELECT NEW_DATE,CASE WHEN TO_CHAR(NEW_DATE,'MM') IN ('01','02','03') THEN \r\n"
//			+ "TO_CHAR(TO_NUMBER(TO_CHAR(NEW_DATE,'YYYY')))-1||'-'||TO_CHAR(TO_NUMBER(TO_CHAR(NEW_DATE,'YYYY'))) ELSE\r\n"
//			+ " TO_CHAR(TO_NUMBER(TO_CHAR(NEW_DATE,'YYYY')))||'-'||TO_CHAR(TO_NUMBER(TO_CHAR(NEW_DATE,'YYYY'))+1) END\r\n"
//			+ " FINANCIAL_YEAR FROM (SELECT TO_DATE(?1,'DD-MM-YYYY') NEW_DATE FROM DUAL)",nativeQuery=true)
	
	@Query(value="	SELECT IN_DATE,\r\n"
			+ "	CASE WHEN TO_CHAR(IN_DATE,'MM') IN ('01','02','03') THEN TO_CHAR(TO_NUMBER(TO_CHAR(IN_DATE,'YYYY')))-1||'-'||TO_CHAR(TO_NUMBER(TO_CHAR(IN_DATE,'YYYY')))\r\n"
			+ "	ELSE TO_CHAR(TO_NUMBER(TO_CHAR(IN_DATE,'YYYY')))||'-'||TO_CHAR(TO_NUMBER(TO_CHAR(IN_DATE,'YYYY'))+1) END FINANCIAL_YEAR,\r\n"
			+ "	CASE WHEN TO_CHAR(IN_DATE,'MM') IN ('04','05','06') THEN 'Q1'\r\n"
			+ "	ELSE CASE WHEN TO_CHAR(IN_DATE,'MM') IN ('07','08','09') THEN 'Q2'\r\n"
			+ "	ELSE CASE WHEN TO_CHAR(IN_DATE,'MM') IN ('10','11','12') THEN 'Q3'\r\n"
			+ "	ELSE 'Q4' END END END QUARTER,\r\n"
			+ "	CASE WHEN TO_CHAR(IN_DATE,'MM') IN ('04','05','06') THEN '1'\r\n"
			+ "	ELSE CASE WHEN TO_CHAR(IN_DATE,'MM') IN ('07','08','09') THEN '2'\r\n"
			+ "	ELSE CASE WHEN TO_CHAR(IN_DATE,'MM') IN ('10','11','12') THEN '3'\r\n"
			+ "	ELSE '4' END END END FREQUENCY\r\n"
			+ "	FROM\r\n"
			+ "	(SELECT TO_DATE(?1,'DD-MM-YYYY') IN_DATE FROM DUAL)",nativeQuery=true)
	

	Object findByObjectNew(@RequestParam String date);
//	
	
	@Query(value="SELECT QUOTATION_NO FROM QUOTATION q WHERE QUOTATION_ID =:quotationId AND IS_ACTIVE =1",nativeQuery=true)
	String getQuotationNumber(Integer quotationId);

}
