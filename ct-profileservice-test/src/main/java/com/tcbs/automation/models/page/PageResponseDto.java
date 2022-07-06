package com.tcbs.automation.models.page;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcbs.automation.models.ProfileDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDto {
  // Request infomation
  @JsonIgnore
  private String condition;
  private Integer page;
  private Integer size;
  private String orderBy;

  // response information
  private Long totalElements;

  private List<ProfileDto> content;
}
