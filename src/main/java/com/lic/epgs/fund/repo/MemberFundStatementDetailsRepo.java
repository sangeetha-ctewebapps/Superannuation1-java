/**
 * 
 */
package com.lic.epgs.fund.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lic.epgs.fund.entity.MemberFundStatementDetailsEntity;

/**
 * @author Muruganandam
 *
 */
public interface MemberFundStatementDetailsRepo extends JpaRepository<MemberFundStatementDetailsEntity, Long> {

//	for intimation
	@Query(value ="WITH MEM_STATEMENT_V1_V3\r\n"
			+ "AS \r\n"
			+ "(\r\n"
			+ "SELECT * FROM (\r\n"
			+ "SELECT \r\n"
			+ "MFS.MEM_STATEMENT_ID,\r\n"
			+ "TO_CHAR(MFD.TRANSACTION_DATE,'DD-MM-YYYY') AS \"DATE\",\r\n"
			+ "DECODE(MFD.ENTRY_TYPE,'CLAIM','DEBIT INT',MFD.ENTRY_TYPE) AS \"TYPE\",\r\n"
			+ "CASE WHEN MFD.ENTRY_TYPE='CLAIM' THEN 0 ELSE MFD.TOTAL_CONTRIBUTION END AS AMOUNT,\r\n"
			+ "MFD.AIR AS \"INT RATE\",\r\n"
			+ "MFD.RECON_DAYS AS \"NO OF DAYS\",\r\n"
			+ "MFD.INTEREST_AMOUNT AS \"INTEREST AMOUNT\",\r\n"
			+ "CASE WHEN MFD.ENTRY_TYPE='CLAIM' THEN MFD.INTEREST_AMOUNT ELSE MFD.TOTAL_CONTRIBUTION + MFD.INTEREST_AMOUNT END AS \"TOTAL\",\r\n"
			+ "DENSE_RANK() OVER (ORDER BY MFS.MEM_STATEMENT_ID DESC) MEMBER_RANK\r\n"
			+ "FROM MEMBER_FUND_STATEMENT_DETAILS MFD,MEMBER_FUND_STATEMENT_SUMMARY MFS\r\n"
			+ "WHERE MFD.MEM_STATEMENT_ID = MFS.MEM_STATEMENT_ID\r\n"
			+ "AND MFS.IS_ACTIVE = 1\r\n"
			+ "AND MFS.MEMBER_ID = ?1 \r\n"
			+ "AND MFS.FINANCIAL_YEAR = ?2 \r\n"
			+ ") WHERE MEMBER_RANK = 1\r\n"
			+ "ORDER BY TO_DATE(\"DATE\",'DD-MM-YYYY')\r\n"
			+ "),\r\n"
			+ "MEM_CB_V1_V3 AS\r\n"
			+ "(\r\n"
			+ "SELECT * FROM (\r\n"
			+ "SELECT \r\n"
			+ "COUNT(1) OVER (PARTITION BY MF.MEM_STATEMENT_ID ORDER BY MF.MEM_STATEMENT_ID DESC) ROWNO,\r\n"
			+ "TO_CHAR(MF.CREATED_ON,'DD-MM-YYYY') AS \"DATE\",'CB' AS \"TYPE\",MF.CLOSING_BALANCE AS AMOUNT,0 AS \"INT RATE\",0 AS \"NO OF DAYS\",\r\n"
			+ "MF.CLOSING_BALANCE_INT AS \"INTEREST AMOUNT\",MF.POLICY_ACCOUNT_VALUE AS \"TOTAL\",\r\n"
			+ "ROW_NUMBER() OVER (ORDER BY MF.MEM_STATEMENT_ID DESC)  MEMBER_RANK\r\n"
			+ "FROM MEMBER_FUND_STATEMENT_SUMMARY MF,MEM_STATEMENT_V1_V3 MS\r\n"
			+ "WHERE MF.MEM_STATEMENT_ID=MS.MEM_STATEMENT_ID\r\n"
			+ ") WHERE MEMBER_RANK = 1\r\n"
			+ ")\r\n"
			+ "SELECT \r\n"
			+ "ROWNUM AS \"S NO\",\r\n"
			+ "MS.\"DATE\",\r\n"
			+ "MS.\"TYPE\",\r\n"
			+ "MS.AMOUNT,\r\n"
			+ "MS.\"INT RATE\",\r\n"
			+ "MS.\"NO OF DAYS\",\r\n"
			+ "MS.\"INTEREST AMOUNT\",\r\n"
			+ "MS.\"TOTAL\"\r\n"
			+ "FROM MEM_STATEMENT_V1_V3 MS\r\n"
			+ "UNION\r\n"
			+ "SELECT ROWNO+1,\r\n"
			+ "MCB.\"DATE\",\r\n"
			+ "MCB.\"TYPE\",\r\n"
			+ "MCB.AMOUNT,\r\n"
			+ "MCB.\"INT RATE\",\r\n"
			+ "MCB.\"NO OF DAYS\",\r\n"
			+ "MCB.\"INTEREST AMOUNT\",\r\n"
			+ "MCB.TOTAL\r\n"
			+ "FROM MEM_CB_V1_V3 MCB",nativeQuery=true)
	List<Object[]> getFund(String memberId, String financialYear);
	
