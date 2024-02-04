package com.lic.epgs.common.utils;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRequest<T> {

	private ObjectMapper mapper = new ObjectMapper();

	public T readEncoded(String json, Class<T> contentClass) throws IOException {
		String decodedJson = new String(Base64.getDecoder().decode(json));
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(decodedJson, contentClass);
	}

	public T convertJsonStringToObject(String json, Class<T> contentClass) throws IOException {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(json, contentClass);
	}

	public String convertObjectToString(T t) {
		String response = null;
		try {
			response = mapper.writeValueAsString(t);
		} catch (JsonProcessingException e) {
		}
		return response;
	}
}
