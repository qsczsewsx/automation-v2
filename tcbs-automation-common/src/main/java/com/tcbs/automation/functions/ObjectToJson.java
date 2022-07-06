package com.tcbs.automation.functions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ObjectToJson {
  public static String parseObjectToJson(Object object) {

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    try {
      return ow.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String parseObjectToJsonWithoutNull(Object object) {

    ObjectWriter ow = new ObjectMapper()
      .setSerializationInclusion(Include.NON_NULL)
      .writer()
      .withDefaultPrettyPrinter();
    try {
      return ow.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }
}
