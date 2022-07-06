package com.tcbs.automation.enumerable.orion.hercules;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DisplayBondStatus {
  CHO_HOAN_THIEN_HO_SO("CHO_HOAN_THIEN_HO_SO", "Chờ hoàn thiện hồ sơ"),
  DA_GUI_YEU_CAU_PHONG_TOA("DA_GUI_YEU_CAU_PHONG_TOA", "Đã gửi yêu cầu phong tỏa"),
  TU_CHOI_PHONG_TOA("TU_CHOI_PHONG_TOA", "Từ chối phong tỏa"),
  DANG_CAM_CO("DANG_CAM_CO", "Đang cầm cố"),
  DA_GUI_YEU_CAU_GIAI_TOA("DA_GUI_YEU_CAU_GIAI_TOA", "Đã gửi yêu cầu giải tỏa"),
  DA_XAC_NHAN_TT_GIAI_TOA("DA_XAC_NHAN_TT_GIAI_TOA", "Đã xác nhận thông tin giải tỏa"),
  TU_CHOI_GIAI_TOA("TU_CHOI_GIAI_TOA", "Từ chối giải tỏa"),
  DA_GIAI_TOA("DA_GIAI_TOA", "Đã giải tỏa"),
  CAN_BO_SUNG_HO_SO("CAN_BO_SUNG_HO_SO", "Cần bổ sung hồ sơ");
  @JsonValue
  private String value;
  private String name;

  @Override
  public String toString() {
    return value;
  }
}
