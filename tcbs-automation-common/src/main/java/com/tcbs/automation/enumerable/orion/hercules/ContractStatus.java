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
public enum ContractStatus {
  DA_XAC_NHAN_PHONG_TOA("DA_XAC_NHAN_PHONG_TOA", "Đã được xác nhận phong tỏa"),
  DA_YEU_CAU_PHONG_TOA("DA_YEU_CAU_PHONG_TOA", "Đã gửi yêu cầu phong tỏa"),
  DA_PHONG_TOA("DA_PHONG_TOA", "Đã phong tỏa tại TCBS, chờ hoàn thiện HS gốc"),
  DA_UPLOAD_SCAN_PHONG_TOA("DA_UPLOAD_SCAN_PHONG_TOA", "Đã upload bản scan HS phong tỏa"),
  TU_CHOI_PHONG_TOA("TU_CHOI_PHONG_TOA", "Từ chối phong tỏa"),
  HOAN_THANH_PHONG_TOA("HOAN_THANH_PHONG_TOA", "Hoàn thành phong tỏa"),
  DA_YEU_CAU_GIAI_TOA("DA_YEU_CAU_GIAI_TOA", "Đã gửi yêu cầu giải tỏa"),
  DA_XAC_NHAN_GIAI_TOA("DA_XAC_NHAN_GIAI_TOA", "Đã được xác nhận giải tỏa"),
  TU_CHOI_GIAI_TOA("TU_CHOI_GIAI_TOA", "Từ chối giải tỏa"),
  DA_GIAI_TOA("DA_GIAI_TOA", "Đã giải tỏa tại TCBS, chờ hoàn thiện HS gốc"),
  DA_UPLOAD_SCAN_GIAI_TOA("DA_UPLOAD_SCAN_GIAI_TOA", "Đã upload bản scan HS giải tỏa"),
  HOAN_THANH_GIAI_TOA("HOAN_THANH_GIAI_TOA", "Hoàn thành giải tỏa"),
  XAC_NHAN_TT_GIAI_TOA("XAC_NHAN_TT_GIAI_TOA", "E.ops xác nhận thông tin đề nghị giải tỏa"),
  HUY_YEU_CAU("HUY_YEU_CAU", "RM hủy luồng");
  private String value;
  private String name;

  @Override
  public String toString() {
    return value;
  }

  @JsonCreator
  public static ContractStatus fromValue(String value) {
    Optional<ContractStatus> comp = Stream.of(values()).filter(e -> e.getValue().equals(value)).findFirst();
    if (comp.isPresent()) {
      return comp.get();
    }
    log.info("Input error: " + value);
    return null;
  }
}
