package com.tcbs.automation.bondlifecycle.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReferenceDto extends BaseDto {

  private String code;

  private String name;

  private String value;

}
