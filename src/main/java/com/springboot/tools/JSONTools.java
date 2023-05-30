package com.springboot.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONTools {
	  public static JsonNode GetJSONFromString(String rawData) throws JsonProcessingException {
	        ObjectMapper mapper = new ObjectMapper();
	        JsonNode jsonObject = mapper.readTree(rawData);
	        return jsonObject;
	    }
}
