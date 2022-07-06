package com.tcbs.automation.coco.performance.share;

import com.tcbs.automation.coco.ColumnIndex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountPerfReturn {
  @ColumnIndex(name = "EOMONTH", index = 0, ignoreIfNull = true)
  private Date timestamp;

  @ColumnIndex(name = "PORTF_RETURN", index = 1, ignoreIfNull = true)
  private Double returns;
}
