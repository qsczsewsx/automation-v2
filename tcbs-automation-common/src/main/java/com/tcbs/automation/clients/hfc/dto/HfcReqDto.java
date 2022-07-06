package com.tcbs.automation.clients.hfc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcbs.automation.tools.DateUtils;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Date;

@Data
@SuperBuilder
public class HfcReqDto {
  private String custodyId;
  private String accountNo;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.ISO_DATE_FORMAT, timezone = DateUtils.TIMEZONE_VN)
  private Date from;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.ISO_DATE_FORMAT, timezone = DateUtils.TIMEZONE_VN)
  private Date to;
  @Builder.Default
  private Integer page = 1;
  @Builder.Default
  private Integer size = 25;

  public MultiValueMap<String, String> toHfcQueryParamMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    if (page != null) {
      map.add("page", String.valueOf(page));
    }
    if (size != null) {
      map.add("size", String.valueOf(size));
    }
    if (custodyId != null) {
      map.add("custody", custodyId);
    }
    if (accountNo != null) {
      map.add("accountId", accountNo);
    }
    if (from != null) {
      map.add("from", DateUtils.toString(from, DateUtils.ISO_DATE_FORMAT));
    }
    if (to != null) {
      map.add("to", DateUtils.toString(to, DateUtils.ISO_DATE_FORMAT));
    }
    return map;
  }

  public MultiValueMap<String, String> toIcopyQueryParamMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    if (page != null) {
      map.add("page", String.valueOf(page));
    }
    if (size != null) {
      map.add("size", String.valueOf(size));
    }
    if (custodyId != null) {
      map.add("custodyId", custodyId);
    }
    if (accountNo != null) {
      map.add("accountNo", accountNo);
    }
    if (from != null) {
      map.add("from", DateUtils.toString(from, DateUtils.ISO_DATE_FORMAT));
    }
    if (to != null) {
      map.add("to", DateUtils.toString(to, DateUtils.ISO_DATE_FORMAT));
    }
    return map;
  }
}
