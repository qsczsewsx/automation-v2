package com.tcbs.automation.staging;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import wiremock.org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VipBankEntity {
  @Id
  private Integer customerId;
  private String custodyCD;
  private String tcbsId;
  private String vipType;

  @Step
  public static List<VipBankEntity> getVipInfo(List<String> tcbsId, String inDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT c.customerId, c.CustodyCode, c.tcbsid, s.AUM_VIP   \r\n");
    queryStringBuilder.append("FROM Stg_tcb_Customer c \r\n");
    queryStringBuilder.append("LEFT JOIN [Stg_tcbs-share_R_TBL_CU_0015_IB_CST_FOR_TCB] s on c.CustomerID = s.CustomerID  \r\n");
    queryStringBuilder.append("WHERE CONVERT(DATE, EFF_FM_Date) <= CONVERT(DATE, :inDate)  \r\n");
    queryStringBuilder.append("AND CONVERT(DATE, END_TO_DATE) > CONVERT(DATE, :inDate)  \r\n");
    queryStringBuilder.append("AND c.TCBSID in ( :tcbsId ) ;  \r\n");

    List<VipBankEntity> listResult = new ArrayList<>();
    try {
      List<Object[]> result = Staging.stagingDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("inDate", inDate)
        .setParameter("tcbsId", tcbsId)
        .getResultList();

      if (CollectionUtils.isNotEmpty(result)) {
        result.forEach(object -> {
            VipBankEntity userInfo = VipBankEntity.builder()
              .customerId((Integer) object[0])
              .custodyCD((String) object[1])
              .tcbsId((String) object[2])
              .vipType((String) object[3])
              .build();
            listResult.add(userInfo);
          }
        );

        return listResult;
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
