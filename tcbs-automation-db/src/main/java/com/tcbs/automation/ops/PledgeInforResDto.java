package com.tcbs.automation.ops;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PledgeInforResDto {
  private static final long serialVersionUID = 1L;
  List<GroupContractBondResDto> contractSameUnblocks = new ArrayList<>(); // Các HĐ thuộc cùng Unblock ID NHUNG khong co status "HoanThanhPhongToa"
  List<GroupContractBondResDto> contractAddRecords = new ArrayList<>(); // Các HĐ có trạng thái "Cần bổ sung hồ sơ" NHUNG khong co status "HoanThanhPhongToa"
  List<GroupContractBondResDto> contractOthers = new ArrayList<>(); // Các HĐ còn lại
  private Integer id;
  private String tcbsId;
  private String custName; // Ten KH
  private String pledgeCode; // So HD cam co
  private String custodyCode; // 105C
  private String pledgeDate;
  private String statusPledge; // Trang thai HD cam co

  @Override
  public String toString() {
    return "PledgeInforResDto{" +
      "id=" + id +
      ", tcbsId='" + tcbsId + '\'' +
      ", custName='" + custName + '\'' +
      ", pledgeCode='" + pledgeCode + '\'' +
      ", custodyCode='" + custodyCode + '\'' +
      ", pledgeDate=" + pledgeDate +
      ", statusPledge='" + statusPledge + '\'' +
      ", contractSameUnblocks=" + contractSameUnblocks +
      ", contractAddRecords=" + contractAddRecords +
      ", contractOthers=" + contractOthers +
      '}';
  }
}
