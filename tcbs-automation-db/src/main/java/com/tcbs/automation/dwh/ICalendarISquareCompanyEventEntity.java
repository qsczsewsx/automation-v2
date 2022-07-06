package com.tcbs.automation.dwh;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ICalendarISquareCompanyEventEntity {
  public Long companyGaId;
  public String eventId;
  public Long bondId;
  public Long groupId;
  public Long creatorId;
  public Long eventTypeId;
  public String feeTypeName;
  public Integer paymentPeriod;
  public Integer paymentExpiredDay;
  public String companyName;
  public String bondName;
  public String bondCode;
  public String eventName;
  public String eventDesc;
  public String customerType;
  public String eventSource;
  public String eventNote;
  public String eventStatus;
  public String eventType;
  public String otherStatus;
  public Date eventDate;
  public Date startDate;
  public Date endDate;
  public Date paymentDate;
  public String paymentLabel;
  public Long paymentValue;
}
