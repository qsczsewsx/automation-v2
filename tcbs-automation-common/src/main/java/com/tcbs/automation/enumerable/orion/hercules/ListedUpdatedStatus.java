package com.tcbs.automation.enumerable.orion.hercules;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
@Slf4j
public enum ListedUpdatedStatus {
  CAN_BO_SUNG_HO_SO("CAN_BO_SUNG_HO_SO", "Cần bổ sung hồ sơ"),
  DANG_BO_SUNG_HO_SO("DANG_BO_SUNG_HO_SO", "Đang bổ sung hồ sơ"),
  TU_CHOI_HO_SO("TU_CHOI_HO_SO", "Từ chối hồ sơ"),
  XAC_NHAN_HO_SO("XAC_NHAN_HO_SO", "Xác nhận hồ sơ"),
  HOAN_THANH_BO_SUNG_HO_SO("HOAN_THANH_BO_SUNG_HO_SO", "Hoàn thành bổ sung hồ sơ");
  private String value;
  private String name;

  @Override
  public String toString() {
    return value;
  }

  @JsonCreator
  public static ListedUpdatedStatus fromValue(String value) {
    Optional<ListedUpdatedStatus> comp = Stream.of(values()).filter(e -> e.getValue().equals(value)).findFirst();
    if (comp.isPresent()) {
      return comp.get();
    }
    log.info("Input error: " + value);
    return null;
  }
}
