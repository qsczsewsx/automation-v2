package com.tcbs.automation.ops;

import lombok.*;

@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PledgeInforReqDto {
  private static final long serialVersionUID = 1L;

  private String custOrPledgeCode;
  private String requestType; // Phong tỏa/Giải tỏa
  private String pledgeStatus;
  private String requestDateFrom;
  private String requestDateTo;

  @Override
  public String toString() {
    return "PledgeInforReqDto{" +
      "custOrPledgeCode='" + custOrPledgeCode + '\'' +
      ", requestType='" + requestType + '\'' +
      ", pledgeStatus='" + pledgeStatus + '\'' +
      ", requestDateFrom='" + requestDateFrom + '\'' +
      ", requestDateTo='" + requestDateTo + '\'' +
      '}';
  }
}
