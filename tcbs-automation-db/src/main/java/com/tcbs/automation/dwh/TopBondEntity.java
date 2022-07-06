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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TopBondEntity {
  @Id
  private String productCode;
  private String bondCode;
  private BigDecimal buyAmt;
  private BigInteger ranking;
  private String issuer;
  private BigDecimal availableForConfirm;
  private Double investmentTimeByMonth;

  @Step("Get top Bond from DWH")
  public static List<TopBondEntity> getTopBond() {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append(" select ts.ProductCode, ts.BondCode, round(BuyAmt_30days/1000000000.0,3) as BuyAmt, Ranking, ch.Company as Issuer,  ");
    queryStringBuilder.append(" bpl.AvailableForConfirm, bpl.InvestmentTimeByMonth  ");
    queryStringBuilder.append(" from (select * from smy_dwh_tcb_Bond_RankingBySales where etlcurdate = (select max(etlcurdate) from  smy_dwh_tcb_Bond_RankingBySales)) ts  ");
    queryStringBuilder.append(
      " left join (select * from smy_dwh_tcb_Bond_Characters where etlcurdate = (select max(etlcurdate) from smy_dwh_tcb_Bond_Characters)) ch on ts.ProductCode = ch.ProductCode  ");
    queryStringBuilder.append(" left join bondproductlisting bpl on ts.ProductCode = bpl.ProductCode  ");
    queryStringBuilder.append(" left join (select * from CheckPricing2 where Date = cast(getdate() as date) ) p on ts.ProductCode = p.ProductCode  ");
    queryStringBuilder.append(" where Ranking <= 5  ");

    List<TopBondEntity> listResult = new ArrayList<>();
    try {
      List<Object[]> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .getResultList();

      if (CollectionUtils.isNotEmpty(result)) {
        result.forEach(object -> {
            TopBondEntity entity = TopBondEntity.builder()
              .productCode((String) object[0])
              .bondCode((String) object[1])
              .buyAmt((BigDecimal) object[2])
              .ranking((BigInteger) object[3])
              .issuer((String) object[4])
              .availableForConfirm((BigDecimal) object[5])
              .investmentTimeByMonth((Double) object[6])
              .build();
            listResult.add(entity);
          }
        );
      }
      Dwh.dwhDbConnection.closeSession();
      return listResult;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }
}
