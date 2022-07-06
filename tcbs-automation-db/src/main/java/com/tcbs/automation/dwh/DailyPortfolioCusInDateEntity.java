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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyPortfolioCusInDateEntity {
  private String custodyCd;
  private Date reportDate;
  @Id
  private String productCode;
  private Double quantity;
  private Double amount;
  private Double cogs;
  private String acctNo;
  private String type;

  @Step("select data")
  public static List<DailyPortfolioCusInDateEntity> getDPCusInfo(String custodyCd, String date, String acctNo) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT *  FROM (  ");
    queryStringBuilder.append("   SELECT *, 'fund' as [type] FROM (  ");
    queryStringBuilder.append("     SELECT f.custodyCd, product as productCode, Balance as quantity, currAmt as amount, costAmount as cogs, acctNo, DateReport  ");
    queryStringBuilder.append("     FROM vw_DP_FundBal f  ");
    queryStringBuilder.append("     LEFT JOIN ( ");
    queryStringBuilder.append("     			SELECT custodycd, acctNo ");
    queryStringBuilder.append("     			FROM stg_dwh_flx_acctNo ");
    queryStringBuilder.append("     			WHERE mrType = 'N' AND custodycd = :custodyCd  ");
    queryStringBuilder.append("     			GROUP BY custodycd, acctNo) normalAcct  ");
    queryStringBuilder.append("     			ON f.CustodyCD = normalAcct.CustodyCD  ");
    queryStringBuilder.append("     WHERE f.custodycd = :custodyCd AND CAST(DateReport as date) = :date AND currAmt IS NOT NULL AND currPrice IS NOT NULL) fund  ");
    queryStringBuilder.append("   UNION ALL  ");
    queryStringBuilder.append("   SELECT *, 'bond' as [type] FROM (  ");
    queryStringBuilder.append("     SELECT b.custodyCd, bondCode as productCode, balanceQuantity as quantity, Principal as amount, Principal as cogs, acctNo, DateReport  ");
    queryStringBuilder.append("     FROM vw_DP_BondBal b  ");
    queryStringBuilder.append("     LEFT JOIN ( ");
    queryStringBuilder.append("     			SELECT custodycd, acctNo ");
    queryStringBuilder.append("     			FROM stg_dwh_flx_acctNo ");
    queryStringBuilder.append("     			WHERE mrType = 'N' AND custodycd = :custodyCd  ");
    queryStringBuilder.append("     			GROUP BY custodycd, acctNo) normalAcct  ");
    queryStringBuilder.append("     			ON b.CustodyCD = normalAcct.CustodyCD  ");
    queryStringBuilder.append("     WHERE b.custodycd = :custodyCd AND CAST(DateReport as date) = :date AND balanceQuantity IS NOT NULL AND Principal IS NOT NULL) bond  ");
    queryStringBuilder.append("   UNION ALL  ");
    queryStringBuilder.append("   SELECT *, 'stock' as [type] FROM (  ");
    queryStringBuilder.append("     SELECT custodyCd, symbol as productCode, quantity, mktAmt as amount, CostPrice*Quantity as cogs, afacctno as acctNo, DateReport  ");
    queryStringBuilder.append("     FROM vw_DP_StockBal_AcctNo  ");
    queryStringBuilder.append("     WHERE custodycd = :custodyCd AND CAST(DateReport as date) = :date ) stock ) final ");
    queryStringBuilder.append("WHERE ( :acctNo = '' OR acctNo = :acctNo )  ");

    List<DailyPortfolioCusInDateEntity> listResult = new ArrayList<>();
    try {
      List<Object[]> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("custodyCd", custodyCd)
        .setParameter("date", date)
        .setParameter("acctNo", acctNo)
        .getResultList();

      if (CollectionUtils.isNotEmpty(result)) {
        result.forEach(object -> {
            DailyPortfolioCusInDateEntity info = DailyPortfolioCusInDateEntity.builder()
              .custodyCd((String) object[0])
              .productCode((String) object[1])
              .quantity((Double) object[2])
              .amount((Double) object[3])
              .cogs((Double) object[4])
              .acctNo((String) object[5])
              .reportDate((Date) object[6])
              .type((String) object[7])
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
