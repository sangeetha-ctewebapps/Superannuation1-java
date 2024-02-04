/**
 * 
 */
package com.lic.epgs.common.exception;

import java.util.NoSuchElementException;

import javax.persistence.NonUniqueResultException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.JDBCException;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.LockAcquisitionException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lic.epgs.common.entity.AppLogErrorEntity;
import com.lic.epgs.common.repository.AppLogErrorRepository;
import com.lic.epgs.utils.CommonConstants;
import com.lic.epgs.utils.CommonUtils;
import com.lic.epgs.utils.DateUtils;

/**
 * @author Muruganandam
 * @reference1 https://www.baeldung.com/hibernate-exceptions
 * @reference2 https://docs.spring.io/spring-framework/docs/6.0.x/reference/html/data-access.html#dao-exceptions
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private AppLogErrorRepository appLogErrorRepository;

	private static final String GLOBAL_EXCEPTION = "GlobalExceptionHandler Error:{}";
	private static final String INTERNAL_APP_ERROR = "Unable to process the request due to internal application error.";

	@Autowired
	@Qualifier("jsonObjectMapper")
	private ObjectMapper jsonObjectMapper;
	protected final Logger logger = LogManager.getLogger(getClass());

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ApplicationErrorDto globalExceptionHandler(Exception exception, WebRequest request) {

		Long refNo = saveErrorLog(exception, request);
		logger.error(GLOBAL_EXCEPTION, CommonUtils.getStackTrace(exception));

		return ApplicationErrorDto.builder().transactionStatus(CommonConstants.ERROR)
				.transactionMessage(CommonUtils.exceptionName(CommonUtils.getRootCauseMessage(exception)))
				// .transactionMessage(CommonUtils.getStackTrace(exception))
				.errorCode(0).errorRefNo(refNo).build();
	}

	@ExceptionHandler(ApplicationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ApplicationErrorDto fundApplicationException(ApplicationException exception, WebRequest request) {

		Long refNo = saveErrorLog(exception, request);
		logger.error(GLOBAL_EXCEPTION, CommonUtils.getStackTrace(exception));

		return ApplicationErrorDto.builder().transactionStatus(CommonConstants.ERROR)
				.transactionMessage(exception.getMessage()).errorCode(0).errorRefNo(refNo).build();
	}

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ApplicationErrorDto noSuchElementException(NoSuchElementException exception, WebRequest request) {

		Long refNo = saveErrorLog(exception, request);
		logger.error(GLOBAL_EXCEPTION, CommonUtils.getStackTrace(exception));

		return ApplicationErrorDto.builder().transactionStatus(CommonConstants.ERROR)
				.transactionMessage(exception.getMessage()).errorCode(0).errorRefNo(refNo).build();
	}

	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ApplicationErrorDto nullExceptionHandler(NullPointerException exception, WebRequest request) {

		Long refNo = saveErrorLog(exception, request);
		logger.error(GLOBAL_EXCEPTION, exception);

		return ApplicationErrorDto.builder().transactionStatus(CommonConstants.ERROR)
				.transactionMessage(INTERNAL_APP_ERROR).errorCode(0).errorRefNo(refNo).build();
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ApplicationErrorDto constraintViolationException(ConstraintViolationException exception,
			WebRequest request) {

		Long refNo = saveErrorLog(exception, request);
		logger.error(GLOBAL_EXCEPTION, CommonUtils.getStackTrace(exception));

		return ApplicationErrorDto.builder().transactionStatus(CommonConstants.ERROR)
				.transactionMessage("Integrity constraint is violated or null value to a not null object.").errorCode(0)
				.errorRefNo(refNo).build();
	}

	@ExceptionHandler(DataException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ApplicationErrorDto dataException(DataException exception, WebRequest request) {

		Long refNo = saveErrorLog(exception, request);
		logger.error(GLOBAL_EXCEPTION, CommonUtils.getStackTrace(exception));

		return ApplicationErrorDto.builder().transactionStatus(CommonConstants.ERROR)
				.transactionMessage("Incorrect request parameter value. Mismatch in datatype/length.").errorCode(0)
				.errorRefNo(refNo).build();
	}

	@ExceptionHandler(JDBCConnectionException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ApplicationErrorDto jDBCConnectionException(JDBCConnectionException exception, WebRequest request) {

		Long refNo = saveErrorLog(exception, request);
		logger.error(GLOBAL_EXCEPTION, CommonUtils.getStackTrace(exception));

		return ApplicationErrorDto.builder().transactionStatus(CommonConstants.ERROR)
				.transactionMessage("Error in connecting with the data source.").errorCode(0).errorRefNo(refNo).build();
	}

	@ExceptionHandler(LockAcquisitionException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ApplicationErrorDto lockAcquisitionException(LockAcquisitionException exception, WebRequest request) {

		Long refNo = saveErrorLog(exception, request);
		logger.error(GLOBAL_EXCEPTION, CommonUtils.getStackTrace(exception));

		return ApplicationErrorDto.builder().transactionStatus(CommonConstants.ERROR)
				.transactionMessage("Lock error occured. Try after sometime.").errorCode(0).errorRefNo(refNo).build();
	}

	@ExceptionHandler(PessimisticLockException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ApplicationErrorDto pessimisticLockException(PessimisticLockException exception, WebRequest request) {

		Long refNo = saveErrorLog(exception, request);
		logger.error(GLOBAL_EXCEPTION, CommonUtils.getStackTrace(exception));

		return ApplicationErrorDto.builder().transactionStatus(CommonConstants.ERROR)
				.transactionMessage("Lock error occured. Try after sometime.").errorCode(0).errorRefNo(refNo).build();
	}

	@ExceptionHandler(QueryTimeoutException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ApplicationErrorDto queryTimeoutException(QueryTimeoutException exception, WebRequest request) {

		Long refNo = saveErrorLog(exception, request);
		logger.error(GLOBAL_EXCEPTION, CommonUtils.getStackTrace(exception));

		return ApplicationErrorDto.builder().transactionStatus(CommonConstants.ERROR)
				.transactionMessage(INTERNAL_APP_ERROR).errorCode(0).errorRefNo(refNo).build();
	}

	@ExceptionHandler(SQLGrammarException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ApplicationErrorDto sQLGrammarException(SQLGrammarException exception, WebRequest request) {

		Long refNo = saveErrorLog(exception, request);
		logger.error(GLOBAL_EXCEPTION, CommonUtils.getStackTrace(exception));

		return ApplicationErrorDto.builder().transactionStatus(CommonConstants.ERROR)
				.transactionMessage(INTERNAL_APP_ERROR).errorCode(0).errorRefNo(refNo).build();
	}

	@ExceptionHandler({ DataAccessException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ApplicationErrorDto genericJDBCException(DataAccessException exception, WebRequest request) {

		Long refNo = saveErrorLog(exception, request);
		logger.error(GLOBAL_EXCEPTION, CommonUtils.getStackTrace(exception));

		return ApplicationErrorDto.builder().transactionStatus(CommonConstants.ERROR)
				.transactionMessage(INTERNAL_APP_ERROR).errorCode(0).errorRefNo(refNo).build();
	}

	@ExceptionHandler({ JDBCException.class, CannotGetJdbcConnectionException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ApplicationErrorDto jDBCException(JDBCException exception, WebRequest request) {

		Long refNo = saveErrorLog(exception, request);
		logger.error(GLOBAL_EXCEPTION, CommonUtils.getStackTrace(exception));

		return ApplicationErrorDto.builder().transactionStatus(CommonConstants.ERROR)
				.transactionMessage(INTERNAL_APP_ERROR).errorCode(0).errorRefNo(refNo).build();
	}

	@ExceptionHandler(InvalidDataAccessApiUsageException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ApplicationErrorDto invalidDataAccessApiUsageException(InvalidDataAccessApiUsageException exception,
			WebRequest request) {

		Long refNo = saveErrorLog(exception, request);
		logger.error(GLOBAL_EXCEPTION, CommonUtils.getStackTrace(exception));

		return ApplicationErrorDto.builder().transactionStatus(CommonConstants.ERROR)
				.transactionMessage(INTERNAL_APP_ERROR).errorCode(0).errorRefNo(refNo).build();
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ApplicationErrorDto httpMessageNotReadableException(HttpMessageNotReadableException exception,
			WebRequest request) {

		Long refNo = saveErrorLog(exception, request);
		logger.error(GLOBAL_EXCEPTION, CommonUtils.getStackTrace(exception));

		return ApplicationErrorDto.builder().transactionStatus(CommonConstants.ERROR)
				.transactionMessage("Required request body is missing or bad request").errorCode(0).errorRefNo(refNo)
				.build();
	}

	@ExceptionHandler({ NonUniqueObjectException.class, NonUniqueResultException.class,
			IncorrectResultSizeDataAccessException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ApplicationErrorDto nonUniqueObjectException(Exception exception, WebRequest request) {

		Long refNo = saveErrorLog(exception, request);
		logger.error(GLOBAL_EXCEPTION, CommonUtils.getStackTrace(exception));

		return ApplicationErrorDto.builder().transactionStatus(CommonConstants.ERROR)
				.transactionMessage("Multiple result for the given request.").errorCode(0).errorRefNo(refNo).build();
	}

	public Long saveErrorLog(Exception exception, WebRequest request) {
		try {

			AppLogErrorEntity appLogErrorEntity = convertStringToJsonObject(request);
			if (appLogErrorEntity == null) {
				appLogErrorEntity = new AppLogErrorEntity();
			}
			appLogErrorEntity.setUri(request.getDescription(true));

			String errorMessage = CommonUtils.getStackTrace(exception);
			if (StringUtils.isNotBlank(errorMessage)) {
				if (errorMessage.length() > 4000) {
					appLogErrorEntity.setErrorMessage(CommonUtils.getStackTrace(exception).substring(0, 3999));
				} else {
					appLogErrorEntity.setErrorMessage(errorMessage);
				}
			}

			appLogErrorEntity.setCreatedOn(DateUtils.sysDate());
			appLogErrorRepository.save(appLogErrorEntity);
			return appLogErrorEntity.getReferenceId();
		} catch (IllegalArgumentException e) {
			logger.error("GlobalExceptionHandler::saveError Log", e);
		}
		return 0l;
	}

	public AppLogErrorEntity convertStringToJsonObject(WebRequest request) {
		try {
			if (StringUtils.isNotBlank(request.getHeader("api-request"))) {
				return jsonObjectMapper.readValue(request.getHeader("api-request"), AppLogErrorEntity.class);
			}

		} catch (JsonProcessingException e) {
			logger.error("GlobalExceptionHandler::saveError Log", e);
		}
		return new AppLogErrorEntity();
	}

}
