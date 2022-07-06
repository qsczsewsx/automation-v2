package com.tcbs.automation.enumerable.orion.hercules;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum PledgeStatus {
  CAN_BO_SUNG_HO_SO("CAN_BO_SUNG_HO_SO", "Cần bổ sung hồ sơ"),
  TU_CHOI("TU_CHOI", "Từ chối"),
  CHO_HOAN_THIEN_HO_SO("CHO_HOAN_THIEN_HO_SO", "Chờ hoàn thiện hồ sơ"),
  DA_GUI_YEU_CAU_PHONG_TOA("DA_GUI_YEU_CAU_PHONG_TOA", "Đã gửi yêu cầu phong tỏa"),
  HOAN_TAT_PHONG_TOA("HOAN_TAT_PHONG_TOA", "Hoàn tất phong tỏa"),
  DA_GUI_YEU_CAU_GIAI_TOA("DA_GUI_YEU_CAU_GIAI_TOA", "Đã gửi yêu cầu giải tỏa"),
  DA_XAC_NHAN_THONG_TIN_GIAI_TOA("DA_XAC_NHAN_THONG_TIN_GIAI_TOA", "Đã xác nhận thông tin giải tỏa"),
  DA_GIAI_TOA_MOT_PHAN("DA_GIAI_TOA_MOT_PHAN", "Đã giải tỏa 1 phần"),
  DA_GIAI_TOA("DA_GIAI_TOA", "Đã giải tỏa");
  @JsonValue
  private String value;
  private String name;

  @Override
  public String toString() {
    return value;
  }

  @JsonCreator
  public static PledgeStatus fromValue(String value) throws Exception {
    Optional<PledgeStatus> comp = Stream.of(values()).filter(e -> e.getValue().equals(value)).findFirst();
    if (comp.isPresent()) {
      return comp.get();
    }
    throw new Exception("Input=" + value);
  }
}
