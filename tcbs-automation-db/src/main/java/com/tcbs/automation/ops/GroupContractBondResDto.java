package com.tcbs.automation.ops;

import lombok.*;

import java.util.Objects;

@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class GroupContractBondResDto {
  private static final long serialVersionUID = 1L;
  private Integer blockId;
  private Integer unblockId; // Pledge ID (Unblock): (không trả thông tin này nếu trạng thái hợp đồng bond là "HoanThanhPhongToa")
  private String tradingCode;
  private String listedCode;
  private String bondName;
  private String bondCode;
  private String bondType;
  private String issuer;
  private int principal;
  private int quantity;
  private int contractValue; // Gia tri HD mua bond
  private String status; // Trang thai cua HD: != statusBond
  private String listedUpdatedStatus; // CanBoSungHoSo...
  private String statusBond; // Trang thai cua HD mua bond
  private String issueDate;
  private String maturityDate;
  private String matchingDate; // Ngay mua bond
  private String requestBlockDate; // Ngay yeu cau phong toa
  private String requestUnblockDate; // Ngay yeu cau giai toa

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GroupContractBondResDto that = (GroupContractBondResDto) o;
    return principal == that.principal
      && quantity == that.quantity
      && contractValue == that.contractValue
      && Objects.equals(blockId, that.blockId)
      && Objects.equals(unblockId, that.unblockId)
      && Objects.equals(tradingCode, that.tradingCode)
      && Objects.equals(listedCode, that.listedCode)
      && Objects.equals(bondName, that.bondName)
      && Objects.equals(bondCode, that.bondCode)
      && Objects.equals(bondType, that.bondType)
      && Objects.equals(issuer, that.issuer)
      && Objects.equals(status, that.status)
      && Objects.equals(listedUpdatedStatus, that.listedUpdatedStatus)
      && Objects.equals(statusBond, that.statusBond)
      && Objects.equals(issueDate, that.issueDate)
      && Objects.equals(maturityDate, that.maturityDate)
      && Objects.equals(matchingDate, that.matchingDate)
      && Objects.equals(requestBlockDate, that.requestBlockDate)
      && Objects.equals(requestUnblockDate, that.requestUnblockDate);
  }

  @Override
  public int hashCode() {
    return 31;
  }
}