	@Query(value ="SELECT BATCH_ID ,to_char(CREATED_ON,'DD-MM-YYYY'), CREATED_BY ,MODIFIED_BY  ,FILE_NAME  ,MODIFIED_ON  ,IS_ACTIVE  ,SUCCESS_COUNT  ,TOTAL_COUNT  FROM  TEMP_CLAIM_MBR_BATCH",nativeQuery=true)
	List<Object[]> getData();
	
	
	@Query(value ="WITH MEM_STATEMENT_V2 AS\r\n"
			+ "(\r\n"
			+ "SELECT * FROM\r\n"
			+ "(SELECT \r\n"
			+ "MFS.MEM_STATEMENT_ID,\r\n"
			+ "TO_CHAR(MFD.TRANSACTION_DATE,'DD-MM-YYYY') AS \"DATE\",\r\n"
			+ "DECODE(MFD.ENTRY_TYPE,'CLAIM','DEBIT INT',MFD.ENTRY_TYPE) AS \"TYPE\",\r\n"
			+ "CASE WHEN MFD.ENTRY_TYPE='CLAIM' THEN 0 ELSE COALESCE(MFD.TOTAL_CONTRIBUTION,0) END AS AMOUNT,\r\n"
			+ "COALESCE(MFD.AIR,0) AS \"INT RATE\",\r\n"
			+ "COALESCE(MFD.RECON_DAYS,0) AS \"NO OF DAYS\",\r\n"
			+ "COALESCE(MFD.INTEREST_AMOUNT,0) AS \"INTEREST AMOUNT\",\r\n"
			+ "COALESCE(MFD.AIR_AMOUNT,0) AS AIR,\r\n"
			+ "COALESCE(MFD.MFR_AMOUNT,0) AS MFR,\r\n"
			+ "COALESCE(MFD.FMC,0) AS FMC,\r\n"
			+ "COALESCE(MFD.FMC_RECON_DAYS,0) AS \"FMC RECON DAYS\",\r\n"
			+ "MFD.GST GST,\r\n"
			+ "CASE WHEN MFD.ENTRY_TYPE='CLAIM' THEN COALESCE(MFD.INTEREST_AMOUNT,0) ELSE COALESCE(MFD.TOTAL_CONTRIBUTION,0) + COALESCE(MFD.INTEREST_AMOUNT,0) END AS \"TOTAL\",\r\n"
			+ "DENSE_RANK() OVER (ORDER BY MFS.MEM_STATEMENT_ID DESC) MEMBER_RANK\r\n"
			+ "FROM MEMBER_FUND_STATEMENT_DETAILS MFD,MEMBER_FUND_STATEMENT_SUMMARY MFS\r\n"
			+ "WHERE MFD.MEM_STATEMENT_ID = MFS.MEM_STATEMENT_ID\r\n"
			+ "AND MFS.IS_ACTIVE = 1\r\n"
			+ "AND MFS.MEMBER_ID = ?1 \r\n"
			+ "AND MFS.FINANCIAL_YEAR = ?2 \r\n"
			+ "AND MFS.STATEMENT_FREQUENCY = ?3  \r\n"
			+ ") MF\r\n"
			+ "WHERE MEMBER_RANK = 1\r\n"
			+ "ORDER BY TO_DATE(\"DATE\",'DD-MM-YYYY')\r\n"
			+ "),\r\n"
			+ "MEM_CB_V2 AS\r\n"
			+ "(\r\n"
			+ "SELECT * FROM (\r\n"
			+ "SELECT COUNT(1) OVER (PARTITION BY MF.MEM_STATEMENT_ID ORDER BY MF.MEM_STATEMENT_ID DESC) ROWNO,\r\n"
			+ "TO_CHAR(MF.CREATED_ON,'DD-MM-YYYY') AS \"DATE\",'CB' AS \"TYPE\",COALESCE(MF.CLOSING_BALANCE,0) AS AMOUNT,\r\n"
			+ "COALESCE(0,0) AS \"INT RATE\",\r\n"
			+ "0 AS \"NO OF DAYS\",\r\n"
			+ "COALESCE(MF.CLOSING_BALANCE_INT,0) AS \"INTEREST AMOUNT\",\r\n"
			+ "COALESCE(MF.AIR_AMOUNT,0) AS AIR,\r\n"
			+ "COALESCE(MF.MFR_AMOUNT,0) AS MFR,\r\n"
			+ "COALESCE(MF.FMC,0) AS FMC,\r\n"
			+ "0 AS \"FMC RECON DAYS\",\r\n"
			+ "COALESCE(MF.GST,0) AS GST,\r\n"
			+ "COALESCE(MF.POLICY_ACCOUNT_VALUE,0) AS \"TOTAL\",\r\n"
			+ "ROW_NUMBER() OVER (ORDER BY MF.MEM_STATEMENT_ID DESC)  MEMBER_RANK\r\n"
			+ "FROM MEMBER_FUND_STATEMENT_SUMMARY MF,MEM_STATEMENT_V2 MS\r\n"
			+ "WHERE MF.MEM_STATEMENT_ID=MS.MEM_STATEMENT_ID\r\n"
			+ ") WHERE MEMBER_RANK = 1\r\n"
			+ ")\r\n"
			+ "SELECT \r\n"
			+ "ROWNUM AS \"S NO\",\r\n"
			+ "MS.\"DATE\",\r\n"
			+ "MS.\"TYPE\",\r\n"
			+ "MS.AMOUNT,\r\n"
			+ "MS.\"INT RATE\",\r\n"
			+ "MS.\"NO OF DAYS\",\r\n"
			+ "MS.\"INTEREST AMOUNT\",\r\n"
			+ "MS.AIR,\r\n"
			+ "MS.MFR,\r\n"
			+ "MS.FMC,\r\n"
			+ "MS.\"FMC RECON DAYS\",\r\n"
			+ "MS.GST,\r\n"
			+ "MS.TOTAL\r\n"
			+ "FROM MEM_STATEMENT_V2 MS\r\n"
			+ "UNION\r\n"
			+ "SELECT ROWNO+1,\r\n"
			+ "MCB.\"DATE\",\r\n"
			+ "MCB.\"TYPE\",\r\n"
			+ "MCB.AMOUNT,\r\n"
			+ "MCB.\"INT RATE\",\r\n"
			+ "MCB.\"NO OF DAYS\",\r\n"
			+ "MCB.\"INTEREST AMOUNT\",\r\n"
			+ "MCB.AIR,\r\n"
			+ "MCB.MFR,\r\n"
			+ "MCB.FMC,\r\n"
			+ "MCB.\"FMC RECON DAYS\",\r\n"
			+ "MCB.GST,\r\n"
			+ "MCB.TOTAL\r\n"
			+ "FROM MEM_CB_V2 MCB",nativeQuery=true)
	List<Object[]> getFundDetails(String memberId, String financialYear,Integer frequency);

}
