package com.automation.tools;

import java.math.BigDecimal;
import java.util.Objects;

public class NumberUtil {
  public static boolean isNullorZero(Number i) {
    return 0 == (i == null ? 0 : i.intValue());
  }

  public static boolean isNotNullorZero(Number i) {
    return !(0 == (i == null ? 0 : i.intValue()));
  }

  public static Double calRatio(Double number, Double shareCirculate) {
    if (number == null || (shareCirculate == null || shareCirculate <= 0)) {
      return null;
    } else {
      return number / shareCirculate;
    }
  }

  public static BigDecimal createBigDecimal(Object obj) {
    return Objects.isNull(obj) ? null : BigDecimal.valueOf(Double.valueOf(String.valueOf(obj)).longValue(), 0);
  }
}
