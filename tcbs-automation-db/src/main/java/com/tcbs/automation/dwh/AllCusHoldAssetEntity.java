package com.tcbs.automation.dwh;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import wiremock.org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "stg_dwh_flx_acctNo")
public class AllCusHoldAssetEntity {
  private String tcbsid;
  @Id
  private String custodyCD;
  private String fullName;
  private Integer cusType;
  private String normalAcct;
  private String marginAcct;

  @Step("select data")
  public static List<AllCusHoldAssetEntity> getAllCusInfo() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * FROM smy_dwh_cus_holdAsset  ");
    queryStringBuilder.append("ORDER BY CustodyCd ");

    List<AllCusHoldAssetEntity> listResult = new ArrayList<>();
    try {
      List<Object[]> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .getResultList();

      if (CollectionUtils.isNotEmpty(result)) {
        result.forEach(object -> {
            AllCusHoldAssetEntity info = AllCusHoldAssetEntity.builder()
              .custodyCD((String) object[0])
              .tcbsid((String) object[1])
              .fullName((String) object[2])
              .cusType((Integer) object[3])
              .normalAcct((String) object[4])
              .marginAcct((String) object[5])
              .build();
            listResult.add(info);
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
